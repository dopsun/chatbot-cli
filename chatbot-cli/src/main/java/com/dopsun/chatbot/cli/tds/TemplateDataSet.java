/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.tds;

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
public class TemplateDataSet implements DataSet {
    private final List<DataItem> dataItems = new ArrayList<>();

    /**
     * @param path
     */
    public TemplateDataSet(Path path) {
        Objects.requireNonNull(path);

        List<String> commandList = new ArrayList<>(); // keep sequence.
        Map<String, List<String>> map = new HashMap<>();

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> {
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
            DataItem dataItem = new TemplateDataItem(commandName, templates);
            dataItems.add(dataItem);
        }
    }

    @Override
    public List<DataItem> items() {
        return dataItems;
    }
}
