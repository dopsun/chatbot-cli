/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.List;
import java.util.Objects;

import com.dopsun.chatbot.cli.CliArgument;
import com.dopsun.chatbot.cli.CliCommand;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class CliCommandImpl implements CliCommand {
    private final String name;
    private final String template;
    private final List<CliArgument> argList;

    public CliCommandImpl(String name, String template, List<CliArgument> argList) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(template);
        Objects.requireNonNull(argList);

        this.name = name;
        this.template = template;
        this.argList = argList;
    }

    @Override
    public String name() {
        return name;
    }

    /**
     * @return the template
     */
    public String template() {
        return template;
    }

    @Override
    public List<CliArgument> arguments() {
        return argList;
    }

}