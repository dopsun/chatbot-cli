/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.ParseResult;
import com.dopsun.chatbot.cli.Rank;

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

    private final List<Command> commands = new ArrayList<>();

    private ParseResultBuilder() {

    }

    public Optional<ParseResult> build() {
        if (commands.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new ParseResultImpl(this));
    }

    public void add(Command command) {
        Objects.requireNonNull(command);

        this.commands.add(command);
    }

    static class ParseResultImpl implements ParseResult {
        private final List<Command> allCommands;

        private ParseResultImpl(ParseResultBuilder builder) {
            Objects.requireNonNull(builder);

            /** @formatter:off */
            this.allCommands = builder.commands.stream()
                    .sorted(Rank.comparator.reversed())
                    .collect(Collectors.toList());
            /** @formatter:on */
        }

        /**
         * @return the altCommands
         */
        public List<Command> allCommands() {
            return allCommands;
        }
    }
}
