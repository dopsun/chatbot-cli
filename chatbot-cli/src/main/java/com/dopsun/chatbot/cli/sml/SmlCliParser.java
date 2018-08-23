/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.ParseResult;
import com.dopsun.chatbot.cli.Parser;
import com.dopsun.chatbot.cli.CommandAndRank;
import com.dopsun.chatbot.cli.input.CommandItem;
import com.dopsun.chatbot.cli.input.CommandSet;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class SmlCliParser implements Parser {
    private final ParserTrace trace;
    private final List<SmlCommandMatcher> matcherList = new ArrayList<>();

    /**
     * @param commandSets
     */
    SmlCliParser(List<CommandSet> commandSets, ParserTrace trace) {
        Objects.requireNonNull(commandSets);
        Objects.requireNonNull(trace);

        this.trace = trace;

        for (CommandSet commandSet : commandSets) {
            for (CommandItem commandItem : commandSet.items()) {
                SmlCommandMatcher matcher = new SmlCommandMatcher(commandItem);
                matcherList.add(matcher);
            }
        }
    }

    /**
     * @return
     */
    ParserTrace trace() {
        return this.trace;
    }

    /**
     * FIXME: if matchList is large, then ForkJoinPool can be used.
     */
    @Override
    public Optional<ParseResult> tryParse(String commandText) {
        Objects.requireNonNull(commandText);

        trace.enterMethod(this, "tryParse", commandText);

        ParseResultBuilder builder = ParseResultBuilder.create();

        for (SmlCommandMatcher matcher : matcherList) {
            List<CommandAndRank> commandList = matcher.tryParse(commandText);

            for (CommandAndRank commandAndRank : commandList) {
                builder.add(commandAndRank);
            }
        }

        return builder.build();
    }

}
