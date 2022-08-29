package com.example.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when provided transactionId is not unique
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Transaction id is not unique")
public class TransactionIdIsNotUniqueException extends WalletException {

    public TransactionIdIsNotUniqueException(String message) {
        super(message);
    }
}
