package tn.smartfuture.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Administrator extends User {
    // Pas de @AllArgsConstructor car User n'a pas de constructeur all-args utilisable ici
}