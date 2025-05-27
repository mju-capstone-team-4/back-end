package org.example.mjuteam4.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    List<Plant> findByPlantGnrlNmContaining(String plantGnrlNm);

    @Query("select p from Plant p where p.plantPilbkNo= :plantPilbkNo")
    Optional<Plant> findByPlantPilbkNo(String plantPilbkNo);
}
