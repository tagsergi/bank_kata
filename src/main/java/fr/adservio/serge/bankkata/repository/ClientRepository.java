package fr.adservio.serge.bankkata.repository;


import fr.adservio.serge.bankkata.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

}
