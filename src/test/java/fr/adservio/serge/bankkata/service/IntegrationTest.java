package fr.adservio.serge.bankkata.service;

import fr.adservio.serge.bankkata.BankkataApplication;
import fr.adservio.serge.bankkata.exception.UnauthorizedOperationException;
import fr.adservio.serge.bankkata.model.Client;
import fr.adservio.serge.bankkata.model.Compte;
import fr.adservio.serge.bankkata.model.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = BankkataApplication.class)
public class IntegrationTest {

    private static final String CLIENT_ID = UUID.randomUUID().toString();
    private static final String ACCOUNT_ID = UUID.randomUUID().toString();

    @Autowired
    private OperationService operationService;

    @Autowired
    private CompteService compteService;

    @Autowired
    private ClientService clientService;

    @BeforeEach
    public void before() {

        Client
                client =
                Client.builder().id(CLIENT_ID).prenoms("Noah").nom("AYEPI").build();

        Compte
                compte =
                Compte.builder().id(ACCOUNT_ID).nom("Compte courant").clientId(CLIENT_ID).build();

        compteService.createAccount(compte);
        clientService.createClient(client);
    }

    @Test
    public void testDeposit(){
        Operation
                operation =
                Operation.builder().accountId(ACCOUNT_ID).libelle("Achat Nespresso").montant(30).build();

        operationService.saveOperation(operation);
        List<Operation>
                operations =
                operationService.findOperations(ACCOUNT_ID,
                        Instant.now().minus(1, ChronoUnit.DAYS),
                        Instant.now().plus(1, ChronoUnit.DAYS));

        Compte account = compteService.findById(ACCOUNT_ID);
        assertThat(account.getSolde()).isEqualTo(operation.getMontant());
        assertThat(operations).containsOnly(operation);
        resetOperationTableAfterTesting();
    }

    @Test
    public void testWithdrawalOperation() {
        // Given
        Operation
                operation =
                Operation.builder().accountId(ACCOUNT_ID).libelle("Sephora buying").montant(-230)
                        .build();

        // When
        operationService.saveOperation(operation);
        List<Operation>
                operations =
                operationService.findOperations(ACCOUNT_ID,
                        Instant.now().minus(1, ChronoUnit.DAYS),
                        Instant.now().plus(1, ChronoUnit.DAYS));

        // Then
        Compte account = compteService.findById(ACCOUNT_ID);
        assertThat(account.getSolde()).isEqualTo(operation.getMontant());
        assertThat(operations).containsOnly(operation);
        resetOperationTableAfterTesting();
    }

    @Test
    public void testWithdrawalOperation_withUnallowedNegativeAmount() {
        // Given
        Compte
                compte =
                Compte.builder().id(ACCOUNT_ID).nom("Epargne retraite").autoriseSoldeNegatif(false)
                        .solde(42).clientId(CLIENT_ID).build();
        compteService.createAccount(compte);

        Operation
                operation =
                Operation.builder().accountId(ACCOUNT_ID).libelle("Sosh ").montant(-500)
                        .build();

        // When / Then
        assertThatThrownBy(() -> operationService.saveOperation(operation))
                .isInstanceOf(UnauthorizedOperationException.class).hasMessageContaining(ACCOUNT_ID);

        List<Operation>
                operations =
                operationService.findOperations(ACCOUNT_ID,
                        Instant.now().minus(1, ChronoUnit.DAYS),
                        Instant.now().plus(1, ChronoUnit.DAYS));

        assertThat(operations).isEmpty();
        resetOperationTableAfterTesting();
    }

    @Test
    public void testFindOperations() {
        // Given
        Operation
                todayOperation =
                Operation.builder().accountId(ACCOUNT_ID).libelle("Sosh").montant(-350)
                        .build();
        Operation
                yesterdayOperation =
                Operation.builder().accountId(ACCOUNT_ID).date(Instant.now().minus(1, ChronoUnit.DAYS))
                        .libelle("Timberland ").montant(340).build();
        Operation
                lastWeekOperation =
                Operation.builder().accountId(ACCOUNT_ID).date(Instant.now().minus(7, ChronoUnit.DAYS))
                        .libelle("Apple 4 Temps la Defense").montant(10).build();

        operationService.saveOperation(todayOperation);
        operationService.saveOperation(yesterdayOperation);
        operationService.saveOperation(lastWeekOperation);

        // When
        List<Operation>
                lastTwoDaysOperations =
                operationService
                        .findOperations(ACCOUNT_ID, Instant.now().minus(2, ChronoUnit.DAYS), Instant.now());

        // Then
        assertThat(lastTwoDaysOperations).containsExactly(todayOperation, yesterdayOperation);
        resetOperationTableAfterTesting();
    }

    private void resetOperationTableAfterTesting(){
        operationService.resetOperations();
    }
}
