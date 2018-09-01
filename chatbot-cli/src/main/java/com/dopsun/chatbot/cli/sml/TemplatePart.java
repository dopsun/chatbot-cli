/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.Objects;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public abstract class TemplatePart {
    final String name;

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

    static class VariablePart extends TemplatePart {
        public VariablePart(String name) {
            super(name);
        }
    }
}
