package com.trmsys.fpp.library.vaas;

import static org.junit.Assert.assertEquals;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.trmsys.fpp.library.testframework.BaseTest;

public class RunnerTest extends BaseTest {

    @Test
    public void test() throws Exception {
        final String [] params = new String[] {"--script", "SampleScriptLocal",
                            "--scenarioData",  "src/main/resources/com/trmsys/fpp/library/marketData/SampleMarketData.json",
                            "--documents", "src/main/resources/com/trmsys/fpp/library/deals/SampleDealLocal.json",
                            "--dates", "2010-10-10"};
        final CustomPrintStream customPrint = new CustomPrintStream();
        System.setOut(customPrint);
        Runner.main(params);
        assertEquals("[42.0]", customPrint.values.get(0));
        assertEquals("Traces : ", customPrint.values.get(1));
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    public class CustomPrintStream extends PrintStream {

        private List<String> values = new ArrayList<>();

        public CustomPrintStream() {
            super(System.out);
        }

        @Override
        public void println(String v){
            values.add(v);
        }
   }

}


