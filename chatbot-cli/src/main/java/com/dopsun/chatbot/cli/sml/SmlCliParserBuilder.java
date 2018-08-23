/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import com.dopsun.chatbot.cli.CliParser;
import com.dopsun.chatbot.cli.CliParserBuilder;
import com.dopsun.chatbot.cli.TraceListener;
import com.dopsun.chatbot.cli.tds.DataSet;
import com.dopsun.chatbot.cli.tds.TrainingSet;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class SmlCliParserBuilder implements CliParserBuilder {
    private final List<DataSet> dataSets = new ArrayList<>();
    private final List<TrainingSet> trainingSets = new ArrayList<>();

    @Nullable
    private TraceListener traceListener;

    @Override
    public CliParser build() {
        ParserTrace parserTrace = new ParserTrace(Optional.ofNullable(traceListener));
        SmlCliParser parser = new SmlCliParser(dataSets, parserTrace);
        return parser;
    }

    /**
     * @param dataSet
     */
    public void addDataSet(DataSet dataSet) {
        Objects.requireNonNull(dataSet);

        this.dataSets.add(dataSet);
    }

    /**
     * @param trainingSet
     */
    public void addTrainingSet(TrainingSet trainingSet) {
        Objects.requireNonNull(trainingSet);

        this.trainingSets.add(trainingSet);
    }

    /**
     * @return the logger
     */
    public TraceListener getLogger() {
        return traceListener;
    }

    /**
     * @param logger
     *            the logger to set
     */
    public void setLogger(TraceListener logger) {
        this.traceListener = logger;
    }
}
