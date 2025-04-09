package org.example.mjuteam4.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
