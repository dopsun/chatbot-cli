/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.dopsun.chatbot.cli.MatcherCost;
import com.dopsun.chatbot.cli.MatcherCostType;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class FileMatchCostInput implements MatcherCost {
    private final Map<MatcherCostType, Integer> matchCosts = new HashMap<>();

    /**
     * @param path
     * @throws IOException
     */
    public FileMatchCostInput(Path path) throws IOException {
        Objects.requireNonNull(path);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            Properties properties = new Properties();
            properties.load(reader);

            for (Object key : properties.keySet()) {
                String keyName = (String) key;
                String value = properties.getProperty((String) key);

                MatcherCostType matchCostType = MatcherCostType.valueOf(keyName);
                matchCosts.put(matchCostType, Integer.getInteger(value));
            }
        }
    }

    @Override
    public int getCost(MatcherCostType type) {
        Objects.requireNonNull(type);

        Integer cost = matchCosts.get(type);
        if (cost == null) {
            return type.value();
        }

        return cost;
    }

}
