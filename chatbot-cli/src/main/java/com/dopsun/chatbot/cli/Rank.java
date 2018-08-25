/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.Comparator;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class Rank implements Comparable<Rank> {
    /**
     * Rank comparator.
     */
    public static Comparator<Command> comparator = (c1, c2) -> {
        return c1.rank().compareTo(c2.rank());
    };

    /** Highest rank value */
    public static int HIGHEST_RANK = 0;

    /** Highest rank with value {@link #HIGHEST_RANK} */
    public static final Rank HIGHEST = new Rank(HIGHEST_RANK);

    /**
     * @param value
     * @return
     */
    public static final Rank of(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Rank value expected >= 0, actual: " + value);
        }

        return new Rank(value);
    }

    private final int value;

    /**
     * @param value
     */
    private Rank(int value) {
        this.value = value;
    }

    /**
     * @return
     */
    public int value() {
        return value;
    }

    @Override
    public int compareTo(Rank another) {
        return Integer.compare(another.value, this.value);
    }
}
