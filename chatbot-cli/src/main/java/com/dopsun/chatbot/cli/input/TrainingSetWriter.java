/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

import com.dopsun.chatbot.cli.Argument;
import com.dopsun.chatbot.cli.Command;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class TrainingSetWriter implements AutoCloseable {
    /**
     * @param path
     * @return
     * @throws IOException
     */
    public static TrainingSetWriter createNew(Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW);
        writer.write("training-set:\n");
        return new TrainingSetWriter(writer);
    }

    /**
     * @param path
     * @return
     * @throws IOException
     */
    public static TrainingSetWriter append(Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.APPEND);

        return new TrainingSetWriter(writer);
    }

    private final BufferedWriter writer;

    private TrainingSetWriter(BufferedWriter writer) {
        Objects.requireNonNull(writer);

        this.writer = writer;
    }

    /**
     * @param input
     * @param command
     * @param feedback
     * @throws IOException
     */
    public void write(String input, Command command, TrainingFeedback feedback) throws IOException {
        writer.write("- input: ");
        writer.write(input);

        writer.write("\n  feedback: ");
        writer.write(feedback.name());

        writer.write("\n  command: ");
        writer.write(command.name());

        writer.write("\n  template: ");
        writer.write(command.template());

        writer.write("\n  rank: ");
        writer.write(command.rank());

        List<Argument> args = command.arguments();
        if (!args.isEmpty()) {
            writer.write("\n  arguments: ");

            for (Argument arg : args) {
                writer.write("\n  - name: ");
                writer.write(arg.name());

                writer.write("\n    value: ");
                writer.write(arg.value().orElse(""));
            }
        }

        writer.write("\n");
        writer.flush();
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
