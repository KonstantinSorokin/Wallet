package com.example.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when the provided player name is empty
 */
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "The player name should not be empty")
public class EmptyPlayerNameException extends WalletException {

    public EmptyPlayerNameException(String message) {
        super(message);
    }

}
