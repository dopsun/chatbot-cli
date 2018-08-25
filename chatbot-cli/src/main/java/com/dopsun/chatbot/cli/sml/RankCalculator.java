/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import com.dopsun.chatbot.cli.Rank;

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
    public Rank rank() {
        return Rank.of(rank);
    }

    public void skipConstantWord(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("illegal count: " + count);
        }

        rank += count * SKIP_CONST_WORD_POINTS_PER_WORD;
    }

    public void discount(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("illegal points: " + points);
        }

        rank += points;
    }
}
