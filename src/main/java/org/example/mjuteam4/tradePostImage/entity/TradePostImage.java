package org.example.mjuteam4.tradePostImage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.tradePost.entity.TradePost;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class TradePostImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_post_image_id")
    private Long id;

    private String imageUrl;
    private LocalDateTime uploadedAt;

    @JoinColumn(name = "trade_post_id")
    @OneToOne(fetch = FetchType.LAZY)
    private TradePost tradePost;

    // 생성자
    private TradePostImage(String imageUrl) {
        this.uploadedAt = LocalDateTime.now();
        this.imageUrl = imageUrl;
    }

    // 생성 메서드
    public static TradePostImage createTradePostImage(String imageUrl) {
        return new TradePostImage(imageUrl);
    }
}
