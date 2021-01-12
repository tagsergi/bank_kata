package fr.adservio.serge.bankkata.service;


import fr.adservio.serge.bankkata.model.Client;
import fr.adservio.serge.bankkata.repository.ClientRepository;
import org.springframework.stereotype.Service;

import static fr.adservio.serge.bankkata.utilities.Validator.validate;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notEmpty;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = requireNonNull(clientRepository);
    }

    public Client createClient(Client client) {
        return clientRepository.save(validate(client));
    }

    public Client findClient(String id) {
        return clientRepository.findById(notEmpty(id)).orElse(null);
    }

}
