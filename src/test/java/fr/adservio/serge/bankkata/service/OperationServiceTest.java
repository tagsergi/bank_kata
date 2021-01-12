package fr.adservio.serge.bankkata.service;


import fr.adservio.serge.bankkata.BankkataApplication;
import fr.adservio.serge.bankkata.exception.UnauthorizedOperationException;
import fr.adservio.serge.bankkata.model.Compte;
import fr.adservio.serge.bankkata.model.Operation;
import fr.adservio.serge.bankkata.repository.CompteRepository;
import fr.adservio.serge.bankkata.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BankkataApplication.class)
public class OperationServiceTest {


    @MockBean
    private CompteRepository accountRepositoryMock;

    @MockBean
    private OperationRepository operationRepositoryMock;

    @Captor
    private ArgumentCaptor<Compte> accountCaptor;

    @Autowired
    private OperationService operationService;


    @Test
    public void testSaveDepositOperation() {
        // Given
        Compte
            account =
                Compte.builder().id(UUID.randomUUID().toString()).nom("Lvret A").solde(100)
                .build();

        Operation
            operation =
            Operation.builder().accountId(account.getId()).libelle("APL")
                .montant(100).build();

        when(accountRepositoryMock.findById(account.getId())).thenReturn(java.util.Optional.of(account));

        // When
        operationService.saveOperation(operation);

        // Then
        verify(operationRepositoryMock).save(operation);
        verify(accountRepositoryMock).save(accountCaptor.capture());
        Compte accountCaptured = accountCaptor.getValue();
        assertThat(accountCaptured.getId()).isEqualTo(account.getId());
        assertThat(accountCaptured.getSolde()).isEqualTo(200);
    }

    @Test
    public void testSaveWithdrawalOperation() {
        // Given
        Compte
            account =
                Compte.builder().id(UUID.randomUUID().toString()).nom("Compte courant").solde(100)
                .build();

        Operation
            operation =
            Operation.builder().accountId(account.getId()).libelle("SNCF").montant(-200)
                .build();

        when(accountRepositoryMock.findById(account.getId())).thenReturn(java.util.Optional.of(account));

        // When
        operationService.saveOperation(operation);

        // Then
        verify(operationRepositoryMock).save(operation);
        verify(accountRepositoryMock).save(accountCaptor.capture());
        Compte accountCaptured = accountCaptor.getValue();
        assertThat(accountCaptured.getId()).isEqualTo(account.getId());
        assertThat(accountCaptured.getSolde()).isEqualTo(-100);
    }

    @Test
    public void testSaveWithdrawalOperation_withNegativeAmountUnallowed() {
        // Given
        Compte
            account =
                Compte.builder().id(UUID.randomUUID().toString()).autoriseSoldeNegatif(false)
                .nom("Tania").solde(100).build();

        Operation
            operation =
            Operation.builder().accountId(account.getId()).libelle("Amazon France").montant(-200).build();

        when(accountRepositoryMock.findById(account.getId())).thenReturn(java.util.Optional.of(account));

        // Then
        assertThatThrownBy(() -> operationService.saveOperation(operation))
            .isInstanceOf(UnauthorizedOperationException.class);
        verify(operationRepositoryMock, never()).save(operation);
        verify(accountRepositoryMock, never()).save(accountCaptor.capture());
    }

}
