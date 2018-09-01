/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class WordAndLocation {
    /**
     * @param sentence
     * @return
     */
    public static List<WordAndLocation> splitSentence(String sentence) {
        Objects.requireNonNull(sentence);

        List<WordAndLocation> list = new ArrayList<>();

        String temp = sentence.toLowerCase();
        int wordStart = -1;
        for (int index = 0; index < temp.length(); index++) {
            char c = temp.charAt(index);
            if (wordStart < 0) {
                if (Character.isWhitespace(c)) {
                    continue;
                }

                wordStart = index;
            } else {
                if (Character.isWhitespace(c)) {
                    StartAndLength location = new StartAndLength(wordStart, index - wordStart);
                    String word = temp.substring(wordStart, index);
                    list.add(new WordAndLocation(word, location));

                    wordStart = -1;
                }
            }
        }

        if (wordStart >= 0) {
            StartAndLength location = new StartAndLength(wordStart, temp.length() - wordStart);
            String word = temp.substring(wordStart);
            list.add(new WordAndLocation(word, location));
        }

        return list;
    }

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
