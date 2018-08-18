/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class CliParserException extends Exception {
    private static final long serialVersionUID = -5527624306689736775L;

    /**
     * 
     */
    public CliParserException() {
        super();
    }

    /**
     * @param message
     * 
     */
    public CliParserException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public CliParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
