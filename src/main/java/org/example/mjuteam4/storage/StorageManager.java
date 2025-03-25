package org.example.mjuteam4.storage;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StorageManager {

    // 식물 질병 이미지 파일명 생성
    public String generateImageFileName(String filename, String filePath, Long userId) {
        String ext = getFileExtension(filename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        String uuid = UUID.randomUUID().toString();
        return "images/" + filePath + "/" + userId + "-profile-" + timestamp + "-" + uuid + ext;
    }

    // 확장자를 얻는다
    public String getFileExtension(String filename) {
        try {
            return filename.substring(filename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw e;
        }
    }
}
