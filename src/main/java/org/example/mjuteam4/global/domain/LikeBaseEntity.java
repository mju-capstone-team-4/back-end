package org.example.mjuteam4.global.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.example.mjuteam4.member.entity.Member;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class LikeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    protected Member member;
    protected LocalDateTime likedAt;
}
