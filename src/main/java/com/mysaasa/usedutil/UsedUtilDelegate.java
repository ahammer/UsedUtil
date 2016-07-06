package com.mysaasa.usedutil;

import java.util.*;

/**
 * This is the actual implementation, in a non-static way
 *
 * This API is not meant to be user facing
 */
class UsedUtilDelegate {
    Map<String, Integer> callCountMap = new HashMap<String, Integer>();

    void allowOnce(String tag, StackTraceElement[] stackTrace) {
        String key = CallKeyGenerator.fromStackTrace(tag, stackTrace);
        incrementCallCountMap(key);
        if (callCountMap.get(key) > 1) throw new RuntimeException("You've specified that you call this only once");
    }

    void logCall(String tag, StackTraceElement[] stackTrace) {
        String key = CallKeyGenerator.fromStackTrace(tag, stackTrace);
        incrementCallCountMap(key);
    }

    boolean isUsed(String tag) {
        if (tag == null) throw new IllegalArgumentException("Tag must not be null");
        for (String key:callCountMap.keySet()) {
            if ((key.startsWith(":") && tag.equals("")) || key.startsWith(tag))
                return false;
        }
        return true;
    }

    String getReport() {
        Set<String> tags = getTags();
        String report = "UsedUtil Runtime Report\n";
        if (tags.size() > 0) {
            report += "Tags Detected: "+tags.toString() +"\n";
        } else {
            report += "No Tags Detected";
        }

        report +="\nUntagged Calls";
        for (String key:callCountMap.keySet()) {
            if (key.startsWith(":")) {
                report += "\n\tKey = "+key.substring(1);
                report += "\n\tCount = "+callCountMap.get(key)+"\n";
            }
        }

        for (String tag:tags) {
            report +="\nTagged Calls: "+tag;
            for (String key:callCountMap.keySet()) {
                if (key.startsWith(tag)) {
                    report += "\n\tKey = "+key.substring(tag.length()+1);
                    report += "\n\tCount = "+callCountMap.get(key)+"\n";
                }
            }

        }
        return report;
    }


    /**
     * Increments the count for a key
     * @param key, the key based on the stacktrace
     */
    private void incrementCallCountMap(String key) {
        Integer count = callCountMap.get(key);
        if (count == null)
            count = 1;
        else
            count++;
        callCountMap.put(key, count);
    }


    /**
     * Iterates over the keys and grabs the tags
     * @return
     */
    private Set<String> getTags() {
        Set<String> keys = callCountMap.keySet();
        Set<String> tags = new HashSet<String>();
        for (String key:keys) {
            if (!key.startsWith(":") && key.contains(":")) {
                String tag = key.split(":")[0];
                if (!tags.contains(tag))tags.add(tag);
            }
        }
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Counts all the usages of a tag
     *
     * @param myTag
     * @return
     */
    public int getUsageCount(String myTag) {
        int count = 0;
        for (String tag:getTags()) {
            if (tag.equals(myTag)) {
                for (String key:callCountMap.keySet()) {
                    if (key.split(":")[0].equals(tag)) count+=callCountMap.get(key);
                }
            }
        }
        return count;
    }

    /**
     * Get's the total usage count
     * @return
     */
    public int getUsageCount() {
        int count = 0;
        for (String key:callCountMap.keySet()) {
            count+=callCountMap.get(key);
        }
        return count;
    }
}
