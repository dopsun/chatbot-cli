/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class ParserException extends Exception {
    private static final long serialVersionUID = -5527624306689736775L;

    /**
     * 
     */
    public ParserException() {
        super();
    }

    /**
     * @param message
     * 
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
