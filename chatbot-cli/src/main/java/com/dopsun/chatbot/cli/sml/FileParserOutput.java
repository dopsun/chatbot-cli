/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Objects;

import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.input.FileTrainingSetWriter;
import com.dopsun.chatbot.cli.input.TrainingFeedback;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class FileParserOutput implements ParserOutput {
    private final SmlParserContext context;

    private final FileTrainingSetWriter trainingSetWriter;

    private final FileTrainingSetWriter succeedWriter;
    private final BufferedWriter failWriter;

    /**
     * @param context
     * @param outDir
     */
    public FileParserOutput(SmlParserContext context, Path outDir) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(outDir);

        this.context = context;

        String timestamp = LocalDateTime.now().toString();

        // TODO: make naming pattern configurable.
        Path trainingDataPath = outDir.resolve("training-data-" + timestamp + ".yml");
        try {
            this.trainingSetWriter = FileTrainingSetWriter.createNew(trainingDataPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create trainingData file.", e);
        }

        Path succeedPath = outDir.resolve("parse-succeed-" + timestamp + ".yml");
        try {
            this.succeedWriter = FileTrainingSetWriter.createNew(succeedPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create parse-succeed file.", e);
        }

        Path failPath = outDir.resolve("parse-fail-" + timestamp + ".yml");
        try {
            this.failWriter = Files.newBufferedWriter(failPath, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create parse-succeed file.", e);
        }
    }

    @Override
    public void fail(String input) {
        try {
            failWriter.write(input);
            failWriter.write("\n");
            failWriter.flush();
        } catch (IOException e) {
            context.tracer().log(this, "Failed to write fail result. " + e.getMessage());
        }
    }

    @Override
    public void succeed(String input, Command command) {
        try {
            succeedWriter.write(input, command, TrainingFeedback.NEUTRAL);
        } catch (IOException e) {
            context.tracer().log(this, "Failed to write succeed result. " + e.getMessage());
        }
    }

    @Override
    public void feedback(String input, Command command, TrainingFeedback feedback) {
        try {
            trainingSetWriter.write(input, command, feedback);
        } catch (IOException e) {
            context.tracer().log(this, "Failed to write feedback. " + e.getMessage());
        }
    }

}
