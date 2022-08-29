package com.example.wallet.controllers;

import com.example.wallet.entities.Transaction;
import com.example.wallet.exceptions.EmptyPlayerNameException;
import com.example.wallet.exceptions.NegativeAmountException;
import com.example.wallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Main REST controller for Wallet microservice.  Handles all the requests to the service.
 */
@RestController
public class WalletRestController {

    private static final String EMPTY_RESPONSE_OK = "{'result': 'Ok'}";

    private final Logger logger = LogManager.getLogger(WalletRestController.class);
    private final WalletService walletService;

    @Autowired
    public WalletRestController(WalletService walletService) {
        this.walletService = walletService;
    }

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public long balance(@RequestParam String playerName) {
        logger.debug("Balance: player=" + playerName);
        validatePlayerName(playerName);
        return walletService.getBalance(playerName);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public Collection<Transaction> history(@RequestParam String playerName) {
        logger.debug("History: player=" + playerName);
        validatePlayerName(playerName);
        return walletService.getHistory(playerName);
    }

    @RequestMapping(value = "/debit", method = RequestMethod.GET)
    public String debit(@RequestParam String playerName, @RequestParam long amount, @RequestParam long transactionId) {
        logger.debug("Debit: player=" + playerName + ", amount=" + amount +
                ", transactionId=" + transactionId);
        validatePlayerName(playerName);
        validateAmount(amount);
        walletService.debit(playerName, amount, transactionId);
        return EMPTY_RESPONSE_OK;
    }

    @RequestMapping(value = "/credit", method = RequestMethod.GET)
    public String credit(@RequestParam String playerName, @RequestParam long amount, @RequestParam long transactionId) {
        logger.debug("Credit: player=" + playerName + ", amount=" + amount +
                ", transactionId=" + transactionId);
        validatePlayerName(playerName);
        validateAmount(amount);
        walletService.credit(playerName, amount, transactionId);
        return EMPTY_RESPONSE_OK;
    }

    private void validatePlayerName(String playerName) {
        if (playerName == null || playerName.length() == 0)
            throw new EmptyPlayerNameException("The player name should not be empty");
    }

    private void validateAmount(long amount) {
        if (amount < 0)
            throw new NegativeAmountException("The amount should be positive");
    }

}
