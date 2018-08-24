/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.ext;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
public interface WordMatcherFactory {

    /**
     * @param template
     * @return
     */
    WordMatcher compile(String template);
}
