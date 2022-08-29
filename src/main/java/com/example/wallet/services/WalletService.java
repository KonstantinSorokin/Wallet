package com.example.wallet.services;

import com.example.wallet.entities.Transaction;

import java.util.Collection;

/**
 * Interface of Wallet Service.
 * Names all operations that Wallet can perform as a service
 */
public interface WalletService {

    long getBalance(String playerName);

    Collection<Transaction> getHistory(String playerName);

    void debit(String playerName, long amount, long transactionId);

    void credit(String playerName, long amount, long transactionId);

}
