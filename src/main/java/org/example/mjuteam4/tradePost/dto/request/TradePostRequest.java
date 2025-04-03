package org.example.mjuteam4.tradePost.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class TradePostRequest {
    private String itemName;
    private Long price;
    private String description;
    private MultipartFile image;
}
