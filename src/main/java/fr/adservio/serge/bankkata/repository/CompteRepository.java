package fr.adservio.serge.bankkata.repository;


import fr.adservio.serge.bankkata.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompteRepository extends JpaRepository<Compte, String> {

    List<Compte> findAccountsByClientId(String clientId);

}
