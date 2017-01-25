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


public class Main {
    
    public static void main (String [] args) throws Exception {

        
        // Create the dates
        List<LocalDate> stepDates = new ArrayList<>();
        stepDates.add(LocalDate.now());
        
        // Create the builder
        DataManagerBuilder builder = new DataManagerBuilder(1, stepDates);
               
       // Create the scenarios
        List<ScenarioValues> scenarios = new ArrayList<>();
            
        // create the scenarios for the vol surface
         List<ScenarioValues> scenarios_eqvol = new ArrayList<>();
        double [] vol_surf_mat = { 1.0, 7.0, 30.0, 183.0, 365.0};
        double [] vol_surf_str = {0.0, 25.0, 50.0, 75.0 , 100.0};
        double [] vol_values ={0.05};

            
        for (int i = 0; i< vol_surf_mat.length; i++){
            for (int j = 0; j < vol_surf_str.length; j++){               
                scenarios_eqvol.add(new ScenarioValues(new ScenarioIdentifier("EQ_VOL_SURFACE", Lists.newArrayList("COCA_COLA","EQUITY","USD")), null,vol_surf_mat[i],vol_surf_str[j], vol_values));
            }
        }

        double[] xval = new double[1];
        for (int i = 0; i<xval.length; i++){
            xval[i]= 200* (i+1);
            xval[i]= 200 * (i+1);
        }
        
        
       // Add values to the scenarios: for ASSET and for DIV_YIELD 
       scenarios.add(new ScenarioValues(new ScenarioIdentifier("ASSET", Lists.newArrayList("COCA_COLA","EQUITY","USD")), null,null,null, xval));
       scenarios.add(new ScenarioValues(new ScenarioIdentifier("DIV_YIELD", Lists.newArrayList("COCA_COLA","CONTINUOUS")), null,null,null, new double[]{0.0150}));
        
        
        //Add the scenarios to the builder
        builder.addScenarios(scenarios);
        builder.addScenarios(scenarios_eqvol);
       
       // Output the builder as a string so that we can write it as a market data json later on
       System.out.println(builder.toString());
       
       
       // Create a PrintWriter Object to write your market data in
       String fileName = "/projects/fpp-library-vaas/fpp-library/src/main/resources/com/trmsys/fpp/library/marketData/gsMarketData.json";
       PrintWriter outputStream = new PrintWriter(fileName);       

       // Write your market data to file. 
       outputStream.println(builder.toString());
       outputStream.close();
       System.out.println(stepDates.toString());
       System.out.println("George's Project"); 
       
        /*yuji start*/
        System.out.println("yuji test start"); 
        List<LocalDate> yujiDates = getLocalDates("1993-02-21",3,3);
        System.out.println(yujiDates.toString());
        System.out.println("yuji test end"); 
         System.out.println("yuji DoubleDates test start"); 
        getDoubleDates("1993-02-21",3,3);
        System.out.println("yuji DoubleDates test end");        
        /*yuji end*/
  
    }
    
        /*yuji getLocalDates start*/
     /**
     * Returns an array with dates 
     * @param start_date
     *        string with the format of "yyyy-mm-dd"
     * @param num_of_dates
     *        number of dates in the array
     * @param freq
     *        number of months to get the next data in the array
     * @return
     *        returns a list of LocalDate list 
     */
    private static List<LocalDate> getLocalDates(String start_date,int num_of_dates,int fre) {
        List<LocalDate> stepDates = new ArrayList<>();
        LocalDate Start_Date=LocalDate.parse(start_date);
        stepDates.add(Start_Date);
        for(int i=0;i<num_of_dates;i++){
            LocalDate new_date=Start_Date.plusMonths(fre);
            stepDates.add(new_date);
            Start_Date=new_date;
        }
        return stepDates;  
    }
     //e.g. of using:List<LocalDate> yujiDates = getDates("1993-02-21",3,3);
     /* yuji getLocalDates end*/

    /*yuji getDoubleDates start*/
     /**
     * Returns an double array with dates 
     * @param start_date
     *        string with the format of "yyyy-mm-dd"
     * @param num_of_dates
     *        number of dates in the array
     * @param freq
     *        number of months to get the next data in the array
     * @return
     *        returns a double array 
     */

    private static double [] getDoubleDates(String start_date,int num_of_dates,int fre) {
        double [] stepDates = new double [num_of_dates];
        LocalDate Start_Date=LocalDate.parse(start_date);
        long dateFromBase = Start_Date.toEpochDay();
        //double doubleNewDate = Double.longBitsToDouble(dateFromBase);
        double doubleNewDate= (double) dateFromBase;
        stepDates[0]=doubleNewDate;
        System.out.println(doubleNewDate);   
        LocalDate new_date=Start_Date;
        for(int i=1;i<num_of_dates;i++){
            new_date=new_date.plusMonths(fre);
            dateFromBase = new_date.toEpochDay();
            doubleNewDate = (double) dateFromBase;
            //doubleNewDate = Double.longBitsToDouble(dateFromBase);
            stepDates[i]=doubleNewDate;
            System.out.println(doubleNewDate);        
        }
        return stepDates;  
    }
     //e.g. of using:double [] yujiDates = getDoubleDates("1993-02-21",3,3);
     /* yuji getDoubleDates end*/
   private static PrintWriter createFile(String fileName){
        
        try{
            File listOfData = new File(fileName);
            PrintWriter infoToWrite = new PrintWriter(new BufferedWriter(new FileWriter(listOfData)));
        } catch(IOException e){
            System.out.println("An IO error occurred: )");
            System.exit(0);
        }
        return null;
    }
    
}
