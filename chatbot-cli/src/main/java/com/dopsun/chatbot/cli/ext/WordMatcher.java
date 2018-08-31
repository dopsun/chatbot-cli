/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.ext;

import java.util.OptionalInt;

import com.dopsun.chatbot.cli.MatcherCost;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface WordMatcher {
    /**
     * @param cost
     * @param input
     * @return {@link OptionalInt#empty()} if not matched; otherwise returns the rank discount of
     *         matching.
     */
    OptionalInt match(MatcherCost cost, String input);
}
