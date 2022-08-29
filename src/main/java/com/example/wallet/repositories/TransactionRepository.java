package com.example.wallet.repositories;

import com.example.wallet.entities.Player;
import com.example.wallet.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Repository for Transaction entity
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Collection<Transaction> findByPlayer (Player player);

    Collection<Transaction> findById (long id);

}

