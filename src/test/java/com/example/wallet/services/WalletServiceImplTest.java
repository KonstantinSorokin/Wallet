package com.example.wallet.services;

import com.example.wallet.entities.Player;
import com.example.wallet.entities.Transaction;
import com.example.wallet.exceptions.InsufficientFundsException;
import com.example.wallet.exceptions.TransactionIdIsNotUniqueException;
import com.example.wallet.repositories.PlayerRepository;
import com.example.wallet.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class WalletServiceImplTest {

    private final PlayerRepository playerRepositoryMock = mock(PlayerRepository.class);
    private final TransactionRepository transactionRepositoryMock = mock(TransactionRepository.class);
    private final Player playerMock = mock(Player.class);

    private static final long EXISTING_TRANSACTION_ID = 1L;
    private static final String PLAYER_NAME = "test";
    private static final long TRANSACTION_1_AMOUNT = 10;
    private final Transaction TRANSACTION_1 = new Transaction(EXISTING_TRANSACTION_ID, TRANSACTION_1_AMOUNT, playerMock);
    private static final long NON_EXISTING_TRANSACTION_ID = 2L;



    private final WalletServiceImpl walletService =
            new WalletServiceImpl(playerRepositoryMock, transactionRepositoryMock);


    private void whenUserExists() {
        when(playerRepositoryMock.getPlayerByName(PLAYER_NAME)).thenReturn(Optional.of(playerMock));
        when(playerMock.getBalance()).thenReturn(TRANSACTION_1_AMOUNT);
    }

    private void whenUserDoesNotExist() {
        when(playerRepositoryMock.getPlayerByName(PLAYER_NAME)).thenReturn(Optional.empty());
    }

    private void whenUserHasTransaction1() {
        when(transactionRepositoryMock.findByPlayer(playerMock)).thenReturn(Arrays.asList(TRANSACTION_1));
        when(transactionRepositoryMock.findById(EXISTING_TRANSACTION_ID)).thenReturn(Arrays.asList(TRANSACTION_1));
        when(transactionRepositoryMock.findById(NON_EXISTING_TRANSACTION_ID)).thenReturn(Collections.emptyList());
    }

    @Test
    void testGetBalanceRequestsBalanceFromPlayer() {
        whenUserExists();
        long balanceReceived = walletService.getBalance(PLAYER_NAME);
        verify(playerRepositoryMock, times(1)).getPlayerByName(PLAYER_NAME);
        verify(playerMock, times(1)).getBalance();
        assertEquals(balanceReceived, TRANSACTION_1_AMOUNT);
    }

    @Test
    void testGetBalanceCreateNonExistingPlayer() {
        whenUserDoesNotExist();
        long balanceReceived = walletService.getBalance(PLAYER_NAME);
        verify(playerRepositoryMock, times(1)).getPlayerByName(PLAYER_NAME);
        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepositoryMock, times(1)).save(playerArgumentCaptor.capture());
        assertEquals(PLAYER_NAME, playerArgumentCaptor.getValue().getName());
        assertEquals(Player.INITIAL_BALANCE, balanceReceived);
    }

    @Test
    void testGetHistory() {
        whenUserExists();
        whenUserHasTransaction1();
        Collection<Transaction> historyReceived = walletService.getHistory(PLAYER_NAME);
        verify(playerRepositoryMock, times(1)).getPlayerByName(PLAYER_NAME);
        verify(transactionRepositoryMock, times(1)).findByPlayer(playerMock);
        assertEquals(1, historyReceived.size());
        assertTrue(historyReceived.contains(TRANSACTION_1));
    }

    @Test
    void testDebitAssertsThatTransactionIdIsUnique() {
        whenUserExists();
        whenUserHasTransaction1();
        assertDoesNotThrow(
                () -> walletService.debit(PLAYER_NAME, TRANSACTION_1_AMOUNT, NON_EXISTING_TRANSACTION_ID)
        );
        assertThrows(
                TransactionIdIsNotUniqueException.class,
                () -> walletService.debit(PLAYER_NAME, TRANSACTION_1_AMOUNT, EXISTING_TRANSACTION_ID)
        );
    }

    @Test
    void testDebitDecreasesPlayersBalanceAndCreatesTransactionWithNegativeAmount() {
        whenUserExists();
        walletService.debit(PLAYER_NAME, TRANSACTION_1_AMOUNT, NON_EXISTING_TRANSACTION_ID);
        verify(playerMock, times(1)).setBalance(TRANSACTION_1_AMOUNT - TRANSACTION_1_AMOUNT);
        verify(playerRepositoryMock, times(1)).save(playerMock);
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepositoryMock, times(1)).save(transactionArgumentCaptor.capture());
        assertEquals(TRANSACTION_1_AMOUNT * -1, transactionArgumentCaptor.getValue().getAmount());
    }

    @Test
    void testDebitAssertsThatFundsAreSufficient() {
        whenUserExists();
        assertThrows(
                InsufficientFundsException.class,
                () -> walletService.debit(PLAYER_NAME, TRANSACTION_1_AMOUNT + 1, NON_EXISTING_TRANSACTION_ID)
        );
    }


    @Test
    void testCreditAssertsThatTransactionIdIsUnique() {
        whenUserExists();
        whenUserHasTransaction1();
        assertDoesNotThrow(
                () -> walletService.credit(PLAYER_NAME, TRANSACTION_1_AMOUNT, NON_EXISTING_TRANSACTION_ID)
        );
        assertThrows(
                TransactionIdIsNotUniqueException.class,
                () -> walletService.credit(PLAYER_NAME, TRANSACTION_1_AMOUNT, EXISTING_TRANSACTION_ID)
        );
    }

    @Test
    void testCreditIncreasesPlayersBalanceAndCreatesTransactionWithPositiveAmount() {
        whenUserExists();
        walletService.credit(PLAYER_NAME, TRANSACTION_1_AMOUNT, NON_EXISTING_TRANSACTION_ID);
        verify(playerMock, times(1)).setBalance(TRANSACTION_1_AMOUNT + TRANSACTION_1_AMOUNT);
        verify(playerRepositoryMock, times(1)).save(playerMock);
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepositoryMock, times(1)).save(transactionArgumentCaptor.capture());
        assertEquals(TRANSACTION_1_AMOUNT, transactionArgumentCaptor.getValue().getAmount());
    }

}
