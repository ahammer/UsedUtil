package com.mysaasa.usedutil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.mysaasa.usedutil.UsedUtil.*;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test Cases to Demonstrate simple usage of the tool
 */
public class UsedUtilTest {

    @Before
    public void setup() {
        UsedUtil.reset();
    }

    @After
    public void debugOutput() {
        UsedUtil.printReport();
    }

    /**
     * We are testing that calls to Log() and Log(tag) properly reflect in calls to isUsed()
     */
    @Test
    public void testIsDead() throws Exception {
        MyTestClass m = new MyTestClass();

        //Verify the untagged state
        assertTrue(isUsed());
        m.doSomething();
        assertFalse(isUsed());

        //Verify a tagged state
        assertTrue(isUsed("myTag"));
        m.doSomethingTagged();
        assertFalse(isUsed("myTag"));
    }

    /**
     * Verify that call once functionality throws a exception when installed
     */
    @Test(expected = RuntimeException.class)
    public void testAllowCallOnceThrows() {
        MyTestClass m = new MyTestClass();
        m.initialize();
        m.initialize();
    }

    /**
     * Verify that CallOnce doesn't cause a problem when you call it once
     */
    @Test
    public void testAllowCallOnceValid() {
        MyTestClass m = new MyTestClass();
        m.initialize();
        assertEquals(UsedUtil.getUsageCount(), 1);
    }

    /**
     * Count usage per tag and overall usage
     */
    @Test
    public void testCount() {
        MyTestClass m = new MyTestClass();
        m.doSomethingTagged();
        m.doSomethingTagged();
        m.doSomethingTagged();
        m.doSomethingElseTagged();
        m.doSomethingElseTagged();
        assertEquals(UsedUtil.getUsageCount("myTag"), 3);
        assertEquals(UsedUtil.getUsageCount("myTag2"), 2);
        assertEquals(UsedUtil.getUsageCount(), 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLogNullTag() {
        log(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllowOnceNullTag() {
        allowOnce(null);
    }

    /**
     * This is a Test Class that helps with the tests.
     * It integrates the API for testing
     */
    class MyTestClass {
        public MyTestClass() {}

        public void doSomething() {
            log();
        }

        public void doSomethingTagged() {
            log("myTag");
        }
        public void doSomethingElseTagged() {
            log("myTag2");
        }

        public void initialize() {
            allowOnce();
        }
    }

}