package com.trmsys.fpp.library.vaas;

import org.junit.Test;

import com.trmsys.fpp.library.testframework.unit.NonRegScriptTest;

public class SampleTest extends NonRegScriptTest {
    @Override protected String getScriptName() {
        return "SampleScript_templ";
    }

    @Test public void testcase() throws Exception {
        runTestFromFile("testcase.json");
    }
}
