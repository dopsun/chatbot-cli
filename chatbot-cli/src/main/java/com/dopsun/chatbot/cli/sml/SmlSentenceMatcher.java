/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import com.dopsun.chatbot.cli.Argument;
import com.dopsun.chatbot.cli.Command;
import com.dopsun.chatbot.cli.ext.WordMatcher;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class SmlSentenceMatcher {
    /**
     * @param sentence
     * @return
     */
    static List<WordAndLocation> splitSentence(String sentence) {
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

    private final SmlParserContext context;
    private final String commandName;
    private final String template;
    private final List<Part> partList = new ArrayList<>();

    /**
     * @param context
     * @param commandName
     * @param template
     */
    public SmlSentenceMatcher(SmlParserContext context, String commandName, String template) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(commandName);
        Objects.requireNonNull(template);

        this.context = context;
        this.commandName = commandName;
        this.template = template;

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
     */
    public Optional<Command> parse(String commandText) {
        Objects.requireNonNull(commandText);

        RankCalculator rankCalc = new RankCalculator(context.matcherCost());

        int index = 0;

        List<Argument> argList = new ArrayList<>();

        VariablePart lastVarPart = null;

        boolean matched = true;
        for (Part part : partList) {
            if (index >= commandText.length()) {
                break;
            }

            if (part instanceof ConstantPart) {
                ConstantPart cpart = (ConstantPart) part;
                StartAndLength startAndLength = cpart.find(rankCalc, commandText, index);
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

        CliCommandImpl cliCommand = new CliCommandImpl(commandName, template, rankCalc.rank(),
                argList);
        return Optional.of(cliCommand);
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

    final class ConstantPart extends Part {
        final List<WordMatcher> wordMatchList = new ArrayList<>();

        public ConstantPart(String name) {
            super(name);

            String[] wordArray = name.split(" ");
            for (String word : wordArray) {
                String temp = word.trim().toLowerCase();
                if (temp.length() > 0) {
                    wordMatchList.add(context.wordMatcherFactory().compile(temp));
                }
            }
        }

        public StartAndLength find(RankCalculator rankCalc, String input, int fromIndex) {
            Objects.requireNonNull(input);

            String inputLower = input.substring(fromIndex).toLowerCase();
            List<WordAndLocation> wordList = splitSentence(inputLower);

            WordAndLocation first = null;
            WordAndLocation last = null;

            for (WordMatcher wordMatcher : wordMatchList) {
                while (wordList.size() > 0) {
                    OptionalInt optDiscount = wordMatcher.match(context.matcherCost(),
                            wordList.get(0).word());
                    if (optDiscount.isPresent()) {
                        if (first == null) {
                            first = wordList.get(0);
                        } else {
                            last = wordList.get(0);
                        }

                        rankCalc.discount(optDiscount.getAsInt());

                        wordList.remove(0);
                        break;
                    }

                    wordList.remove(0);
                    rankCalc.skipConstantWord(1);
                }

                if (wordList.size() == 0) {
                    return StartAndLength.NOT_FOUND;
                }
            }

            if (last == null) {
                return first.location().offset(fromIndex);
            } else {
                return first.location().merge(last.location()).offset(fromIndex);
            }
        }
    }

    static class VariablePart extends Part {
        public VariablePart(String name) {
            super(name);
        }
    }

    static class CliArgumentImpl implements Argument {
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
