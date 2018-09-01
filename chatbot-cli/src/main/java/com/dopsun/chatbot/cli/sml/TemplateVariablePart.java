/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Pattern;

import com.dopsun.chatbot.cli.Argument;

/**
 * Variable can be defined as <code>${name|attr1=value1, attr2=value2}</code>
 * 
 * @author Dop Sun
 * @since 1.0.0
 */
final class TemplateVariablePart extends TemplatePart {
    private static final String ATTR_START = "|";
    private static final String ATTR_SEPARATOR = ",";
    private static final String ATTR_NAME_VALUESEPARATOR = "=";

    private static final String MIN_CHAR = "min-char";
    private static final String MAX_CHAR = "max-char";
    private static final String MIN_WORD = "min-word";
    private static final String MAX_WORD = "max-word";
    private static final String PATTERN = "pattern";
    private static final String PREFIX = "prefix";

    public static TemplateVariablePart parse(String text) {
        Objects.requireNonNull(text);

        String name = text;
        Map<String, String> attrMap = new HashMap<>();

        String attrListText = null;

        int attrStartIndex = text.indexOf(ATTR_START);

        if (attrStartIndex > 0) {
            name = text.substring(0, attrStartIndex).trim();
            attrListText = text.substring(attrStartIndex + 1).trim();
        }

        if (attrListText != null) {
            String[] nameAndValueList = attrListText.split(ATTR_SEPARATOR);
            for (String nameAndValue : nameAndValueList) {
                String trimmed = nameAndValue.trim();
                String[] nameValuePair = trimmed.split(ATTR_NAME_VALUESEPARATOR);

                attrMap.put(nameValuePair[0].trim(), nameValuePair[1].trim());
            }
        }

        return new TemplateVariablePart(name, attrMap);
    }

    private final Map<String, String> attrMap;

    /**
     * @param name
     * @param attributeMap
     */
    public TemplateVariablePart(String name, Map<String, String> attributeMap) {
        super(name);

        this.attrMap = attributeMap;
    }

    public Optional<Argument> match(String value) {
        String trimmed = value.trim();

        OptionalInt optMinChar = getMinChar();
        if (optMinChar.isPresent() && trimmed.length() < optMinChar.getAsInt()) {
            return Optional.empty();
        }

        OptionalInt optMaxChar = getMaxChar();
        if (optMaxChar.isPresent() && trimmed.length() > optMaxChar.getAsInt()) {
            return Optional.empty();
        }

        OptionalInt optMinWord = getMinWord();
        OptionalInt optMaxWord = getMaxWord();
        if (optMinWord.isPresent() || optMaxWord.isPresent()) {
            String[] words = trimmed.split(" ");
            long wordCount = Arrays.stream(words).filter(w -> !w.trim().isEmpty()).count();

            if (optMinWord.isPresent() && wordCount < optMinWord.getAsInt()) {
                return Optional.empty();
            }
            if (optMaxWord.isPresent() && wordCount > optMaxWord.getAsInt()) {
                return Optional.empty();
            }
        }

        Optional<String> optPrefix = getPrefix();
        if (optPrefix.isPresent() && !trimmed.startsWith(optPrefix.get())) {
            return Optional.empty();
        }

        Optional<String> optPattern = getPattern();
        if (optPattern.isPresent() && !Pattern.matches(optPattern.get(), trimmed)) {
            return Optional.empty();
        }

        if (trimmed.isEmpty()) {
            return Optional.of(new CliArgumentImpl(this.name()));
        } else {
            return Optional.of(new CliArgumentImpl(this.name(), trimmed));
        }
    }

    /**
     * @return
     */
    public OptionalInt getMinChar() {
        return getAsInteger(MIN_CHAR);
    }

    /**
     * @return
     */
    public OptionalInt getMaxChar() {
        return getAsInteger(MAX_CHAR);
    }

    /**
     * @return
     */
    public OptionalInt getMinWord() {
        return getAsInteger(MIN_WORD);
    }

    /**
     * @return
     */
    public OptionalInt getMaxWord() {
        return getAsInteger(MAX_WORD);
    }

    /**
     * @return
     */
    public Optional<String> getPrefix() {
        return Optional.ofNullable(attrMap.get(PREFIX));
    }

    /**
     * @return
     */
    public Optional<String> getPattern() {
        return Optional.ofNullable(attrMap.get(PATTERN));
    }

    private OptionalInt getAsInteger(String key) {
        String minCharText = attrMap.get(key);
        if (minCharText == null) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(Integer.getInteger(minCharText));
    }

    private static class CliArgumentImpl implements Argument {
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