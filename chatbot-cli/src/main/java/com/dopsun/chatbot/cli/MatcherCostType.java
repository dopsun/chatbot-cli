/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public enum MatcherCostType {
    /**
     * Matches full word.
     */
    FULL_WORD(0),

    /**
     * Skip one const word.
     */
    SKIP_ONE_CONST_WORD(5),

    /**
     * Only matches prefix.
     */
    PREFIX_ONLY(5);

    private int value;

    private MatcherCostType(int value) {
        this.value = value;
    }

    /**
     * @return
     */
    public int value() {
        return value;
    }
}
