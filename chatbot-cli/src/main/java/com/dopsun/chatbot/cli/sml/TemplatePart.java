/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.Objects;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
abstract class TemplatePart {
    private final String name;

    /**
     * @param name
     */
    protected TemplatePart(String name) {
        Objects.requireNonNull(name);

        String temp = name.trim();
        if (temp.length() == 0) {
            throw new IllegalArgumentException("Empty name.");
        }

        this.name = temp;
    }

    /**
     * @return
     */
    public String name() {
        return name;
    }
}
