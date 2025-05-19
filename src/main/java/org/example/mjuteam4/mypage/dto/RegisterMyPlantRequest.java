package org.example.mjuteam4.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class RegisterMyPlantRequest {

    private String name;

    private String description;

    private Long plantId;

    private boolean recommendTonic;

    private MultipartFile image;
}
