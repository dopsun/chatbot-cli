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
     * @param input
     * @param fromIndex
     * @return
     */
    public StartAndLength find(String input, int fromIndex) {
        Objects.requireNonNull(input);

        String inputLower = input.toLowerCase();

        int index = inputLower.indexOf(wordLower, fromIndex);
        if (index < 0) {
            return StartAndLength.NOT_FOUND;
        }

        return new StartAndLength(index, wordLower.length());
    }
}
