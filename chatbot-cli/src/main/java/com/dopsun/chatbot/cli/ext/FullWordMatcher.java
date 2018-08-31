/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.ext;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.regex.Pattern;

import com.dopsun.chatbot.cli.MatcherCost;
import com.dopsun.chatbot.cli.MatcherCostType;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class FullWordMatcher implements WordMatcher {
    /**
     * @return
     */
    public static final WordMatcherFactory newFactory() {
        return new Factory();
    }

    static class Factory implements WordMatcherFactory {
        @Override
        public FullWordMatcher compile(String template) {
            return new FullWordMatcher(template);
        }
    }

    private static String compile(String input) {
        int index = input.indexOf('[');
        if (index >= 0) {
            int endIndex = input.indexOf(']', index);
            if (endIndex < 0) {
                throw new RuntimeException("No matched ] found.");
            }

            StringBuilder sb = new StringBuilder();
            if (index > 0) {
                sb.append(compile(input.substring(0, index)));

                sb.append("(");
                sb.append(compile(input.substring(index + 1, endIndex)));
                sb.append(")?");

                if (endIndex < (input.length() - 1)) {
                    sb.append(compile(input.substring(endIndex + 1)));
                }
            }
            return sb.toString();
        }

        index = input.indexOf('(');
        if (index >= 0) {
            int endIndex = input.indexOf(')', index);
            if (endIndex < 0) {
                throw new RuntimeException("No matched ) found.");
            }

            StringBuilder sb = new StringBuilder();
            if (index > 0) {
                sb.append(compile(input.substring(0, index)));

                sb.append("(");
                sb.append(compile(input.substring(index + 1, endIndex)));
                sb.append("){1}");

                if (endIndex < (input.length() - 1)) {
                    sb.append(compile(input.substring(endIndex + 1)));
                }
            }
            return sb.toString();
        }

        index = input.indexOf('|');
        if (index >= 0) {
            StringBuilder sb = new StringBuilder();

            sb.append("(");
            String[] parts = input.split("\\|");
            boolean first = true;
            for (String part : parts) {
                if (first) {
                    first = false;
                } else {
                    sb.append("|");
                }

                sb.append(part);
            }
            sb.append("){1}");
            return sb.toString();
        }

        return input;
    }

    private final String wordLower;
    private final Pattern fullMatchPattern;

    /**
     * @param word
     */
    public FullWordMatcher(String word) {
        Objects.requireNonNull(word);

        this.wordLower = word.toLowerCase();
        String patternString = compile(wordLower);

        this.fullMatchPattern = Pattern.compile("^" + patternString + "$");
    }

    @Override
    public OptionalInt match(MatcherCost matcherCost, String word) {
        if (fullMatchPattern.matcher(word).find()) {
            return OptionalInt.of(matcherCost.getCost(MatcherCostType.FULL_WORD));
        }

        return OptionalInt.empty();
    }
}
