package fr.adservio.serge.bankkata.repository;



import fr.adservio.serge.bankkata.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {

    List<Operation> findOperationsByAccountIdAndDateBetweenOrderByDateDesc(String accountId,
                                                                           Instant start,
                                                                           Instant end);
}
