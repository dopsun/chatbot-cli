/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.Parser;
import com.dopsun.chatbot.cli.ParserBuilder;
import com.dopsun.chatbot.cli.ParserTracer;
import com.dopsun.chatbot.cli.input.CommandSet;
import com.dopsun.chatbot.cli.input.TrainingSet;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class SmlCliParserBuilder implements ParserBuilder {
    private final List<CommandSet> commandSet = new ArrayList<>();
    private final List<TrainingSet> trainingSets = new ArrayList<>();

    private Optional<ParserTracer> parserTracer = Optional.empty();

    @Override
    public Parser build() {
        return new SmlCliParser(this);
    }

    /**
     * @return the commandSet
     */
    public List<CommandSet> commandSet() {
        return commandSet;
    }

    /**
     * @return the trainingSets
     */
    public List<TrainingSet> trainingSets() {
        return trainingSets;
    }

    /**
     * @return the parserTracer
     */
    public Optional<ParserTracer> parserTracer() {
        return parserTracer;
    }

    /**
     * @param dataSet
     */
    public void addCommandSet(CommandSet dataSet) {
        Objects.requireNonNull(dataSet);

        this.commandSet.add(dataSet);
    }

    /**
     * @param trainingSet
     */
    public void addTrainingSet(TrainingSet trainingSet) {
        Objects.requireNonNull(trainingSet);

        this.trainingSets.add(trainingSet);
    }

    /**
     * @param listener
     *            the logger to set
     */
    public void setTracer(ParserTracer listener) {
        this.parserTracer = Optional.of(listener);
    }
}
