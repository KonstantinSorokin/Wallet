package com.example.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when funds are insufficient for debit operation
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Insufficient funds")
public class InsufficientFundsException extends WalletException {

    public InsufficientFundsException(String message) {
        super(message);
    }

}
