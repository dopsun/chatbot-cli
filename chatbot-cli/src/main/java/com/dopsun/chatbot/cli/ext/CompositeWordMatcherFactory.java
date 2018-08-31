/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

import com.dopsun.chatbot.cli.MatcherCost;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class CompositeWordMatcherFactory implements WordMatcherFactory {
    /**
     * @return
     */
    public static CompositeWordMatcherFactory createDefault() {
        CompositeWordMatcherFactory factory = new CompositeWordMatcherFactory();

        factory.add(FullWordMatcher.newFactory());
        factory.add(PrefixWordMatcher.newFactory());

        return factory;
    }

    private final List<WordMatcherFactory> factoryList = new ArrayList<>();

    /**
     * @param factory
     */
    public void add(WordMatcherFactory factory) {
        Objects.requireNonNull(factory);

        this.factoryList.add(factory);
    }

    @Override
    public WordMatcher compile(String template) {
        Objects.requireNonNull(template);

        List<WordMatcher> wordMatcherList = new ArrayList<>();
        for (WordMatcherFactory factory : factoryList) {
            wordMatcherList.add(factory.compile(template));
        }

        return new CompsiteWordMatcher(wordMatcherList);
    }

    static class CompsiteWordMatcher implements WordMatcher {
        private final List<WordMatcher> wordMatchers;

        private CompsiteWordMatcher(List<WordMatcher> wordMatchers) {
            Objects.requireNonNull(wordMatchers);

            this.wordMatchers = wordMatchers;
        }

        @Override
        public OptionalInt match(MatcherCost matcherCost, String input) {
            Objects.requireNonNull(matcherCost);
            Objects.requireNonNull(input);

            int lowestRank = Integer.MAX_VALUE;
            boolean matched = false;

            for (WordMatcher wordMatcher : wordMatchers) {
                OptionalInt optDiscount = wordMatcher.match(matcherCost, input);
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

}
