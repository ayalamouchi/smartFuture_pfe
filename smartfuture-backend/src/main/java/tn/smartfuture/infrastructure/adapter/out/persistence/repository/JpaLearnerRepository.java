package tn.smartfuture.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.LearnerEntity;

@Repository
public interface JpaLearnerRepository extends JpaRepository<LearnerEntity, Long> {
}