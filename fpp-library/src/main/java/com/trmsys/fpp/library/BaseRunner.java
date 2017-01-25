package com.trmsys.fpp.library;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggerRepository;

public class BaseRunner {
	
	 public static void setupLogging() {
	        final org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
	        if (!rootLogger.getAllAppenders().hasMoreElements()) {
	            rootLogger.setLevel(Level.OFF);
	            rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%d{HH:mm:ss:SSS} %-5p %c{1} %x - %m%n")));

	            final LoggerRepository logRepo = rootLogger.getLoggerRepository();
	            logRepo.getLogger("com.trmsys.fpp").setLevel(Level.OFF);
	            logRepo.getLogger("com.trmsys.fpp.script").setLevel(Level.OFF);
	            logRepo.getLogger("com.trmsys.fpp.script.ast.parameter.PopulationStatistics").setLevel(Level.OFF); // Allocation
	            logRepo.getLogger("com.trmsys.fpp.engine.generic.groovy.GroovyProcessor").setLevel(Level.OFF); // Displays number of function calls in groovy mode
	            logRepo.getLogger("com.trmsys.fpp.clutilities.chrono.PerformanceChrono").setLevel(Level.OFF);
	        }
	    }

}
