package com.trmsys.fpp.library.che;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.trmsys.fpp.engine.datamanager.DataManagerBuilder;
import com.trmsys.fpp.engine.datamanager.DefaultScenarioMetaDataAccessor;
import com.trmsys.fpp.engine.datamanager.ScenarioValues;
import com.trmsys.fpp.session.impl.ScenarioIdentifier;

import java.io.*;
import java.lang.*;
import java.util.*;


public class Main2 {
    
    public static void main (String [] args) throws Exception {
        
        
        // Create the dates
        List<LocalDate> stepDates = new ArrayList<>();
        stepDates.add(LocalDate.now());
        
        // Create the builder
        DataManagerBuilder builder = new DataManagerBuilder(1, stepDates);
        
        // Create the scenarios
        List<ScenarioValues> scenarios = new ArrayList<>();
        double [] values = new double [1];
        for (int i = 0 ; i < values.length  ; ++i){
            values[i] = i*25;
        }
        
       
        // create the scenarios for the vol surface
         List<ScenarioValues> scenarios_eqvol = new ArrayList<>();
        double [] vol_surf_mat = {0.0, 1.0,7.0, 30.0 , 61.0, 91.0, 183.0,  274.0, 365.0, 731.0, 1096.0, 1826.0, 2557.0, 3653.0, 7305.0};
        double [] vol_surf_str = {0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
        double [] vol_values = {5.0};

            
        for (int i = 0; i< vol_surf_mat.length; i++){
            for (int j = 0; j < vol_surf_str.length; j++){               
                scenarios_eqvol.add(new ScenarioValues(new ScenarioIdentifier("EQ_VOL_SURFACE", Lists.newArrayList("COCA_COLA","EQUITY","USD")), null,vol_surf_mat[i],20*vol_surf_str[j], vol_values));
            }
        }

        // create the scenarios for the yield curve
        List<ScenarioValues> scenarios_yieldcurve = new ArrayList<>();
        double [] yield_curve = {31 ,93 ,186 ,365 ,731, 1095, 1825, 2555, 2650, 7300, 10950};
        double [] ZCR_USD =  {0.24, 0.28,0.36,0.45,0.59,0.71,1.0,1.27,1.46,1.81,2.24};
        for (int j = 0; j  < yield_curve.length; j++){ 
            scenarios.add(new ScenarioValues(new ScenarioIdentifier("YIELD_CURVE", Lists.newArrayList("USD","ZERO_COUPON_RATE")), yield_curve[j],null,null, new double[]{ZCR_USD[j]}));
        }
        
        double[] xval = new double[1];
        for (int i = 0; i<xval.length; i++){
            xval[i]= 20* (i+1);
        }
        
        //  Create the scenario identifiers here
        ScenarioIdentifier asset = new ScenarioIdentifier("ASSET", Lists.newArrayList("PEPSI","EQUITY","USD"));
        scenarios.add(new ScenarioValues(new ScenarioIdentifier("ASSET", Lists.newArrayList("COCA_COLA","EQUITY","USD")), null,null,null, xval));
        
        
        //  Add the scenarios to the builders
        builder.addScenarios(scenarios_eqvol);
        builder.addScenarios(scenarios);
       
       
       System.out.println(builder.toString());
       
       String fileName = "/projects/fpp-library-vaas/fpp-library/src/main/resources/com/trmsys/fpp/library/marketData/gsMarketData2.json";
        
       
        
        
       PrintWriter outputStream = new PrintWriter(fileName);
       
       outputStream.println(builder.toString());
       outputStream.close();
       System.out.println(stepDates.toString());
       System.out.println("George's Project");
        
        //  The MarketData printwriter is created and commented so that you can use it if you want to write to a differnt file easily
       // PrintWriter marketData = createFile("/projects/fpp-library/fpp-script-runner/src/test/resources/com/trmsys/fpp/library/runner/gsMarketData.json");
        
       
    }
    
    private static PrintWriter createFile(String fileName){
        
        try{
            File listOfData = new File(fileName);
            
            PrintWriter infoToWrite = new PrintWriter(
            new BufferedWriter(new FileWriter(listOfData)));
        } catch(IOException e){
            System.out.println("An IO error occurred :)");
            System.exit(0);
        }
        return null;
    }
}
