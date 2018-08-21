/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dopsun.chatbot.cli.CliParseResult;
import com.dopsun.chatbot.cli.CommandAndRank;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class ParseResultBuilder {
    /**
     * @return
     */
    public static final ParseResultBuilder create() {
        return new ParseResultBuilder();
    }

    private final List<CommandAndRank> commands = new ArrayList<>();

    private ParseResultBuilder() {

    }

    public Optional<CliParseResult> build() {
        if (commands.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new ParseResultImpl(this));
    }

    public void add(CommandAndRank commandAndRank) {
        Objects.requireNonNull(commandAndRank);

        this.commands.add(commandAndRank);
    }

    static class ParseResultImpl implements CliParseResult {
        private final List<CommandAndRank> allCommands;

        private ParseResultImpl(ParseResultBuilder builder) {
            Objects.requireNonNull(builder);

            /** @formatter:off */
            this.allCommands = builder.commands.stream()
                    .sorted(CommandAndRank.rankComparator)
                    .collect(Collectors.toList());
            /** @formatter:on */
        }

        /**
         * @return the altCommands
         */
        public List<CommandAndRank> allCommands() {
            return allCommands;
        }
    }
}
