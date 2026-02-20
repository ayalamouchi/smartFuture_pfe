package tn.smartfuture.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.TrainerEntity;

@Repository
public interface JpaTrainerRepository extends JpaRepository<TrainerEntity, Long> {
}