/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli;

/**
 * Logger to trace the {@link CliParser} internal work.
 * 
 * <p>
 * <code>chatbot-cli</code> is <i>NOT</i> depends on any third party libraries by default, including
 * popular logging frameworks. So, if required, application can provide this logger while building
 * {@link CliParser}.
 * </p>
 * <p>
 * This can easily adapted to an actual logger. And if this is the case, it's recommended to create
 * a dedicated log instance using {@link CliParser} class. For example, with slf4j:
 * </p>
 * 
 * <pre>
 * private static final Logger log = LoggerFactory.getLogger(CliParser.class);
 * </pre>
 * 
 * @author Dop Sun
 * @since 1.0.0
 */
@FunctionalInterface
public interface TraceListener {
    /**
     * @param message
     */
    void trace(String message);
}
