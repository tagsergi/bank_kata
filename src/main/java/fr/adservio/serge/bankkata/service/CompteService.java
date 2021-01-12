package fr.adservio.serge.bankkata.service;


import fr.adservio.serge.bankkata.model.Compte;
import fr.adservio.serge.bankkata.repository.CompteRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static fr.adservio.serge.bankkata.utilities.Validator.validate;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notEmpty;

@Service
public class CompteService {

    private CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = requireNonNull(compteRepository);
    }

    public Compte createAccount(Compte compte) {
        return compteRepository.save(validate(compte));
    }

    public List<Compte> findAccountsByClient(String clientId) {
        return compteRepository.findAccountsByClientId(notEmpty(clientId));
    }

    public Compte findById(String id){
        return compteRepository.findById(id).orElse(null);
    }
}
