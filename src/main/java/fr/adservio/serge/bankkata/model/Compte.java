package fr.adservio.serge.bankkata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Compte implements Serializable {

    @Id
    private String id;

    @NotNull
    private String nom;

    @NotNull
    private String clientId;

    private double solde;

    @Builder.Default
    private boolean autoriseSoldeNegatif = true;


}
