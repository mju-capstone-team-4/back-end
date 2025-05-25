package org.example.mjuteam4.disease;

import org.example.mjuteam4.disease.entity.Disease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    Page<Disease> findByMemberIdOrderByCreatedAtDesc(Pageable pageable, Long MemberId);
}
