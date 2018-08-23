/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import com.dopsun.chatbot.cli.input.CommandSet;
import com.dopsun.chatbot.cli.input.TrainingSet;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface ParserBuilder {
    /**
     * @return a new instance of {@link Parser}.
     */
    Parser build();

    /**
     * @param dataSet
     */
    void addCommandSet(CommandSet dataSet);

    /**
     * @param trainingSet
     */
    void addTrainingSet(TrainingSet trainingSet);

    /**
     * @param logger
     *            the logger to set
     */
    void setLogger(TraceListener logger);
}
