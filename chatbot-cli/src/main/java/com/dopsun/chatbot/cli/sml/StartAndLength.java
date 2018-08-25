/*
 * Copyright (c) 2018 Dop Sun. All rights reserved.
 */

package com.dopsun.chatbot.cli.sml;

/**
 * @author Dop Sun
 * @since 1.0.0
 */
final class StartAndLength {
    /**
     * 
     */
    public static final StartAndLength NOT_FOUND = new StartAndLength();

    private final int start;
    private final int length;

    /**
     * 
     */
    private StartAndLength() {
        this.start = -1;
        this.length = -1;
    }

    /**
     * @return
     */
    public boolean isNotFound() {
        return this.start < 0;
    }

    /**
     * @param start
     * @param length
     */
    public StartAndLength(int start, int length) {
        this.start = start;
        this.length = length;
    }

    /**
     * @return
     */
    public int start() {
        return start;
    }

    /**
     * @return
     */
    public int length() {
        return length;
    }

    /**
     * @return
     */
    public int stop() {
        return start + length;
    }

    /**
     * @param count
     * @return
     */
    public StartAndLength offset(int count) {
        return new StartAndLength(start + count, length);
    }

    /**
     * @param another
     * @return
     */
    public StartAndLength merge(StartAndLength another) {
        if (start < another.start) {
            return new StartAndLength(start, another.stop() - start);
        } else {
            return new StartAndLength(another.start, this.stop() - another.start);
        }
    }
}
