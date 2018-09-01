/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.Argument;
import com.dopsun.chatbot.cli.Rank;
import com.dopsun.chatbot.cli.sml.TemplatePart.VariablePart;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class Template {
    /**
     * @param context
     * @param template
     * @return
     */
    public static Template compile(SmlParserContext context, String template) {
        return new Template(context, template);
    }

    private static final String VAR_START_TAG = "${";
    private static final String VAR_STOP_TAG = "}";

    private final SmlParserContext context;
    private final String template;
    private final List<TemplatePart> partList = new ArrayList<>();

    private Template(SmlParserContext context, String template) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(template);

        this.context = context;
        this.template = template;

        int index = 0;
        while (index < template.length()) {
            String partName = null;

            int startPos = template.indexOf(VAR_START_TAG, index);
            if (startPos < 0) {
                partName = template.substring(index).trim();
                partList.add(new TemplateConstPart(context, partName));
                break;
            }

            partName = template.substring(index, startPos).trim();
            partList.add(new TemplateConstPart(context, partName));

            index = startPos + VAR_START_TAG.length();

            int stopPos = template.indexOf(VAR_STOP_TAG, index);
            if (stopPos < 0) {
                throw new RuntimeException("Matching stop tag not found at index " + index);
            }

            partName = template.substring(index, stopPos).trim();
            partList.add(new VariablePart(partName));

            index = stopPos + VAR_STOP_TAG.length();
        }
    }

    /**
     * @param commandText
     * @return
     */
    public Optional<MatchResult> match(String commandText) {
        Objects.requireNonNull(commandText);

        RankCalculator rankCalc = new RankCalculator(context.matcherCost());

        int index = 0;

        List<Argument> argList = new ArrayList<>();

        VariablePart lastVarPart = null;

        boolean matched = true;
        for (TemplatePart part : partList) {
            if (index >= commandText.length()) {
                break;
            }

            if (part instanceof TemplateConstPart) {
                TemplateConstPart cpart = (TemplateConstPart) part;
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

        MatchResult matchResult = new MatchResult(rankCalc.rank(), argList);
        return Optional.of(matchResult);
    }

    /**
     * @return the template
     */
    public String template() {
        return template;
    }

    public static class MatchResult {
        private final Rank rank;
        private final List<Argument> argList;

        public MatchResult(Rank rank, List<Argument> argList) {
            Objects.requireNonNull(argList);

            this.rank = rank;
            this.argList = argList;
        }

        /**
         * @return the rank
         */
        public Rank rank() {
            return rank;
        }

        /**
         * @return the argList
         */
        public List<Argument> argList() {
            return argList;
        }
    }

    private static class CliArgumentImpl implements Argument {
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