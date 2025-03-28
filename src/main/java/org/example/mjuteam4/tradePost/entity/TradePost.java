package org.example.mjuteam4.tradePost.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.mjuteam4.member.entity.Member;
import org.example.mjuteam4.tradePostImage.entity.TradePostImage;

@Data
@Entity
public class TradePost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    private Long price;
    private String description;
    private String itemName;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(mappedBy = "tradePost", orphanRemoval = true, cascade = CascadeType.ALL)
    private TradePostImage tradePostImage;

    // 의존관계 메서드
    public void addTradePostImage(TradePostImage tradePostImage){
        this.tradePostImage = tradePostImage;
    }
}
