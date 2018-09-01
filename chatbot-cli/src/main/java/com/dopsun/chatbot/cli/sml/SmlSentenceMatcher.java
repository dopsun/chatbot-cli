/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.sml.Template.MatchResult;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class SmlSentenceMatcher {
    private final String commandName;
    private final Template template;

    /**
     * @param context
     * @param commandName
     * @param template
     */
    public SmlSentenceMatcher(SmlParserContext context, String commandName, String template) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(commandName);
        Objects.requireNonNull(template);

        this.commandName = commandName;
        this.template = Template.compile(context, template);
    }

    /**
     * @param commandText
     * @return
     */
    public Optional<Command> parse(String commandText) {
        Objects.requireNonNull(commandText);

        Optional<MatchResult> optResult = template.match(commandText);
        if (!optResult.isPresent()) {
            return Optional.empty();
        }

        MatchResult result = optResult.get();

        CliCommandImpl cliCommand = new CliCommandImpl(commandName, template.template(),
                result.rank(), result.argList());

        return Optional.of(cliCommand);
    }

}
