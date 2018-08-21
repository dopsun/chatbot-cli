/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class RankCalculator {
    private static int SKIP_CONST_WORD_POINTS_PER_WORD = 5;

    private int rank = 0;

    public RankCalculator() {

    }

    /**
     * @return
     */
    public int rank() {
        return rank;
    }

    public void skipConstantWord(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("illegal count: " + count);
        }

        rank += count * SKIP_CONST_WORD_POINTS_PER_WORD;
    }
}
