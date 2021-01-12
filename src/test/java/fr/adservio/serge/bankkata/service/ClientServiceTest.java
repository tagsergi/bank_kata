package fr.adservio.serge.bankkata.service;


import fr.adservio.serge.bankkata.BankkataApplication;
import fr.adservio.serge.bankkata.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = BankkataApplication.class)
public class ClientServiceTest  {

    private static final String CLIENT_ID = UUID.randomUUID().toString();


    @Autowired
    private ClientService clientService;


    @Test
    public void testCreateAndFind() {
        // Given
        Client  client = Client.builder().id(CLIENT_ID).prenoms("Serge Gildas").nom("AYEPI").build();

        // When
        Client clientCreated = clientService.createClient(client);
        Client clientFound = clientService.findClient(clientCreated.getId());

        // Then
        assertThat(clientCreated.getId()).isNotEmpty();
        assertThat(clientCreated.getPrenoms()).isEqualTo(client.getPrenoms());
        assertThat(clientCreated.getNom()).isEqualTo(client.getNom());
        assertThat(clientFound).isEqualTo(clientCreated);
    }

}
