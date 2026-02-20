package tn.smartfuture.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tn.smartfuture.domain.enums.LearnerLevel;

@Entity
@Table(name = "learners")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class LearnerEntity extends UserEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String company;

    @Column(nullable = false)
    private String ville;

    @Enumerated(EnumType.STRING)
    private LearnerLevel level = LearnerLevel.BEGINNER;

    private Integer profileCompletionRate = 0;
}