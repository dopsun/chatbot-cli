/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

import com.dopsun.chatbot.cli.ext.WordMatcher;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class TemplateConstPart extends TemplatePart {
    private final SmlParserContext context;
    final List<WordMatcher> wordMatchList = new ArrayList<>();

    /**
     * @param context
     * @param name
     */
    public TemplateConstPart(SmlParserContext context, String name) {
        super(name);
        this.context = context;

        String[] wordArray = name.split(" ");
        for (String word : wordArray) {
            String temp = word.trim().toLowerCase();
            if (temp.length() > 0) {
                wordMatchList.add(context.wordMatcherFactory().compile(temp));
            }
        }
    }

    /**
     * @param rankCalc
     * @param input
     * @param fromIndex
     * @return
     */
    public StartAndLength find(RankCalculator rankCalc, String input, int fromIndex) {
        Objects.requireNonNull(input);

        String inputLower = input.substring(fromIndex).toLowerCase();
        List<WordAndLocation> wordList = WordAndLocation.splitSentence(inputLower);

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