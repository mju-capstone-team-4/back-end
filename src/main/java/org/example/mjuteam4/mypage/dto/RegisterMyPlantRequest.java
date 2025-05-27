package org.example.mjuteam4.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@Setter
public class RegisterMyPlantRequest {

    private String name;

    private String description;

    private String plantPilbkNo;

    private boolean recommendTonic;

    private MultipartFile image;
}
