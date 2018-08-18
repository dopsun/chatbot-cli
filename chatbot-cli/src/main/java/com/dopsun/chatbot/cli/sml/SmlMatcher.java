/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.CliArgument;
import com.dopsun.chatbot.cli.CliCommand;
import com.dopsun.chatbot.cli.CliParseResult;
import com.dopsun.chatbot.cli.CliParserException;
import com.dopsun.chatbot.cli.tds.DataItem;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class SmlMatcher {
    private final String commandName;
    private final List<SmlInputMatcher> inputMatcherList;

    /**
     * @param dataItem
     */
    public SmlMatcher(DataItem dataItem) {
        Objects.requireNonNull(dataItem);

        this.commandName = dataItem.commandName();

        List<SmlInputMatcher> tempList = new ArrayList<>();
        for (String template : dataItem.templates()) {
            tempList.add(new SmlInputMatcher(template));
        }
        this.inputMatcherList = tempList;
    }

    /**
     * @param commandText
     * @return
     * @throws CliParserException
     */
    public Optional<CliParseResult> tryParse(String commandText) throws CliParserException {
        Objects.requireNonNull(commandText);

        for (SmlInputMatcher inputMatcher : inputMatcherList) {
            Optional<List<CliArgument>> optArgList = inputMatcher.parse(commandText);

            if (optArgList.isPresent()) {
                CliParseResultImpl cliParseResult = new CliParseResultImpl(this.commandName,
                        optArgList.get());
                return Optional.of(cliParseResult);
            }
        }

        return Optional.empty();
    }

    static class CliParseResultImpl implements CliParseResult {
        private final CliCommand command;

        public CliParseResultImpl(String commandName, List<CliArgument> argList) {
            Objects.requireNonNull(commandName);
            Objects.requireNonNull(argList);

            this.command = new CliCommandImpl(commandName, argList);
        }

        @Override
        public CliCommand command() {
            return command;
        }
    }

    static class CliCommandImpl implements CliCommand {
        private final String name;
        private final List<CliArgument> argList;

        public CliCommandImpl(String name, List<CliArgument> argList) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(argList);

            this.name = name;
            this.argList = argList;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public List<CliArgument> arguments() {
            return argList;
        }

    }

    static class CliArgumentImpl implements CliArgument {
        private final String name;
        private final Optional<String> value;

        public CliArgumentImpl(String name, Optional<String> value) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(value);

            this.name = name;
            this.value = value;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Optional<String> value() {
            return value;
        }
    }

}
