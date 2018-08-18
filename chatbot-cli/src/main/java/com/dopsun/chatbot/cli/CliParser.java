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
     * @param commandText
     * @return
     * @throws CliParserException
     */
    Optional<CliParseResult> tryParse(String commandText) throws CliParserException;
}
