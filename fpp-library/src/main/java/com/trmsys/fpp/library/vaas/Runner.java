package com.trmsys.fpp.library.vaas;

import com.trmsys.fpp.library.runner.ScriptRunner;
import com.trmsys.fpp.library.BaseRunner;

public class Runner extends BaseRunner {
	
	public static void main (String [] args) throws Exception {
		setupLogging();
		ScriptRunner.main(args);
	}

}
