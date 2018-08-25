/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import java.util.List;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface CommandItem {
    /**
     * @return
     */
    String commandName();

    /**
     * @return the input
     */
    List<String> templates();

}
