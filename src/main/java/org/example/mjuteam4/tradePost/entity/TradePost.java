package org.example.mjuteam4.tradePost.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.member.entity.Member;
import org.example.mjuteam4.question.dto.request.QuestionRequest;
import org.example.mjuteam4.question.entity.Question;
import org.example.mjuteam4.tradePost.dto.request.TradePostRequest;
import org.example.mjuteam4.tradePostImage.entity.TradePostImage;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class TradePost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_post_id")
    private Long id;

    private Long price;
    private String description;
    private String itemName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(mappedBy = "tradePost", orphanRemoval = true, cascade = CascadeType.ALL)
    private TradePostImage tradePostImage;

    // 생성자
    private TradePost(TradePostRequest tradePostRequest){
        this.price = tradePostRequest.getPrice();
        this.description = tradePostRequest.getDescription();
        this.itemName = tradePostRequest.getItemName();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 생성 메서드
    public static TradePost create(TradePostRequest tradePostRequest){
        return new TradePost(tradePostRequest);
    }

    // 수정 메서드
    public TradePost modifyTradePost(TradePostRequest tradePostRequest) {
        String itemName = tradePostRequest.getItemName();
        String description = tradePostRequest.getDescription();
        Long price = tradePostRequest.getPrice();

        if(StringUtils.hasText(itemName)){
            this.itemName = itemName;
        }
        if(StringUtils.hasText(TradePost.this.description)) {
            this.description = description;
        }

        if (price != null && price > 0) {
            this.price = price;
        }

        this.updatedAt = LocalDateTime.now();
        return this;
    }


    // 의존관계 메서드
    public void addTradePostImage(TradePostImage tradePostImage){
        this.tradePostImage = tradePostImage;
        tradePostImage.setTradePost(this);
    }
}
