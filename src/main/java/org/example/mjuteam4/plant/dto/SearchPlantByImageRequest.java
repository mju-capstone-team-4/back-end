package org.example.mjuteam4.plant.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class SearchPlantByImageRequest {

    private MultipartFile file;
}
