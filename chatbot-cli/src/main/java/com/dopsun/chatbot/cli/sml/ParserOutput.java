/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.input.TrainingFeedback;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
interface ParserOutput {
    /**
     * Write nothing.
     */
    static ParserOutput NULL = new ParserOutput() {
        @Override
        public void fail(String input) {
        }

        @Override
        public void succeed(String input, Command command) {
        }

        @Override
        public void feedback(String input, Command command, TrainingFeedback feedback) {
        }
    };

    /**
     * Write failed result.
     * 
     * @param input
     */
    void fail(String input);

    /**
     * Write successful result.
     * 
     * @param input
     * @param command
     */
    void succeed(String input, Command command);

    /**
     * Provide feedback.
     * 
     * @param input
     * @param command
     * @param feedback
     */
    void feedback(String input, Command command, TrainingFeedback feedback);
}
