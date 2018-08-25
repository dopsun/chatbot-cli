/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.Optional;

import com.dopsun.chatbot.cli.sml.SmlCliParserBuilder;

/**
 * A parser parses text into {@link Command}.
 * 
 * @author Dop Sun
 * @since 1.0.0
 */
public interface Parser {
    /**
     * Creates a new {@link ParserBuilder}.
     * 
     * @return a new {@link ParserBuilder}.
     */
    static ParserBuilder newBuilder() {
        return new SmlCliParserBuilder();
    }

    /**
     * Tries to parse the <code>commandText</code>. Returns {@link Optional#empty()} if not
     * recognized. At least one {@link Command} matched if result {@link Optional#isPresent()}.
     * 
     * @param commandText
     *            command text to parse.
     * @return returns {@link Optional#empty()} if <code>commandText</code> not recognized.
     */
    Optional<ParseResult> tryParse(String commandText);
}
