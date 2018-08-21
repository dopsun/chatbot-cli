/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.List;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface CliCommand {
    /**
     * @return
     */
    String name();

    /**
     * @return
     */
    String template();

    /**
     * @return
     */
    List<CliArgument> arguments();
}
