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