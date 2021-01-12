package fr.adservio.serge.bankkata.exception;

import fr.adservio.serge.bankkata.model.Compte;
import fr.adservio.serge.bankkata.model.Operation;

public class UnauthorizedOperationException extends RuntimeException {

    private static final String MESSAGE = "Unauthorized operation %s on account %s";

    public UnauthorizedOperationException(Compte account, Operation operation) {
        super(String.format(MESSAGE, operation, account.getId()));
    }

}
