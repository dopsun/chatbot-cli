/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.tds;

import java.util.List;
import java.util.Objects;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class TemplateDataItem implements DataItem {
    private final String commandName;
    private final List<String> templates;

    /**
     * @param commandName
     * @param templates
     */
    public TemplateDataItem(String commandName, final List<String> templates) {
        Objects.requireNonNull(commandName);
        Objects.requireNonNull(templates);

        this.commandName = commandName;
        this.templates = templates;
    }

    @Override
    public String commandName() {
        return commandName;
    }

    @Override
    public List<String> templates() {
        return templates;
    }

}
