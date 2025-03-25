package org.example.mjuteam4.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final StorageManager storageManager;

    // 단일 파일 업로드
    public String uploadFile(MultipartFile multipartFile, Long memberId) {

        // 메타 데이터 생성
        System.out.println(multipartFile.getOriginalFilename());
        String filename = storageManager.generateDiseaseImageFileName(multipartFile.getOriginalFilename(), memberId);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        // s3에 이미지 전송 후
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, filename, multipartFile.getInputStream(), objectMetadata));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // S3 이미지 URL 주소 리턴
        return getS3ImageUrl(filename);
    }

    // S3에 저장된 이미지 주소 가져오기
    public String getS3ImageUrl(String filename) {
        return amazonS3.getUrl(bucket, filename).toString();
    }

}
