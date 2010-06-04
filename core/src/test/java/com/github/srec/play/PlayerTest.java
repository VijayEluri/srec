package com.github.srec.play;

import com.github.srec.testng.AbstractSRecTestNGTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

@Test
public class PlayerTest extends AbstractSRecTestNGTest {
    private static final String TEST_SCRIPT_DIR = "src/test/resources/";

    public PlayerTest() {
        super(TEST_SCRIPT_DIR, "com.github.srec.ui.TestForm", new String[0]);
    }

    public void test() throws IOException {
        runTest("test_form.xml");
    }

    public void testError() throws IOException {
        Player p = runTest("test_form_error.xml", false);
        p.printErrors();
        assertEquals(p.getErrors().size(), 2);
        assertEquals(p.getErrors().get(0).getLineNumber(), 18);
        assertEquals(p.getErrors().get(1).getLineNumber(), 26);
    }

    public void testMethod() throws IOException {
        runTest("test_form_method_call.xml");
    }

    public void testMethodDeep() throws IOException {
        runTest("test_form_method_deep.xml");
    }

    public void testIf() throws IOException {
        runTest("if.xml");
    }

    public void testWhile() throws IOException {
        runTest("while.xml");
    }
}
