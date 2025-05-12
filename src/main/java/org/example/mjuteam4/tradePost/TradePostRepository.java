package org.example.mjuteam4.tradePost;

import org.example.mjuteam4.tradePost.entity.TradePost;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradePostRepository extends JpaRepository<TradePost, Long> {
    Page<TradePost> findByMemberId(Long memberId);
}
