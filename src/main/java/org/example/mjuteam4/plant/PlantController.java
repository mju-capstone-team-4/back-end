package org.example.mjuteam4.plant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.plant.dto.PlantCollectionResponse;
import org.example.mjuteam4.plant.dto.RegisterPlantReq;
import org.example.mjuteam4.plant.dto.SearchPlantByImageRequest;
import org.example.mjuteam4.plant.dto.SearchPlantByImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plant")
@Slf4j
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/search")
    public ResponseEntity<String> search(
            @RequestParam String keyword,
            @RequestParam String page
    ) {
        log.info("search 들어옴");
        return ResponseEntity.ok().body(plantService.search(keyword, page));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterPlantReq registerPlant){
        plantService.register(registerPlant);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/search/{q1}")
    public ResponseEntity<String> searchOne(@PathVariable String q1) {
        return ResponseEntity.ok().body(plantService.searchOne(q1));
    }

//    @GetMapping("/search/save/{q1}")
//    public ResponseEntity<String> searchSaveOne(@PathVariable String q1) {
//        plantService.fetchAndSaveDetail(q1);
//        return ResponseEntity.ok().body("save completed");
//    }

    @GetMapping("/save-all")
    public String saveAllPlants() {
        plantService.fetchAndSaveAllPlants();
        return "✅ 전체 저장 요청 완료";
    }

    @PostMapping("/search/image")
    public ResponseEntity<SearchPlantByImageResponse> searchImage(@ModelAttribute SearchPlantByImageRequest searchPlantByImageRequest) {
        return ResponseEntity.ok().body(plantService.searchImage(searchPlantByImageRequest));
    }

}
