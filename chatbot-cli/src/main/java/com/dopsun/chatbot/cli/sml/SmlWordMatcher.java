/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.Objects;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class SmlWordMatcher {
    private final String wordLower;

    /**
     * @param word
     */
    public SmlWordMatcher(String word) {
        Objects.requireNonNull(word);

        this.wordLower = word.toLowerCase();
    }

    /**
     * @param word
     * @return <code>true</code> if word matches.
     */
    public boolean match(String word) {
        return wordLower.equalsIgnoreCase(word);
    }
}
