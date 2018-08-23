/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.tds;

import java.util.List;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface CommandSet {
    /**
     * @return
     */
    List<CommandItem> items();
}
