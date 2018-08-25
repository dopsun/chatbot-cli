/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

import java.util.List;
import java.util.Map;

/**
 * In Command Line Interface (CLI), user inputs will be translated into commands.
 * 
 * @author Dop Sun
 * @since 1.0.0
 */
public interface Command {
    /**
     * @return name of command.
     */
    String name();

    /**
     * One command can have multiple templates. This value will help identify which one was actually
     * selected.
     * 
     * @return template matched for this command.
     */
    String template();

    /**
     * Arguments of this command, with the sequence it's defined in {@link #template()}. Using Java
     * 8 Streams APIs to convert to map if needed.
     * 
     * <p>
     * For example:
     * </p>
     * 
     * <pre>
     * arguments().stream().collect(Collectors.toMap(Argument::name, Function.identity()));
     * </pre>
     * 
     * <p>
     * <b>Design considerations</b>
     * <ul>
     * <li>No conversion provided, as it creates a new instance of {@link Map} every time a map is
     * requested. Leave to consumer decided when a conversion is required.</li>
     * <li>{@link List} is chosen, instead of map, with key as String, as map will cause confusion
     * about what's value of key.Sequence of arguments retained is a by product.</li>
     * </ul>
     * </p>
     * 
     * @return
     */
    List<Argument> arguments();

    /**
     * Command is in higher rank if {@link #rank()} is smaller.
     * 
     * @return rank of this command.
     */
    Rank rank();
}
