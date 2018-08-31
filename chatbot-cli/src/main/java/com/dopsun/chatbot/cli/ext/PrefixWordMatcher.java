/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.ext;

import java.util.Objects;
import java.util.OptionalInt;

import com.dopsun.chatbot.cli.MatcherCost;
import com.dopsun.chatbot.cli.MatcherCostType;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class PrefixWordMatcher implements WordMatcher {
    /**
     * Rank discount points if word matched.
     */
    public static final int RANK_DISCOUNT_POINT = 5;

    /**
     * @return
     */
    public static final WordMatcherFactory newFactory() {
        return new Factory();
    }

    static class Factory implements WordMatcherFactory {
        @Override
        public PrefixWordMatcher compile(String template) {
            return new PrefixWordMatcher(template);
        }
    }

    private static int MIN_LENGTH = 3;

    private final String template;

    private PrefixWordMatcher(String template) {
        Objects.requireNonNull(template);

        String temp = template.trim().toLowerCase();
        if (temp.isEmpty()) {
            throw new IllegalArgumentException("template cannot be blank or empty string.");
        }

        this.template = template;
    }

    @Override
    public OptionalInt match(MatcherCost matcherCost, String input) {
        Objects.requireNonNull(matcherCost);
        Objects.requireNonNull(input);

        String lowerInput = input.trim().toLowerCase();
        if (lowerInput.length() < MIN_LENGTH) {
            return OptionalInt.empty();
        }

        if (template.startsWith(lowerInput)) {
            return OptionalInt.of(matcherCost.getCost(MatcherCostType.PREFIX_ONLY));
        }

        return OptionalInt.empty();
    }

}
