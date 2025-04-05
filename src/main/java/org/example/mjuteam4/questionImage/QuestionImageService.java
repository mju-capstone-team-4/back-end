package org.example.mjuteam4.questionImage;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.questionImage.entity.QuestionImage;
import org.example.mjuteam4.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionImageService {
    private final StorageService storageService;
    public QuestionImage createQuestionImage(MultipartFile multipartFile){
        String s3ImageUrl = storageService.uploadFile(multipartFile, "questionImage",2L);
        QuestionImage questionImage = QuestionImage.createQuestionImage(s3ImageUrl);
        return questionImage;
    }

    public void deleteS3Image(String originalImageUrl){

        storageService.deleteFile(originalImageUrl);
    }
}
