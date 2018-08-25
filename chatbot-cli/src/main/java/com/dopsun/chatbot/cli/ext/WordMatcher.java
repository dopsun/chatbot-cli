/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.ext;

import java.util.OptionalInt;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface WordMatcher {
    /**
     * @param input
     * @return {@link OptionalInt#empty()} if not matched; otherwise returns the rank discount of
     *         matching.
     */
    OptionalInt match(String input);
}
