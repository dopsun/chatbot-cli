/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dopsun.chatbot.cli.Argument;
import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.ParseResult;
import com.dopsun.chatbot.cli.Parser;
import com.dopsun.chatbot.cli.ParserBuilder;
import com.dopsun.chatbot.cli.ParserException;
import com.dopsun.chatbot.cli.input.CommandSet;
import com.dopsun.chatbot.cli.input.CommandSetReader;
import com.dopsun.chatbot.cli.input.TrainingSet;
import com.dopsun.chatbot.cli.input.TrainingSetReader;
import com.dopsun.chatbot.cli.sml.PropertiesScript.ScriptCase;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
public class SmlCliParserTest {
    private static Parser parser;

    @BeforeClass
    public static void prepareParser() throws URISyntaxException, IOException {
        URL csUrl = ClassLoader.getSystemResource("input/command-data.properties");
        Path csPath = Paths.get(csUrl.toURI());

        CommandSetReader csReader = new CommandSetReader();
        CommandSet commandSet = csReader.read(csPath);

        URL tsUrl = ClassLoader.getSystemResource("input/training-data.yml");
        Path tsPath = Paths.get(tsUrl.toURI());
        TrainingSetReader tsReader = new TrainingSetReader();
        TrainingSet trainingSet = tsReader.read(tsPath);

        ParserBuilder parserBuilder = Parser.newBuilder();
        parserBuilder.addCommandSet(commandSet);
        parserBuilder.addTrainingSet(trainingSet);

        parserBuilder.setLogger(System.out::println);
        parser = parserBuilder.build();
    }

    public static String parseResultToString(ParseResult parseResult) {
        Command command = parseResult.command();

        StringBuilder sb = new StringBuilder();
        sb.append(command.name());
        sb.append("(");

        int i = 0;
        for (Argument arg : command.arguments()) {
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
    public void sanityTest() throws ParserException, URISyntaxException, IOException {
        URL url = ClassLoader.getSystemResource("input/test-data.properties");
        Path testCasesPath = Paths.get(url.toURI());

        PropertiesScript script = new PropertiesScript(testCasesPath);

        for (ScriptCase scriptCase : script.scriptCases()) {
            Optional<ParseResult> optResult = parser.tryParse(scriptCase.input());

            Optional<String> optScriptResult = scriptCase.result();
            if (!optScriptResult.isPresent()) {
                Assert.assertFalse(optResult.isPresent());
                continue;
            }

            String scriptResult = optScriptResult.get();
            ParseResult cliResult = optResult.get();

            Assert.assertEquals(scriptResult, parseResultToString(cliResult));
        }
    }
}
