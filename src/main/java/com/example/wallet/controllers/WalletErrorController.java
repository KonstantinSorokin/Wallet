package com.example.wallet.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


/**
 * Error controller.  Handles exceptions in the application.
 */
@Controller
public class WalletErrorController implements ErrorController {

    private final Logger logger = LogManager.getLogger(WalletErrorController.class);

    @RequestMapping(value="/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Throwable exception = (Throwable) request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
        if (exception != null) {
            logger.error("Handled exception: ", exception);
        }
        return "{'Status': " + status + ", 'errorMessage': '" + errorMessage + "'}";
    }

 }

