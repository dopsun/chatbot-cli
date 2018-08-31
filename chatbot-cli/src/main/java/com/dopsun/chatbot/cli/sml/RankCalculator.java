/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.Objects;

import com.dopsun.chatbot.cli.MatcherCost;
import com.dopsun.chatbot.cli.MatcherCostType;
import com.dopsun.chatbot.cli.Rank;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class RankCalculator {
    private final MatcherCost matcherCost;

    private int rank = 0;

    public RankCalculator(MatcherCost matcherCost) {
        Objects.requireNonNull(matcherCost);

        this.matcherCost = matcherCost;
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

        rank += count * matcherCost.getCost(MatcherCostType.SKIP_ONE_CONST_WORD);
    }

    public void discount(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("illegal points: " + points);
        }

        rank += points;
    }
}
