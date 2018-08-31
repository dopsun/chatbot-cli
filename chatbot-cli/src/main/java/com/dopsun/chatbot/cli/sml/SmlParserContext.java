/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import com.dopsun.chatbot.cli.MatcherCost;
import com.dopsun.chatbot.cli.ext.WordMatcherFactory;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
interface SmlParserContext {
    /**
     * @return
     */
    ParserTracerWrapper tracer();

    /**
     * @return
     */
    WordMatcherFactory wordMatcherFactory();

    /**
     * @return
     */
    MatcherCost matcherCost();
}
