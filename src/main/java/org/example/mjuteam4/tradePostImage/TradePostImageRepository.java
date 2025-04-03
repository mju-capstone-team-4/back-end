package org.example.mjuteam4.tradePostImage;

import org.example.mjuteam4.tradePostImage.entity.TradePostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradePostImageRepository extends JpaRepository<TradePostImage, Long> {
}
