package com.example.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when debit or credit operation is called with negative amount parameter
 */
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "The amount should be positive")
public class NegativeAmountException extends WalletException {

    public NegativeAmountException(String message) {
        super(message);
    }

}
