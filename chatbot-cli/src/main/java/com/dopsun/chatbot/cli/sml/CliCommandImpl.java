/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.List;
import java.util.Objects;

import com.dopsun.chatbot.cli.Argument;
import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.Rank;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class CliCommandImpl implements Command {
    private final String name;
    private final String template;
    private final Rank rank;
    private final List<Argument> argList;

    public CliCommandImpl(String name, String template, Rank rank, List<Argument> argList) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(template);
        Objects.requireNonNull(argList);

        this.name = name;
        this.template = template;
        this.rank = rank;
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
    public Rank rank() {
        return rank;
    }

    @Override
    public List<Argument> arguments() {
        return argList;
    }

}