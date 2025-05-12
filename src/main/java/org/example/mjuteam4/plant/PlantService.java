package org.example.mjuteam4.plant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@RequiredArgsConstructor
@Slf4j
public class PlantService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final PlantRepository plantRepository;

    @Value("${openapi.data}")
    private String serviceKey;

    public String search(String keyword) {
        String baseUrl = "http://openapi.nature.go.kr/openapi/service/rest/PlantService/plntIlstrSearch";

        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("st", "1")                 // 1: 국명 기준
                .queryParam("sw", keyword)            // 검색어
                .queryParam("dateGbn", "")            // 전체
                .queryParam("dateFrom", "")
                .queryParam("dateTo", "")
                .queryParam("numOfRows", "10")
                .queryParam("pageNo", "1")
                .build(false)                         // 인코딩 비활성화 (이미 인코딩된 serviceKey 고려)
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            log.info("status = {}", response.getStatusCode());
            log.info("body = {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("API 호출 실패", e);
            throw e;
        }
    }


    public String searchOne(String q1) {
        String baseUrl = "http://openapi.nature.go.kr/openapi/service/rest/PlantService/plntIlstrInfo";

        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("q1", q1)                 // 1: 국명 기준
                .build(false)                         // 인코딩 비활성화 (이미 인코딩된 serviceKey 고려)
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            log.info("status = {}", response.getStatusCode());
            log.info("body = {}", response.getBody());
            String responseXml = response.getBody();
            savePlantFromSearchOneResponse(responseXml);
            return response.getBody();
        } catch (Exception e) {
            log.error("API 호출 실패", e);
            throw e;
        }
    }

    private void savePlantFromSearchOneResponse(String xmlResponse) {
        try {
            log.info("xmlResponse = {}", xmlResponse);
            JSONObject json = new JSONObject(xmlResponse);
            log.info("json = {}", json);
            JSONObject item = json
                    .getJSONObject("response")
                    .getJSONObject("body")
                    .getJSONObject("item");

            Plant plant = Plant.builder()
                    .plantPilbkNo(item.optString("plantPilbkNo"))
                    .plantGnrlNm(nullIfEmpty(item.optString("plantGnrlNm")))
                    .plantSpecsScnm(nullIfEmpty(item.optString("plantSpecsScnm")))
                    .familyKorNm(nullIfEmpty(item.optString("familyKorNm")))
                    .genusKorNm(nullIfEmpty(item.optString("genusKorNm")))
                    .leafDesc(nullIfEmpty(item.optString("leafDesc")))
                    .flwrDesc(nullIfEmpty(item.optString("flwrDesc")))
                    .fritDesc(nullIfEmpty(item.optString("fritDesc")))
                    .shpe(nullIfEmpty(item.optString("shpe")))
                    .sz(nullIfEmpty(item.optString("sz")))
                    .grwEvrntDesc(nullIfEmpty(item.optString("grwEvrntDesc")))
                    .dstrb(nullIfEmpty(item.optString("dstrb")))
                    .useMthdDesc(nullIfEmpty(item.optString("useMthdDesc")))
                    .imgUrl(nullIfEmpty(item.optString("imgUrl")))
                    .build();

            plantRepository.save(plant);
            log.info("✅ 저장 완료: {} ({})", plant.getPlantGnrlNm(), plant.getPlantPilbkNo());

        } catch (Exception e) {
            log.error("❌ 식물 저장 실패", e);
        }
    }

    private String nullIfEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }

    /**
     * 식물 도감 다 불러와서 디비에 저장
     */

    public void fetchAndSaveAllPlants() {
        int page = 1;
        int totalPages = 1;
        int processed = 0;
        int max = 100; // ✅ 저장할 최대 수

        while (page <= totalPages && processed < max) {
            try {
                String baseUrl = "http://openapi.nature.go.kr/openapi/service/rest/PlantService/plntIlstrSearch";
                String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("st", "1")
                        .queryParam("sw", "")
                        .queryParam("dateGbn", "")
                        .queryParam("dateFrom", "")
                        .queryParam("dateTo", "")
                        .queryParam("numOfRows", "100")
                        .queryParam("pageNo", page)
                        .build(false)
                        .toUriString();

                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
                JSONObject json = new JSONObject(response.getBody());
                JSONObject body = json.getJSONObject("response").getJSONObject("body");

                totalPages = (int) Math.ceil(body.getInt("totalCount") / 100.0);
                Object items = body.optJSONObject("items").opt("item");

                if (items instanceof JSONObject singleItem) {
                    if (processItem(singleItem)) processed++;
                } else if (items instanceof JSONArray itemArray) {
                    for (int i = 0; i < itemArray.length(); i++) {
                        if (processed >= max) break;
                        JSONObject item = itemArray.getJSONObject(i);
                        if (processItem(item)) processed++;
                    }
                }

                log.info("✅ {}페이지 처리 완료 (누적 저장 {}개)", page, processed);
                page++;

            } catch (Exception e) {
                log.error("❌ {}페이지 처리 중 오류", page, e);
                page++;
            }
        }
    }

    private boolean processItem(JSONObject item) {
        String pilbkNo = item.optString("plantPilbkNo");
        String detailYn = item.optString("detailYn");

        if (!"Y".equals(detailYn)) {
            log.info("⏭️ 상세정보 없음 → 스킵 (plantPilbkNo = {})", pilbkNo);
            return false;
        }

        try {
            String responseJson = searchOne(pilbkNo);
            savePlantFromSearchOneResponse(responseJson);
            Thread.sleep(300); // 요청 속도 제한
            return true;
        } catch (Exception e) {
            log.error("❌ 상세조회 실패 (plantPilbkNo = {})", pilbkNo, e);
            return false;
        }
    }

}
