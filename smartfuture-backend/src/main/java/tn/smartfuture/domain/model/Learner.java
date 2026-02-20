package tn.smartfuture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tn.smartfuture.domain.enums.LearnerLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Learner extends User {
    private String firstName;
    private String lastName;
    private String company;
    private String ville;
    private LearnerLevel level;
    private Integer profileCompletionRate;
}