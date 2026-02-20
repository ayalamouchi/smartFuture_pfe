package tn.smartfuture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Trainer extends User {
    private String firstName;
    private String lastName;
    private String bio;
    private String cvFileUrl;
    private String cvCNFCPPFileUrl;
    private Float averageRating;
    private Integer totalSessionsAnimated;
    private Boolean isValidated;
}