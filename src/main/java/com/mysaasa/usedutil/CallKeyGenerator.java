package com.mysaasa.usedutil;

import java.util.*;

/**
 * This turns a callstack into a key
 *
 * This key is then used to identify integration points
 */
public class CallKeyGenerator {
    public static String fromStackTrace(String tag, StackTraceElement[] stackTrace) {
        if (tag == null || stackTrace == null) throw new IllegalArgumentException("Arguments can not be null");
        List<StackTraceElement> trace = fixStackTrace(stackTrace);
        String key = buildKey(trace);
        return tag+":"+key;
    }

    /**
     * Converts the Array to a List, and truncates the first 2 Levels
     * (2 Levels being the getStackTrace call itself, and the static helper,
     * leaving the line you actually called the API from).
     */
    private static List<StackTraceElement> fixStackTrace(StackTraceElement[] stackTrace) {
        List<StackTraceElement> mList = new ArrayList();
        for (int i=2;i<stackTrace.length;i++) {
            mList.add(stackTrace[i]);
        }
        return Collections.unmodifiableList(mList);
    }

    /**
     * Builds the key from a fixed stack trace
     *
     * The key is simply the location of the function call.
     *
     * @param stackTrace
     * @return
     */
    private static String buildKey(List<StackTraceElement> stackTrace) {
        StackTraceElement element = stackTrace.get(0);
        return element.toString();
    }
}
