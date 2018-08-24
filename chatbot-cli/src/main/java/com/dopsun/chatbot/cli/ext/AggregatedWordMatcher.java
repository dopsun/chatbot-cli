/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class AggregatedWordMatcher implements WordMatcher {
    /**
     * @return
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * @author Dop Sun
     * @since 1.0.0
     */
    public static class Builder {
        private final List<WordMatcher> wordMatchers = new ArrayList<>();

        private Builder() {

        }

        /**
         * @return
         */
        public AggregatedWordMatcher build() {
            return new AggregatedWordMatcher(this);
        }

        /**
         * @param wordMatcher
         */
        public void add(WordMatcher wordMatcher) {
            Objects.requireNonNull(wordMatcher);
            wordMatchers.add(wordMatcher);
        }
    }

    private final List<WordMatcher> wordMatchers;

    private AggregatedWordMatcher(Builder builder) {
        Objects.requireNonNull(builder);

        this.wordMatchers = new ArrayList<>(builder.wordMatchers);
    }

    @Override
    public OptionalInt match(String input) {
        int lowestRank = Integer.MAX_VALUE;
        boolean matched = false;

        for (WordMatcher wordMatcher : wordMatchers) {
            OptionalInt optDiscount = wordMatcher.match(input);
            if (optDiscount.isPresent()) {
                matched = true;
                lowestRank = Math.min(lowestRank, optDiscount.getAsInt());
            }
        }

        if (matched) {
            return OptionalInt.of(lowestRank);
        }

        return OptionalInt.empty();
    }

}
