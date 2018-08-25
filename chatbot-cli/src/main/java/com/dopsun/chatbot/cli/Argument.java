/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.Optional;

/**
 * Argument in a {@link Command}.
 * 
 * @author Dop Sun
 * @since 1.0.0
 */
public interface Argument {
    /**
     * @return name of argument.
     */
    String name();

    /**
     * @return {@link Optional#empty()} if value is not defined.
     */
    Optional<String> value();
}
