package tn.smartfuture.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class TrainerEntity extends UserEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(length = 1000)
    private String bio;

    private String cvFileUrl;

    private String cvCNFCPPFileUrl;

    private Float averageRating = 0.0f;

    private Integer totalSessionsAnimated = 0;

    private Boolean isValidated = false;
}