/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import com.dopsun.chatbot.cli.Argument;
import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.Rank;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class FileTrainingSet implements TrainingSet {
    private final Path path;

    /**
     * @param path
     */
    public FileTrainingSet(Path path) {
        Objects.requireNonNull(path);

        this.path = path;
    }

    @Override
    public void accept(Consumer<TrainingItem> itemVisitor) {
        try {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                read(reader, itemVisitor);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read the training set.", ex);
        }
    }

    private void read(BufferedReader reader, Consumer<TrainingItem> itemVisitor)
            throws IOException {
        String firstline = reader.readLine();
        if (!"training-set:".equals(firstline)) {
            throw new IllegalStateException("Invalid header.");
        }

        TrainingItemBuilder itemBuilder = null;
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            if (line.trim().length() == 0) {
                continue; // ignore empty line
            }

            if (!(line.startsWith("- ") || itemBuilder != null)) {
                throw new IllegalStateException("Invalid item.");
            }

            if (line.startsWith("- ")) {
                line = line.replaceFirst("- ", "  ");

                if (itemBuilder != null) {
                    itemVisitor.accept(itemBuilder.build());
                }

                itemBuilder = new TrainingItemBuilder();
            }

            if (line.startsWith("  input:")) {
                itemBuilder.input = line.substring("  input:".length()).trim();
            } else if (line.startsWith("  command:")) {
                itemBuilder.commandName = line.substring("  command:".length()).trim();
            } else if (line.startsWith("  template:")) {
                itemBuilder.commandTemplate = line.substring("  template:".length()).trim();
            } else if (line.startsWith("  rank:")) {
                itemBuilder.rank = Rank
                        .of(Integer.parseInt(line.substring("  rank:".length()).trim()));
            } else if (line.startsWith("  feedback:")) {
                itemBuilder.feedback = TrainingFeedback
                        .valueOf(line.substring("  feedback:".length()).trim());
            } else if (line.startsWith("  arguments:")) {

            } else if (line.startsWith("  - name:")) {
                String argName = line.substring("  - name:".length()).trim();

                if (itemBuilder.lastArgumentName != null) {
                    itemBuilder.arguments.add(new CliArgumentImpl(itemBuilder.lastArgumentName));
                }

                itemBuilder.lastArgumentName = argName;
            } else if (line.startsWith("  - value:")) {
                String argValue = line.substring("  - value:".length()).trim();

                if (itemBuilder.lastArgumentName == null) {
                    throw new RuntimeException("Invalid training data set.");
                }

                itemBuilder.arguments
                        .add(new CliArgumentImpl(itemBuilder.lastArgumentName, argValue));
                itemBuilder.lastArgumentName = null;
            }
        }

        if (itemBuilder != null) {
            itemVisitor.accept(itemBuilder.build());
        }
    }

    static class TrainingItemBuilder {
        private String input;
        private String commandName;
        private String commandTemplate;
        private List<Argument> arguments = new ArrayList<>();
        private Rank rank;
        private TrainingFeedback feedback;

        private String lastArgumentName;

        public TrainingItem build() {
            if (lastArgumentName != null) {
                arguments.add(new CliArgumentImpl(lastArgumentName));
            }

            return new TrainingItemImpl(this);
        }
    }

    static class TrainingItemImpl implements TrainingItem {
        private final String input;
        private final Command command;
        private final TrainingFeedback feedback;

        public TrainingItemImpl(TrainingItemBuilder builder) {
            Objects.requireNonNull(builder);

            this.input = Objects.requireNonNull(builder.input);
            this.command = new CliCommandImpl(builder);
            this.feedback = Objects.requireNonNull(builder.feedback);
        }

        @Override
        public String input() {
            return input;
        }

        @Override
        public Command command() {
            return command;
        }

        @Override
        public TrainingFeedback feedback() {
            return feedback;
        }
    }

    static class CliCommandImpl implements Command {

        private final String name;
        private final String template;
        private final Rank rank;
        private final List<Argument> arguments;

        public CliCommandImpl(TrainingItemBuilder builder) {
            Objects.requireNonNull(builder);

            this.name = Objects.requireNonNull(builder.commandName);
            this.template = Objects.requireNonNull(builder.commandTemplate);
            this.rank = builder.rank;
            this.arguments = Objects.requireNonNull(builder.arguments);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String template() {
            return template;
        }

        @Override
        public Rank rank() {
            return rank;
        }

        @Override
        public List<Argument> arguments() {
            return arguments;
        }

    }

    static class CliArgumentImpl implements Argument {
        private final String name;
        private final Optional<String> value;

        public CliArgumentImpl(String name) {
            Objects.requireNonNull(name);

            this.name = name;
            this.value = Optional.empty();
        }

        public CliArgumentImpl(String name, String value) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(value);

            this.name = name;
            this.value = Optional.of(value);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Optional<String> value() {
            return value;
        }

    }

}