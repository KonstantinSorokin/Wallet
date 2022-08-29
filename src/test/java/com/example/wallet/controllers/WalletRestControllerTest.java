package com.example.wallet.controllers;

import com.example.wallet.entities.Transaction;
import com.example.wallet.exceptions.EmptyPlayerNameException;
import com.example.wallet.exceptions.NegativeAmountException;
import com.example.wallet.services.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WalletRestControllerTest {

    private final WalletService walletServiceMock = mock(WalletService.class);

    private final WalletRestController walletRestController = new WalletRestController(walletServiceMock);

    private static final String PLAYER_NAME = "test";
    private static final long TRANSACTION_ID = 1;
    private static final long TRANSACTION_1_AMOUNT = 10;
    private final Transaction TRANSACTION_1 = new Transaction(TRANSACTION_ID, TRANSACTION_1_AMOUNT, null);

    private void whenPlayerDefined() {
        when(walletServiceMock.getBalance(PLAYER_NAME)).thenReturn(TRANSACTION_1_AMOUNT);
        when(walletServiceMock.getHistory(PLAYER_NAME)).thenReturn(Arrays.asList(TRANSACTION_1));
    }

    @Test
    void testBalanceValidatesPlayerName() {
        assertDoesNotThrow(
                () -> walletRestController.balance(PLAYER_NAME)
        );
        assertThrows(
                EmptyPlayerNameException.class,
                () -> walletRestController.balance(null)
        );
        assertThrows(
                EmptyPlayerNameException.class,
                () -> walletRestController.balance("")
        );
    }

    @Test
    void testBalance() {
        whenPlayerDefined();
        long balanceReceived = walletRestController.balance(PLAYER_NAME);
        verify(walletServiceMock, times(1)).getBalance(PLAYER_NAME);
        assertEquals(TRANSACTION_1_AMOUNT, balanceReceived);
    }

    @Test
    void testHistoryValidatesPlayerName() {
        assertDoesNotThrow(
                () -> walletRestController.history(PLAYER_NAME)
        );
        assertThrows(
                EmptyPlayerNameException.class,
                () -> walletRestController.history(null)
        );
        assertThrows(
                EmptyPlayerNameException.class,
                () -> walletRestController.history("")
        );
    }

    @Test
    void testHistory() {
        whenPlayerDefined();
        Collection<Transaction> historyReceived = walletRestController.history(PLAYER_NAME);
        verify(walletServiceMock, times(1)).getHistory(PLAYER_NAME);
        assertEquals(1, historyReceived.size());
        assertTrue(historyReceived.contains(TRANSACTION_1));
    }

    @Test
    void testDebitValidatesPlayerName() {
        assertDoesNotThrow(
                () -> walletRestController.debit(PLAYER_NAME, TRANSACTION_1_AMOUNT, TRANSACTION_ID)
        );
        assertThrows(
                EmptyPlayerNameException.class,
                () -> walletRestController.debit(null, TRANSACTION_1_AMOUNT, TRANSACTION_ID)
        );
        assertThrows(
                EmptyPlayerNameException.class,
                () -> walletRestController.debit("", TRANSACTION_1_AMOUNT, TRANSACTION_ID)
        );
    }

    @Test
    void testDebitValidatesAmount() {
        assertDoesNotThrow(
                () -> walletRestController.debit(PLAYER_NAME, TRANSACTION_1_AMOUNT, TRANSACTION_ID)
        );
        assertThrows(
                NegativeAmountException.class,
                () -> walletRestController.debit(PLAYER_NAME, -1, TRANSACTION_ID)
        );
    }

    @Test
    void testDebitCallsService() {
        walletRestController.debit(PLAYER_NAME, TRANSACTION_1_AMOUNT, TRANSACTION_ID);
        verify(walletServiceMock, times(1))
                .debit(PLAYER_NAME, TRANSACTION_1_AMOUNT, TRANSACTION_ID);
    }

    @Test
    void testCreditValidatesPlayerName() {
        assertDoesNotThrow(
                () -> walletRestController.credit(PLAYER_NAME, TRANSACTION_1_AMOUNT, TRANSACTION_ID)
        );
        assertThrows(
                EmptyPlayerNameException.class,
                () -> walletRestController.credit(null, TRANSACTION_1_AMOUNT, TRANSACTION_ID)
        );
        assertThrows(
                EmptyPlayerNameException.class,
                () -> walletRestController.credit("", TRANSACTION_1_AMOUNT, TRANSACTION_ID)
        );
    }

    @Test
    void testCreditValidatesAmount() {
        assertDoesNotThrow(
                () -> walletRestController.credit(PLAYER_NAME, TRANSACTION_1_AMOUNT, TRANSACTION_ID)
        );
        assertThrows(
                NegativeAmountException.class,
                () -> walletRestController.credit(PLAYER_NAME, -1, TRANSACTION_ID)
        );
    }

    @Test
    void testCreditCallsService() {
        walletRestController.credit(PLAYER_NAME, TRANSACTION_1_AMOUNT, TRANSACTION_ID);
        verify(walletServiceMock, times(1))
                .credit(PLAYER_NAME, TRANSACTION_1_AMOUNT, TRANSACTION_ID);
    }

}
