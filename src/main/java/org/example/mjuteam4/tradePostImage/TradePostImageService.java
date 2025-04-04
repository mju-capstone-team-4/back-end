package org.example.mjuteam4.tradePostImage;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.storage.StorageService;
import org.example.mjuteam4.tradePostImage.entity.TradePostImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TradePostImageService {
    private final StorageService storageService;

    public TradePostImage createTradePostImage(MultipartFile multipartFile){
        String s3ImageUrl = storageService.uploadFile(multipartFile, "tradePostImage", 5L);
        TradePostImage tradePostImage = TradePostImage.createTradePostImage(s3ImageUrl);
        return tradePostImage;
    }

    public void deleteImageService(String originalImageUrl){
        storageService.deleteFile(originalImageUrl);
    }
}
