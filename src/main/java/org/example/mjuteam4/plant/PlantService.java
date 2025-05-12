package org.example.mjuteam4.plant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
