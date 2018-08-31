/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.input.CommandItem;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class SmlCommandMatcher {
    private final List<SmlSentenceMatcher> inputMatcherList;

    /**
     * @param context
     * @param dataItem
     */
    public SmlCommandMatcher(SmlParserContext context, CommandItem dataItem) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(dataItem);

        List<SmlSentenceMatcher> tempList = new ArrayList<>();

        for (String template : dataItem.templates()) {
            tempList.add(new SmlSentenceMatcher(context, dataItem.commandName(), template));
        }

        this.inputMatcherList = tempList;
    }

    /**
     * @param commandText
     * @return
     */
    public List<Command> tryParse(String commandText) {
        Objects.requireNonNull(commandText);

        List<Command> list = new ArrayList<>();
        for (SmlSentenceMatcher inputMatcher : inputMatcherList) {
            Optional<Command> optCommandAndRank = inputMatcher.parse(commandText);

            if (optCommandAndRank.isPresent()) {
                list.add(optCommandAndRank.get());
            }
        }

        return list;
    }
}
