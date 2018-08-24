/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.ext;

/**
 * @author Dop Sun
 * @param <T>
 * @since 1.0.0
 */
public interface WordMatcherFactory<T extends WordMatcher> {

    /**
     * @param template
     * @return
     */
    T compile(String template);
}
