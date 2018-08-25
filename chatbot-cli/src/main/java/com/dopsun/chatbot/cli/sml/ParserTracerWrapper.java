/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

import java.util.Objects;
import java.util.Optional;

import com.dopsun.chatbot.cli.ParserTracer;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class ParserTracerWrapper {
    private final Optional<ParserTracer> listener;

    ParserTracerWrapper(Optional<ParserTracer> listener) {
        Objects.requireNonNull(listener);

        this.listener = listener;
    }

    /**
     * @return
     */
    public boolean isEnabled() {
        return listener.isPresent();
    }

    /**
     * @param source
     * @param message
     */
    public void log(Object source, String message) {
        if (listener.isPresent()) {
            listener.get().trace(source.getClass().getSimpleName() + ":: " + message);
        }
    }

    /**
     * @param source
     * @param fmt
     * @param args
     */
    public void log(Object source, String fmt, Object... args) {
        if (listener.isPresent()) {
            String msg = String.format(fmt, args);
            listener.get().trace(source.getClass().getSimpleName() + ":: " + msg);
        }
    }

    /**
     * @param source
     * @param methodName
     * @param arg
     */
    public void enterMethod(Object source, String methodName, Object arg) {
        if (listener.isPresent()) {
            String msg = String.format("%1$s:: %2$s(\"%3$s\") - entered.",
                    source.getClass().getSimpleName(), methodName, arg);
            listener.get().trace(msg);
        }
    }

    /**
     * @param source
     * @param methodName
     * @param arg
     */
    public void exitMethod(Object source, String methodName, Object arg) {
        if (listener.isPresent()) {
            String msg = String.format("%1$s:: %2$s(\"%3$s\") - exited.",
                    source.getClass().getSimpleName(), methodName, arg);
            listener.get().trace(msg);
        }
    }
}
