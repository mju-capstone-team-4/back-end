package org.example.mjuteam4.plant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.plant.dto.PlantCollectionResponse;
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
    public ResponseEntity<String> search(@RequestParam String keyword) {
        log.info("search 들어옴");
        return ResponseEntity.ok().body(plantService.search(keyword));
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

}
