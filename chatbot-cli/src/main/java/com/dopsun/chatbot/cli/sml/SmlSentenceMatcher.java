/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.CliArgument;
import com.dopsun.chatbot.cli.CliParserException;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public class SmlSentenceMatcher {
    /**
     * @param sentence
     * @return
     */
    public static List<WordAndLocation> splitSentence(String sentence) {
        Objects.requireNonNull(sentence);

        List<WordAndLocation> list = new ArrayList<>();

        String temp = sentence.toLowerCase();
        int wordStart = -1;
        for (int index = 0; index < temp.length(); index++) {
            char c = temp.charAt(index);
            if (wordStart < 0) {
                if (Character.isWhitespace(c)) {
                    continue;
                }

                wordStart = index;
            } else {
                if (Character.isWhitespace(c)) {
                    StartAndLength location = new StartAndLength(wordStart, index - wordStart);
                    String word = temp.substring(wordStart, index);
                    list.add(new WordAndLocation(word, location));

                    wordStart = -1;
                }
            }
        }

        if (wordStart >= 0) {
            StartAndLength location = new StartAndLength(wordStart, temp.length() - wordStart);
            String word = temp.substring(wordStart);
            list.add(new WordAndLocation(word, location));
        }

        return list;
    }

    private static final String START_TAG = "${";
    private static final String STOP_TAG = "}";

    private final List<Part> partList = new ArrayList<>();

    /**
     * @param template
     */
    public SmlSentenceMatcher(String template) {
        Objects.requireNonNull(template);

        int index = 0;
        while (index < template.length()) {
            String partName = null;

            int startPos = template.indexOf(START_TAG, index);
            if (startPos < 0) {
                partName = template.substring(index).trim();
                partList.add(new ConstantPart(partName));
                break;
            }

            partName = template.substring(index, startPos).trim();
            partList.add(new ConstantPart(partName));

            index = startPos + START_TAG.length();

            int stopPos = template.indexOf(STOP_TAG, index);
            if (stopPos < 0) {
                throw new RuntimeException("Matching stop tag not found at index " + index);
            }

            partName = template.substring(index, stopPos).trim();
            partList.add(new VariablePart(partName));

            index = stopPos + STOP_TAG.length();
        }
    }

    /**
     * @param commandText
     * @return
     * @throws CliParserException
     */
    public Optional<List<CliArgument>> parse(String commandText) throws CliParserException {
        Objects.requireNonNull(commandText);

        int index = 0;

        List<CliArgument> argList = new ArrayList<>();

        VariablePart lastVarPart = null;

        boolean matched = true;
        for (Part part : partList) {
            if (index >= commandText.length()) {
                break;
            }

            if (part instanceof ConstantPart) {
                ConstantPart cpart = (ConstantPart) part;
                StartAndLength startAndLength = cpart.find(commandText, index);
                if (startAndLength.start() < 0) {
                    // TODO: logging?
                    matched = false;
                    break;
                }

                if (lastVarPart != null) {
                    String varValue = commandText.substring(index, startAndLength.start()).trim();
                    if (varValue.length() == 0) {
                        argList.add(new CliArgumentImpl(lastVarPart.name, Optional.empty()));
                    } else {
                        argList.add(new CliArgumentImpl(lastVarPart.name, Optional.of(varValue)));
                    }

                    lastVarPart = null;
                }

                index = startAndLength.stop();
            }

            if (part instanceof VariablePart) {
                if (lastVarPart != null) {
                    // TODO: logging?
                    matched = false;
                    break;
                }

                lastVarPart = (VariablePart) part;
            }
        }

        if (lastVarPart != null) {
            String varValue = commandText.substring(index).trim();
            if (varValue.length() == 0) {
                argList.add(new CliArgumentImpl(lastVarPart.name, Optional.empty()));
            } else {
                argList.add(new CliArgumentImpl(lastVarPart.name, Optional.of(varValue)));
            }
        }

        if (!matched) {
            return Optional.empty();
        }

        return Optional.of(argList);
    }

    static abstract class Part {
        final String name;

        protected Part(String name) {
            Objects.requireNonNull(name);

            String temp = name.trim();
            if (temp.length() == 0) {
                throw new IllegalArgumentException("Empty name.");
            }

            this.name = temp;
        }
    }

    static class ConstantPart extends Part {
        final List<SmlWordMatcher> wordMatchList = new ArrayList<>();

        public ConstantPart(String name) {
            super(name);

            String[] wordArray = name.split(" ");
            for (String word : wordArray) {
                String temp = word.trim().toLowerCase();
                if (temp.length() > 0) {
                    wordMatchList.add(new SmlWordMatcher(temp));
                }
            }
        }

        public StartAndLength find(String input, int fromIndex) {
            Objects.requireNonNull(input);

            String inputLower = input.substring(fromIndex).toLowerCase();
            List<WordAndLocation> wordList = splitSentence(inputLower);

            WordAndLocation first = null;
            for (SmlWordMatcher wordMatcher : wordMatchList) {
                while (wordList.size() > 0) {
                    if (wordMatcher.match(wordList.get(0).word())) {
                        if (first == null) {
                            first = wordList.get(0);
                        }
                        break;
                    }

                    wordList.remove(0);

                }

                if (wordList.size() == 0) {
                    return StartAndLength.NOT_FOUND;
                }
            }

            if (first == null) {
                first = wordList.get(0);
            }
            
            if (first == null) {
                return wordList.get(0).location().offset(fromIndex);
            } else {
                if (first == wordList.get(0)) {
                    return wordList.get(0).location().offset(fromIndex);
                } else {
                    return first.location().merge(wordList.get(0).location()).offset(fromIndex);
                }
            }
        }
    }

    static class VariablePart extends Part {
        public VariablePart(String name) {
            super(name);
        }
    }

    static class CliArgumentImpl implements CliArgument {
        private final String name;
        private final Optional<String> value;

        public CliArgumentImpl(String name, Optional<String> value) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(value);

            this.name = name;
            this.value = value;
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