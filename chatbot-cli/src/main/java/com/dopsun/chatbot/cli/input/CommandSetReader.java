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
import java.util.stream.Stream;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public final class CommandSetReader {
    /**
     * 
     */
    public CommandSetReader() {

    }

    /**
     * @param path
     * @return
     */
    public CommandSet read(Path path) {
        Objects.requireNonNull(path);

        List<String> commandList = new ArrayList<>(); // keep sequence.
        Map<String, List<String>> map = new HashMap<>();

        List<CommandItem> commandItems = new ArrayList<>();

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
            e.printStackTrace();
        }

        for (String commandName : commandList) {
            List<String> templates = map.get(commandName);
            CommandItem dataItem = new CommandItemImpl(commandName, templates);
            commandItems.add(dataItem);
        }

        CommandSetImpl result = new CommandSetImpl(commandItems);
        return result;
    }

    static class CommandSetImpl implements CommandSet {
        private final List<CommandItem> items;

        public CommandSetImpl(List<CommandItem> items) {
            Objects.requireNonNull(items);

            this.items = items;
        }

        @Override
        public List<CommandItem> items() {
            return items;
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
