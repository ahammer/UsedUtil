package com.mysaasa.usedutil;

/**
 * UsedUtil
 * A tool for finding dead code or enforcing code you intend to only run once.
 *
 * User Stories:
 * - As a developer I would like to be able to find potentially dead code
 * - As a developer I want to catch runtime errors such as duplicate calls
 * - As a developer I want a log of my applications usage
 * - As a developer, I want to be able to generate reports from this data
 * - As a developer I'd like to do this with minimal integration/typing
 *
 * The Real Story
 *
 * Let's face it, we all work on junk code sometimes. When you do, this can
 * come in handy in all sorts of situations.
 *
 * Case #1: You want to verify that a suspected block of code is dead or not.
 *   - Place UsedUtil.log("yourtag") throughout your code.
 *   - Run your expected code paths
 *   - Check result with UsedUtil.isUsed("yourtag")
 *
 * Case #2: You want to ensure a function only runs once
 *   - Use UsedUtil.allowOnce() in your function
 *   - If UsedUtil is enabled, it will throw the second time you call that method
 *
 */
public class UsedUtil {
    /**
     * Instance of the object. I do this for quality reasons
     * (implementation is non-static, interface is static)
     */
    static private UsedUtilDelegate instance = new UsedUtilDelegate();


    /**
     * Global Enable flag. If this is false, everything will be disabled (except re-enabling)
     */
    private volatile static boolean mGloballyEnabled = true;


    /**
     * Private Constructor, to prevent direct access
     */
    private UsedUtil(){}


    /**
     * Log a call. This will make future calls to isUsed() false.
     */
    public static void log() {
        if (!mGloballyEnabled) return;
        instance.logCall("", Thread.currentThread().getStackTrace());
    }


    /**
     * Log a tagged call. This will make future calls to isUsed(String tag) return false.
     */
    public static void log(String tag) {
        if (!mGloballyEnabled) return;
        if (tag == null) throw new IllegalArgumentException("Tag can not be null");
        instance.logCall(tag, Thread.currentThread().getStackTrace());
    }


    /**
     * Allow this call once, no tag mode.
     *
     * If any particular instance of this is called twice, you will get an exception.
     */
    public static void allowOnce() {
        if (!mGloballyEnabled) return;
        instance.allowOnce("", Thread.currentThread().getStackTrace());
    }


    /**
     * Allow this call once with this tag. Otherwise we will throw an exception.
     *
     * @param tag The tag for this call. Can be any string.
     *            If you want to call initialize once per class for example,
     *            you can use hashcode, if your hashcode isn't broken
     *            because mutable state
     */
    public static void allowOnce(String tag) {
        if (!mGloballyEnabled) return;
        instance.allowOnce(tag, Thread.currentThread().getStackTrace());
    }


    public static String getReport() {
        return instance.getReport();
    }

    public static void printReport() {
        System.out.println(getReport());
    }

    public boolean isEnabled() {
        return mGloballyEnabled;
    }


    public void setEnabled(boolean mGloballyEnabled) {
        this.mGloballyEnabled = mGloballyEnabled;
    }

    public static boolean isUsed() {
        return instance.isUsed("");
    }

    public static boolean isUsed(String tag) {
        return instance.isUsed(tag);
    }


    public static void reset() {
        instance = new UsedUtilDelegate();
    }

    public static int getUsageCount(String myTag) {
        return instance.getUsageCount(myTag);
    }

    public static int getUsageCount() {
        return instance.getUsageCount();
    }
}

