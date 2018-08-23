/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.Comparator;
import java.util.Objects;

/**
 * Command and its ranking. Command is in higher rank if {@link #rank()} is smaller.
 * 
 * @author Dop Sun
 * @since 1.0.0
 */
public final class CommandAndRank {
    /**
     * Comparing two {@link CommandAndRank} with its rank.
     */
    public static Comparator<CommandAndRank> rankComparator = (c1, c2) -> {
        return Integer.compare(c1.rank(), c2.rank());
    };

    private final Command command;
    private final int rank;

    /**
     * @param command
     * @param rank
     */
    public CommandAndRank(Command command, int rank) {
        Objects.requireNonNull(command);

        this.command = command;
        this.rank = rank;
    }

    /**
     * @return the command
     */
    public Command command() {
        return command;
    }

    /**
     * @return the rank
     */
    public int rank() {
        return rank;
    }
}