/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import com.dopsun.chatbot.cli.CliCommand;

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
    CliCommand command();

    /**
     * @return
     */
    TrainingFeedback feedback();
}
