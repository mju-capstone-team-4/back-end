package org.example.mjuteam4.mypage.repository;

import org.example.mjuteam4.mypage.entity.MyPlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyPlantRepository extends JpaRepository<MyPlant, Long> {

    Optional<MyPlant> findById(Long myPlantId);
}
