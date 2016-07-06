package com.mysaasa.usedutil;

import java.util.*;

/**
 * This turns a callstack into a key
 *
 * This key is then used to identify integration points
 */
public class CallKeyGenerator {

    /**
     * Generates a Key from a StackTrace array, to identify the location of a call
     *
     * @param tag   A tag that will be included in the Key
     * @param stackTrace    The stacktrace provided by UsedUtil calls
     * @return  The Key, identifying the Tag and Call position
     */
    public static String fromStackTrace(String tag, StackTraceElement[] stackTrace) {
        if (tag == null || stackTrace == null) throw new IllegalArgumentException("Arguments can not be null");
        List<StackTraceElement> trace = fixStackTrace(stackTrace);
        String key = buildKey(trace);
        return tag+":"+key;
    }

    /**
     * Fixes a stack trace to show the exact point that the user has called
     *
     * @param stackTrace    The stack trace from the place UsedUtil is called
     * @return  The fixed stack trace, as a UnmodifiableList instead of a array
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
     * @return  The stack trace as a String key, to identify this particulary call location
     */
    private static String buildKey(List<StackTraceElement> stackTrace) {
        StackTraceElement element = stackTrace.get(0);
        return element.toString();
    }
}
