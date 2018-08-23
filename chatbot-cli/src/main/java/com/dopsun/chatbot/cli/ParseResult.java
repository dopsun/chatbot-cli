/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.List;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface ParseResult {
    /**
     * @return first command matched.
     */
    default Command command() {
        return allCommands().get(0).command();
    }

    /**
     * Here lists all commands which possibly matching the input, sorted by ranking. First command
     * is highest rank (lowest rank value).
     * 
     * @return list of commands matched.
     */
    List<CommandAndRank> allCommands();
}
