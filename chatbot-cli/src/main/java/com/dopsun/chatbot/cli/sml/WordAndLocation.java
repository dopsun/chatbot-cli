/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.Objects;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class WordAndLocation {
    private final String word;
    private final StartAndLength location;

    /**
     * @param word
     * @param location
     */
    public WordAndLocation(String word, StartAndLength location) {
        Objects.requireNonNull(word);
        Objects.requireNonNull(location);

        if (location.isNotFound()) {
            throw new IllegalArgumentException("Invalid location as it's NOT_FOUND.");
        }

        this.word = word.toLowerCase();
        this.location = location;
    }

    /**
     * @return
     */
    public String word() {
        return word;
    }

    /**
     * @return
     */
    public StartAndLength location() {
        return location;
    }
}
