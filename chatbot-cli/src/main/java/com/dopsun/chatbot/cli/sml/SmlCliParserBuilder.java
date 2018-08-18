/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.dopsun.chatbot.cli.CliParser;
import com.dopsun.chatbot.cli.CliParserBuilder;
import com.dopsun.chatbot.cli.tds.DataSet;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class SmlCliParserBuilder implements CliParserBuilder {
    private final List<DataSet> dataSets = new ArrayList<>();

    @Override
    public CliParser build() {
        SmlCliParser parser = new SmlCliParser(dataSets);
        return parser;
    }

    /**
     * @param dataSet
     */
    public void add(DataSet dataSet) {
        Objects.requireNonNull(dataSet);

        this.dataSets.add(dataSet);
    }
}
