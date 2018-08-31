/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.MatcherCost;
import com.dopsun.chatbot.cli.ParseResult;
import com.dopsun.chatbot.cli.Parser;
import com.dopsun.chatbot.cli.ext.CompositeWordMatcherFactory;
import com.dopsun.chatbot.cli.ext.WordMatcherFactory;
import com.dopsun.chatbot.cli.input.CommandSet;
import com.dopsun.chatbot.cli.input.FileTrainingSetWriter;
import com.dopsun.chatbot.cli.input.TrainingFeedback;
import com.dopsun.chatbot.cli.input.TrainingSetWriter;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class SmlCliParser implements Parser, SmlParserContext {
    private final ParserTracerWrapper tracer;
    private final List<SmlCommandMatcher> matcherList = new ArrayList<>();

    private final WordMatcherFactory wordMatcherFactory;
    private final MatcherCost matcherCost;

    private final TrainingSetWriter trainingSetWriter;

    /**
     * @param commandSets
     * @param traceListener
     */
    SmlCliParser(SmlCliParserBuilder builder) {
        Objects.requireNonNull(builder);
        if (builder.commandSet().isEmpty()) {
            throw new IllegalStateException("Empty command set.");
        }

        this.tracer = new ParserTracerWrapper(builder.parserTracer());

        wordMatcherFactory = builder.wordMatcherFactory()
                .orElse(CompositeWordMatcherFactory.createDefault());

        for (CommandSet commandSet : builder.commandSet()) {
            commandSet.accept(commandItem -> {
                SmlCommandMatcher matcher = new SmlCommandMatcher(this, commandItem);
                matcherList.add(matcher);
            });
        }

        this.matcherCost = builder.matcherCost().orElse(matcherCostType -> matcherCostType.value());

        if (builder.outDir().isPresent()) {
            String timestamp = LocalDateTime.now().toString();
            Path dataPath = builder.outDir().get();

            // TODO: make naming pattern configurable.
            Path trainingDataPath = dataPath.resolve("training-data-" + timestamp + ".yml");
            try {
                this.trainingSetWriter = FileTrainingSetWriter.createNew(trainingDataPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create trainingData file.", e);
            }
        } else {
            this.trainingSetWriter = (input, command, feedback) -> {
            };
        }
    }

    /**
     * @return
     */
    ParserTracerWrapper tracer() {
        return this.tracer;
    }

    @Override
    public WordMatcherFactory wordMatcherFactory() {
        return wordMatcherFactory;
    }

    /**
     * @return the matcherCost
     */
    public MatcherCost matcherCost() {
        return matcherCost;
    }

    /**
     * FIXME: if matchList is large, then ForkJoinPool can be used.
     */
    @Override
    public Optional<ParseResult> tryParse(String commandText) {
        Objects.requireNonNull(commandText);

        tracer.enterMethod(this, "tryParse", commandText);

        ParseResultBuilder builder = ParseResultBuilder.create();

        for (SmlCommandMatcher matcher : matcherList) {
            List<Command> commandList = matcher.tryParse(commandText);

            for (Command command : commandList) {
                builder.add(command);
            }
        }

        Optional<ParseResult> optResult = builder.build();
        if (optResult.isPresent()) {
            ParseResult parseResult = optResult.get();
            try {
                this.trainingSetWriter.write(commandText, parseResult.command(),
                        TrainingFeedback.NEUTRAL);
            } catch (IOException e) {
                tracer.log(this, "Writer to trainingSet failed.");
            }
        }

        return optResult;
    }

}
