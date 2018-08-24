/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.List;

/**
 * Result for {@link Parser#tryParse(String)}.
 * 
 * @author Dop Sun
 * @since 1.0.0
 */
public interface ParseResult {
    /**
     * This returns the first command in the {@link #allCommands()}. This is the command with
     * highest rank. As {@link #allCommands()} contains at least one command, so this is already
     * available.
     * 
     * @return first command matched.
     */
    default Command command() {
        return allCommands().get(0);
    }

    /**
     * Here lists all commands which possibly matching the input, sorted by ranking. First command
     * is highest rank (lowest rank value).
     * 
     * {@link Parser#tryParse(String)} guarantees at least one command in the ParserResult if result
     * present.
     * 
     * @return list of commands matched.
     */
    List<Command> allCommands();
}
