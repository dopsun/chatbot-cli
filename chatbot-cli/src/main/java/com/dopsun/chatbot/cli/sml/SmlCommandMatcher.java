/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.CommandAndRank;
import com.dopsun.chatbot.cli.tds.DataItem;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class SmlCommandMatcher {
    private final List<SmlSentenceMatcher> inputMatcherList;

    /**
     * @param dataItem
     */
    public SmlCommandMatcher(DataItem dataItem) {
        Objects.requireNonNull(dataItem);

        List<SmlSentenceMatcher> tempList = new ArrayList<>();

        for (String template : dataItem.templates()) {
            tempList.add(new SmlSentenceMatcher(dataItem.commandName(), template));
        }

        this.inputMatcherList = tempList;
    }

    /**
     * @param commandText
     * @return
     */
    public List<CommandAndRank> tryParse(String commandText) {
        Objects.requireNonNull(commandText);

        List<CommandAndRank> list = new ArrayList<>();
        for (SmlSentenceMatcher inputMatcher : inputMatcherList) {
            Optional<CommandAndRank> optCommandAndRank = inputMatcher.parse(commandText);

            if (optCommandAndRank.isPresent()) {
                list.add(optCommandAndRank.get());
            }
        }

        return list;
    }
}
