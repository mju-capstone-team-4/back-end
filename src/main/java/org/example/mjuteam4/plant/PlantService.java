package org.example.mjuteam4.plant;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.plant.dto.SearchPlantByImageRequest;
import org.example.mjuteam4.plant.dto.SearchPlantByImageResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Service
@RequiredArgsConstructor
@Slf4j
public class PlantService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final PlantRepository plantRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Value("${gpt.api-key}")
    private String gptKey;

    @Value("${openapi.data}")
    private String serviceKey;


    public String search(String keyword) {
        String baseUrl = "https://apis.data.go.kr/1400119/PlantResource/plantPilbkSearch";
        String encodedKeyword = UriUtils.encode(keyword, StandardCharsets.UTF_8);

        String uriStr = baseUrl
                + "?serviceKey=%2BQiEewXiCtFc2wzKcuiF5ZXfhmMWDsF4qhJooQgwm7qUCNBAnmSk0RnbjcxocBrLmuEvHTpyjggBzSiYERlvFw%3D%3D"
                + "&reqSearchWrd=" + encodedKeyword
                + "&pageNo=1"
                + "&numOfRows=20";

        // java.net.URI 객체로 고정
        URI uri = URI.create(uriStr);

        // User-Agent 넣기 (필수 아님, 보완용)
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                String.class
        );

        JSONObject json = new JSONObject(response.getBody());
        JSONObject body = json.getJSONObject("response").getJSONObject("body");

        Object itemsRaw = body.get("items");

        if (!(itemsRaw instanceof JSONObject)) {
            log.warn("items가 JSONObject가 아님: {}", itemsRaw);
            return json.toString();  // 검색 결과 없음 또는 오류 메시지 포함
        }

        JSONObject itemsObj = (JSONObject) itemsRaw;

        if (!itemsObj.has("item")) {
            return json.toString(); // 검색 결과 없음
        }

        JSONArray items = itemsObj.getJSONArray("item");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String plantPilbkNo = String.valueOf(item.get("plantPilbkNo"));

            Optional<Plant> plantOpt = plantRepository.findByPlantPilbkNo(plantPilbkNo);
            plantOpt.ifPresent(plant -> item.put("imageUrl", plant.getImgUrl()));
        }

        return json.toString();
    }


    public String searchOne(String reqPlantPilbkNo) {
        String baseUrl = "https://apis.data.go.kr/1400119/PlantResource/plantPilbkInfo";

        String uriStr = baseUrl
                + "?serviceKey=%2BQiEewXiCtFc2wzKcuiF5ZXfhmMWDsF4qhJooQgwm7qUCNBAnmSk0RnbjcxocBrLmuEvHTpyjggBzSiYERlvFw%3D%3D"
                + "&reqPlantPilbkNo=" + reqPlantPilbkNo;

        URI uri = URI.create(uriStr); // URI 객체 고정

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            log.info("status = {}", response.getStatusCode());
            // JSON 파싱
            JSONObject json = new JSONObject(response.getBody());
            JSONObject body = json.getJSONObject("response").getJSONObject("body");

            // item 필드가 JSONObject인지 먼저 확인
            Object itemRaw = body.opt("item");

            if (!(itemRaw instanceof JSONObject)) {
                log.warn("item 필드가 JSONObject가 아님: {}", itemRaw);
                return json.toString();  // 예외 없이 원본 반환
            }

            JSONObject item = (JSONObject) itemRaw;

            // DB에서 imageUrl 조회
            Optional<Plant> plantOpt = plantRepository.findByPlantPilbkNo(reqPlantPilbkNo);
            plantOpt.ifPresent(plant -> item.put("imageUrl", plant.getImgUrl()));

            return json.toString();

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

    @Transactional
    public void fetchAndSaveAllPlants() {
        int page = 6;
        int processed = 0;

        List<Future<?>> futures = new ArrayList<>();

        while (true) { // ✅ 조건을 while (true)로 수정
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

                int totalPages = (int) Math.ceil(body.getInt("totalCount") / 100.0); // ✅ 응답 후에 계산
                if (page > totalPages) break; // ✅ 마지막 페이지 넘으면 종료

                Object items = body.optJSONObject("items").opt("item");

                if (items instanceof JSONObject singleItem) {
                    submitDetailTask(singleItem.optString("plantPilbkNo"), futures);
                    processed++;
                } else if (items instanceof JSONArray itemArray) {
                    for (int i = 0; i < itemArray.length(); i++) {
                        JSONObject item = itemArray.getJSONObject(i);
                        String detailYn = item.optString("detailYn");
                        if (!"Y".equals(detailYn)) continue;

                        submitDetailTask(item.optString("plantPilbkNo"), futures);
                        processed++;
                    }
                }

                log.info("✅ {}페이지 처리 완료 (누적 요청 {}건 / 총 {}건)", page, processed, totalPages * 100);
                page++;

            } catch (Exception e) {
                log.error("❌ {}페이지 처리 중 오류", page, e);
                page++;
            }
        }

        // 병렬 작업 완료 대기
        for (Future<?> future : futures) {
            try {
                future.get(); // 예외 전파
            } catch (Exception e) {
                log.error("❌ 병렬 저장 실패", e);
            }
        }

        executor.shutdown();
    }



    private void submitDetailTask(String plantPilbkNo, List<Future<?>> futures) {
        futures.add(executor.submit(() -> {
            try {
                String json = searchOne(plantPilbkNo);
                savePlantFromSearchOneResponse(json);
                Thread.sleep(200); // 요청 간 간격 약간 유지
            } catch (Exception e) {
                log.error("❌ 상세 저장 실패 (plantPilbkNo = {})", plantPilbkNo, e);
            }
        }));
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

    public SearchPlantByImageResponse searchImage(SearchPlantByImageRequest searchPlantByImageRequest) {

        log.info("file = {}", searchPlantByImageRequest.getFile());

        MultipartFile file = searchPlantByImageRequest.getFile();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 1. MultipartFile → Base64 인코딩
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // 2. 프롬프트 작성
            String prompt = "아래 이미지는 식물입니다. 이 식물의 이름을 알려주세요. " +
                    "아래와 같은 JSON 형태로만 응답해주세요:\n" +
                    "{ \"plantName\": \"식물명\"} " +
                    "마크다운 기호나 설명 없이 순수 JSON으로만 응답하세요.";

            // 3. OpenAI Vision API 호출 요청 구성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(gptKey);

            Map<String, Object> imageMap = Map.of(
                    "type", "image_url",
                    "image_url", Map.of("url", "data:image/jpeg;base64," + base64Image)
            );

            Map<String, Object> textMap = Map.of(
                    "type", "text",
                    "text", prompt
            );

            Map<String, Object> message = Map.of(
                    "role", "user",
                    "content", List.of(textMap, imageMap)
            );

            Map<String, Object> request = Map.of(
                    "model", "gpt-4o",
                    "messages", List.of(message),
                    "max_tokens", 500
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    String.class
            );

            // 4. 응답 처리 및 파싱
            String content = objectMapper.readTree(response.getBody())
                    .get("choices").get(0).get("message").get("content").asText();

            SearchPlantByImageResponse result = objectMapper.readValue(content, SearchPlantByImageResponse.class);
            log.info("식물 이름: {}", result.getPlantName());
            return result;

        } catch (IOException e) {
            throw new RuntimeException("이미지 인코딩 실패", e);
        } catch (Exception e) {
            throw new RuntimeException("OpenAI 응답 파싱 실패", e);
        }
    }
}
