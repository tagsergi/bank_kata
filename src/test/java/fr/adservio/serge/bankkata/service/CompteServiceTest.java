package fr.adservio.serge.bankkata.service;


import fr.adservio.serge.bankkata.BankkataApplication;
import fr.adservio.serge.bankkata.model.Client;
import fr.adservio.serge.bankkata.model.Compte;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = BankkataApplication.class)
public class CompteServiceTest {

    private static final String CLIENT_ID = UUID.randomUUID().toString();


    @Autowired
    private ClientService clientService;

    @Autowired
    private CompteService accountService;

    @Before
    public void setup() {

        Client
                client =
                Client.builder().id(CLIENT_ID).prenoms("Serge Gildas").nom("AYEPI").build();
        clientService.createClient(client);
    }


    @Test
    public void testCreateAndFind() {
        // Given
        Compte account1 = Compte.builder().clientId(CLIENT_ID).nom("Compte courant").build();
        Compte account2 = Compte.builder().clientId(CLIENT_ID).nom("Livret A").build();

        // When
        Compte accountCreated1 = accountService.createAccount(account1);
        Compte accountCreated2 = accountService.createAccount(account2);
        List<Compte> accounts = accountService.findAccountsByClient(CLIENT_ID);

        // Then
        assertCompte(accountCreated1, account1);
        assertCompte(accountCreated2, account2);
        assertThat(accounts).containsExactlyInAnyOrder(accountCreated1, accountCreated2);

    }

    private void assertCompte(Compte actual, Compte expected) {
        assertThat(actual.getNom()).isEqualTo(expected.getNom());
        assertThat(actual.getClientId()).isEqualTo(expected.getClientId());
        assertThat(actual.getSolde()).isEqualTo(expected.getSolde());
        assertThat(actual.isAutoriseSoldeNegatif()).isEqualTo(expected.isAutoriseSoldeNegatif());
    }

}
