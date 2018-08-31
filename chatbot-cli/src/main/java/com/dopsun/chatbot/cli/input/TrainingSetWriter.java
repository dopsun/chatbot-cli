/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import java.io.IOException;

import com.dopsun.chatbot.cli.Command;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface TrainingSetWriter {
    /**
     * @param input
     * @param command
     * @param feedback
     * @throws IOException
     */
    void write(String input, Command command, TrainingFeedback feedback) throws IOException;
}
