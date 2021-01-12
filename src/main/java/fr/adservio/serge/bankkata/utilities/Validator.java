package fr.adservio.serge.bankkata.utilities;

import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

public class Validator {

    private static final javax.validation.Validator
            VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    private Validator() {
        // Util
    }

    public static <T> T validate(T o) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(o);

        if (CollectionUtils.isNotEmpty(violations)) {
            throw new IllegalArgumentException("Validation failed : " + violations);
        }

        return o;
    }
}
