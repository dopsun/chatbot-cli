/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dopsun.chatbot.cli.CliArgument;
import com.dopsun.chatbot.cli.CliCommand;
import com.dopsun.chatbot.cli.CliParseResult;
import com.dopsun.chatbot.cli.CliParser;
import com.dopsun.chatbot.cli.CliParserException;
import com.dopsun.chatbot.cli.sml.PropertiesScript.ScriptCase;
import com.dopsun.chatbot.cli.tds.TemplateDataSet;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
public class SmlCliParserTest {
    private static CliParser cliParser;

    @BeforeClass
    public static void prepareParser() throws URISyntaxException {
        URL url = ClassLoader.getSystemResource("input/training-data.properties");
        Path dsPath = Paths.get(url.toURI());

        TemplateDataSet dataSet = new TemplateDataSet(dsPath);

        SmlCliParserBuilder builder = new SmlCliParserBuilder();
        builder.add(dataSet);
        builder.setLogger(System.out::println);
        cliParser = builder.build();
    }

    public static void loadScript() throws URISyntaxException, IOException {
        URL url = ClassLoader.getSystemResource("input/training-data.properties");
        Path dsPath = Paths.get(url.toURI());

        try (Reader reader = Files.newBufferedReader(dsPath)) {
            Properties props = new Properties();
            props.load(reader);

        }
    }

    public static String parseResultToString(CliParseResult parseResult) {
        CliCommand command = parseResult.command();

        StringBuilder sb = new StringBuilder();
        sb.append(command.name());
        sb.append("(");

        int i = 0;
        for (CliArgument arg : command.arguments()) {
            if (i > 0) {
                sb.append(", ");
            }
            i++;

            sb.append("${" + arg.name() + "}=" + arg.value().orElse(""));
        }

        sb.append(")");

        return sb.toString();
    }

    @Test
    public void sanityTest() throws CliParserException, URISyntaxException, IOException {
        URL url = ClassLoader.getSystemResource("input/test-data.properties");
        Path testCasesPath = Paths.get(url.toURI());

        PropertiesScript script = new PropertiesScript(testCasesPath);

        for (ScriptCase scriptCase : script.scriptCases()) {
            Optional<CliParseResult> optResult = cliParser.tryParse(scriptCase.input());

            Optional<String> optScriptResult = scriptCase.result();
            if (!optScriptResult.isPresent()) {
                Assert.assertFalse(optResult.isPresent());
                continue;
            }

            String scriptResult = optScriptResult.get();
            CliParseResult cliResult = optResult.get();

            Assert.assertEquals(scriptResult, parseResultToString(cliResult));
        }
    }
}
