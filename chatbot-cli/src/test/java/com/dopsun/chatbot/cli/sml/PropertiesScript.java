/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Nullable;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class PropertiesScript {
    private static final String INPUT_KEY = "test.%1$s.input";
    private static final String RESULT_KEY = "test.%1$s.result";

    private final List<ScriptCase> caseList = new ArrayList<>();

    /**
     * @param path
     * @throws URISyntaxException
     * @throws IOException
     */
    public PropertiesScript(Path path) throws URISyntaxException, IOException {
        // URL url = ClassLoader.getSystemResource("input/training-data.properties");
        // Path dsPath = Paths.get(url.toURI());

        try (Reader reader = Files.newBufferedReader(path)) {
            Properties props = new Properties();
            props.load(reader);

            int index = 1;
            while (true) {
                String inputKey = String.format(INPUT_KEY, index);
                String resultKey = String.format(RESULT_KEY, index);

                String input = props.getProperty(inputKey);
                String result = props.getProperty(resultKey);
                if (input == null) {
                    break;
                }

                caseList.add(new ScriptCase(input, result));

                index++;
            }
        }
    }

    /**
     * @return the caseList
     */
    public List<ScriptCase> scriptCases() {
        return caseList;
    }

    /**
     * @author Dop Sun
     * @since 1.0.0
     */
    public static class ScriptCase {
        private final String input;
        private final Optional<String> result;

        /**
         * @param input
         * @param result
         */
        public ScriptCase(String input, @Nullable String result) {
            Objects.requireNonNull(input);

            this.input = input;

            String tempResult = Optional.ofNullable(result).orElse("").trim();
            if (tempResult.length() == 0) {
                this.result = Optional.empty();
            } else {
                this.result = Optional.of(tempResult);
            }
        }

        /**
         * @return
         */
        public String input() {
            return input;
        }

        /**
         * @return
         */
        public Optional<String> result() {
            return result;
        }
    }
}
