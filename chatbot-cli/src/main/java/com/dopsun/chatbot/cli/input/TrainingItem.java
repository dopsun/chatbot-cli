/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import com.dopsun.chatbot.cli.Command;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface TrainingItem {
    /**
     * @return
     */
    String input();

    /**
     * @return
     */
    Command command();

    /**
     * @return
     */
    TrainingFeedback feedback();
}
