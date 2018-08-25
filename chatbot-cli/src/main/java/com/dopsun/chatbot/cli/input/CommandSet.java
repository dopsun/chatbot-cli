/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import java.util.function.Consumer;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface CommandSet {
    /**
     * @param itemVisitor
     */
    void accept(Consumer<CommandItem> itemVisitor);
}
