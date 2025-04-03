package org.example.mjuteam4.tradePost.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.tradePost.entity.TradePost;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class TradePostResponse {
    private Long tradePostId;
    private String itemName;
    private Long price;
    private String description;
    private String imageUrl;

    private TradePostResponse(TradePost tradePost){
        this.tradePostId = tradePost.getId();
        this.itemName = tradePost.getItemName();
        this.price = tradePost.getPrice();
        this.description = tradePost.getDescription();
        this.imageUrl = tradePost.getTradePostImage().getImageUrl();
    }

    public static TradePostResponse create(TradePost tradePost){
        return new TradePostResponse(tradePost);
    }
}
