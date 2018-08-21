/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.CliParseResult;
import com.dopsun.chatbot.cli.CliParser;
import com.dopsun.chatbot.cli.tds.DataItem;
import com.dopsun.chatbot.cli.tds.DataSet;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class SmlCliParser implements CliParser {
    private final ParserTrace trace;
    private final List<SmlMatcher> matcherList = new ArrayList<>();

    /**
     * @param dataSets
     */
    SmlCliParser(List<DataSet> dataSets, ParserTrace trace) {
        Objects.requireNonNull(dataSets);
        Objects.requireNonNull(trace);

        this.trace = trace;

        for (DataSet ds : dataSets) {
            for (DataItem di : ds.items()) {
                SmlMatcher matcher = new SmlMatcher(di);
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
    public Optional<CliParseResult> tryParse(String commandText) {
        Objects.requireNonNull(commandText);

        trace.enterMethod(this, "tryParse", commandText);

        for (SmlMatcher matcher : matcherList) {
            Optional<CliParseResult> optResult = matcher.tryParse(commandText);
            if (optResult.isPresent()) {
                return optResult;
            }
        }

        return Optional.empty();
    }

}
