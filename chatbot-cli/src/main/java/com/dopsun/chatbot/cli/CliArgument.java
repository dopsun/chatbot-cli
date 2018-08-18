/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.Optional;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface CliArgument {
    /**
     * @return
     */
    String name();

    /**
     * @return
     */
    Optional<String> value();
}
