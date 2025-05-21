package org.example.mjuteam4.mypage.repository;

import org.example.mjuteam4.mypage.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);


    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.diseaseList WHERE m.id = :id")
    Optional<Member> findWithDiseasesById(@Param("id") Long id);

}
