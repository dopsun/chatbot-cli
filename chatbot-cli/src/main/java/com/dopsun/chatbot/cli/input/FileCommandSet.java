/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class FileCommandSet implements CommandSet {
    private final Path path;

    /**
     * @param path
     */
    public FileCommandSet(Path path) {
        Objects.requireNonNull(path);

        this.path = path;
    }

    @Override
    public void accept(Consumer<CommandItem> itemVisitor) {
        List<String> commandList = new ArrayList<>(); // keep sequence.
        Map<String, List<String>> map = new HashMap<>();

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> {
                if (line.startsWith("#")) {
                    return;
                }

                int pos = line.indexOf('=');
                if (pos < 0) {
                    return;
                }

                String key = line.substring(0, pos).trim();
                String template = line.substring(pos + 1).trim();

                List<String> list = map.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                    map.put(key, list);
                    commandList.add(key);
                }
                list.add(template);
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read command.", e);
        }

        for (String commandName : commandList) {
            List<String> templates = map.get(commandName);
            CommandItem commandItem = new CommandItemImpl(commandName, templates);

            itemVisitor.accept(commandItem);
        }
    }

    static class CommandItemImpl implements CommandItem {
        private final String commandName;
        private final List<String> templates;

        /**
         * @param commandName
         * @param templates
         */
        public CommandItemImpl(String commandName, final List<String> templates) {
            Objects.requireNonNull(commandName);
            Objects.requireNonNull(templates);

            this.commandName = commandName;
            this.templates = templates;
        }

        @Override
        public String commandName() {
            return commandName;
        }

        @Override
        public List<String> templates() {
            return templates;
        }

    }
}
