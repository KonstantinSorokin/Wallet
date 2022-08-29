package com.example.wallet.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class WalletErrorControllerTest {

    private static final Integer ERROR_STATUS_CODE = 1234;
    private static final String ERROR_MESSAGE = "Test error message";
    private HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
    private final WalletErrorController walletErrorController = new WalletErrorController();

    @Test
    void handleErrorReportsStatusAndErrorMessage() {
        when(httpServletRequestMock.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(ERROR_STATUS_CODE);
        when(httpServletRequestMock.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn(ERROR_MESSAGE);

        String result = walletErrorController.handleError(httpServletRequestMock);

        assertNotNull(result);
        assertTrue(result.contains(ERROR_STATUS_CODE.toString()));
        assertTrue(result.contains(ERROR_MESSAGE));
        verify(httpServletRequestMock, times(1))
                .getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
    }

}
