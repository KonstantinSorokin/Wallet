package com.example.wallet.services;

import com.example.wallet.entities.Player;
import com.example.wallet.entities.Transaction;
import com.example.wallet.exceptions.InsufficientFundsException;
import com.example.wallet.exceptions.TransactionIdIsNotUniqueException;
import com.example.wallet.repositories.PlayerRepository;
import com.example.wallet.repositories.TransactionRepository;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of Wallet Service.
 * Defines all the operations that Wallet can perform as a service
 */
@Service
public class WalletServiceImpl implements WalletService {

    private final Logger logger = LogManager.getLogger(WalletServiceImpl.class);
    PlayerRepository playerRepository;
    TransactionRepository transactionRepository;

    @Autowired
    public WalletServiceImpl(PlayerRepository playerRepository, TransactionRepository transactionRepository) {
        this.playerRepository = playerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public long getBalance(String playerName) {
        logger.debug("getBalance: player=" + playerName);
        return getPlayer(playerName).getBalance();
    }

    @Override
    public Collection<Transaction> getHistory(String playerName) {
        logger.debug("getHistory: player=" + playerName);
        Player player = getPlayer(playerName);
        Collection<Transaction> result = transactionRepository.findByPlayer(player);
        if (result == null) result = new ArrayList<>();
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void debit(String playerName, long amount, long transactionId) {
        logger.debug("debit: player=" + playerName + ", amount=" + amount +", transactionId=" + transactionId);
        updateBalance(playerName, amount * -1, transactionId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void credit(String playerName, long amount, long transactionId) {
        logger.debug("credit: player=" + playerName + ", amount=" + amount +", transactionId=" + transactionId);
        updateBalance(playerName, amount, transactionId);
    }

    private @NonNull Player getPlayer(String playerName) {
        Player player = playerRepository.getPlayerByName(playerName).orElse(null);
        if (player == null) {
            player = new Player(playerName);
            playerRepository.save(player);
            logger.info("Created new player: " + playerName);
        }
        return player;
    }

    private void assertTransactionIdIsUnique(long transactionId) {
        Collection<Transaction> foundTransactions = transactionRepository.findById(transactionId);
        if (foundTransactions != null && !foundTransactions.isEmpty()) {
            logger.warn("TransactionID is not unique: " + transactionId);
            throw new TransactionIdIsNotUniqueException("TransactionID is not unique");
        }
    }

    private void assertFundsAreSufficient(long amount, Player player) {
        long newBalance = player.getBalance() + amount;
        if (amount < 0 && newBalance < 0) {
            logger.warn("Insufficient balance: player=" + player.getName() + ", amount=" + amount);
            throw new InsufficientFundsException("Insufficient balance");
        }
    }

    private void updateBalance(String playerName, long amount, long transactionId) {
        assertTransactionIdIsUnique(transactionId);
        Player player = getPlayer(playerName);
        assertFundsAreSufficient(amount, player);
        player.setBalance(player.getBalance() + amount);
        Transaction transaction = new Transaction(transactionId, amount, player);
        playerRepository.save(player);
        transactionRepository.save(transaction);
    }

}
