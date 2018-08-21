/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.Optional;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface CliParser {
    /**
     * Tries to parse the <code>commandText</code>. Returns {@link Optional#empty()} if not
     * recognized.
     * 
     * @param commandText
     *            command text to parse.
     * @return returns {@link Optional#empty()} if <code>commandText</code> not recognized.
     */
    Optional<CliParseResult> tryParse(String commandText);
}
