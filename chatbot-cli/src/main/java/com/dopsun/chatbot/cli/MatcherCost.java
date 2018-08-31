/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface MatcherCost {
    /**
     * @param type
     * @return matcher cost, {@link MatcherCostType#value()} if no other alternative source.
     */
    int getCost(MatcherCostType type);
}
