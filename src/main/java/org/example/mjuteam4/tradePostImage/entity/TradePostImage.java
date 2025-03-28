package org.example.mjuteam4.tradePostImage.entity;

import jakarta.persistence.*;
import org.example.mjuteam4.member.entity.Member;
import org.example.mjuteam4.tradePost.entity.TradePost;

import java.time.LocalDateTime;

@Entity
public class TradePostImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_post_image_id")
    private Long id;

    private String imageUrl;
    private LocalDateTime uploadedAt;

    @JoinColumn(name = "trade_id")
    @OneToOne(fetch = FetchType.LAZY)
    private TradePost tradePost;
}
