package fr.adservio.serge.bankkata.service;


import fr.adservio.serge.bankkata.exception.UnauthorizedOperationException;
import fr.adservio.serge.bankkata.model.Compte;
import fr.adservio.serge.bankkata.model.Operation;
import fr.adservio.serge.bankkata.repository.CompteRepository;
import fr.adservio.serge.bankkata.repository.OperationRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Currency;
import java.util.List;

import static fr.adservio.serge.bankkata.utilities.Validator.validate;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notEmpty;

@Service
public class OperationService {

    private CompteRepository compteRepository;
    private OperationRepository operationRepository;


    public OperationService(CompteRepository compteRepository, OperationRepository operationRepository) {
        this.compteRepository = requireNonNull(compteRepository);
        this.operationRepository = requireNonNull(operationRepository);
    }

    public List<Operation> findOperations(String accountId,
                                          Instant startOperationDate,
                                          Instant endOperationDate) {

        return operationRepository
            .findOperationsByAccountIdAndDateBetweenOrderByDateDesc(notEmpty(accountId),
                requireNonNull(startOperationDate),
                requireNonNull(endOperationDate));
    }

    public void saveOperation(Operation operation) {
        validate(operation);
        Compte account = getAccount(operation);

        double newPotentialAccountAmount = getNewPotentialAccountAmount(account, operation);

        if (!isOperationAllowed(account, newPotentialAccountAmount)) {
            throw new UnauthorizedOperationException(account, operation);
        }

        account.setSolde(newPotentialAccountAmount);

        compteRepository.save(account);
        operationRepository.save(requireNonNull(operation));
    }

    public void resetOperations(){
        operationRepository.deleteAll();
    }

    private Compte getAccount(Operation operation) {
        return requireNonNull(compteRepository.findById(operation.getAccountId()).orElse(null));
    }

    private double getNewPotentialAccountAmount(Compte account, Operation operation) {

        double accountAmount = account.getSolde();
        double operationAmount = operation.getMontant();
        return accountAmount + operationAmount;
    }

    private boolean isOperationAllowed(Compte account, double newPotentialAccountAmount) {
        return account.isAutoriseSoldeNegatif() || newPotentialAccountAmount >= 0;
    }
}
