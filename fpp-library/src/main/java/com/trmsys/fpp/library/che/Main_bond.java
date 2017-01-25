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


public class Main_bond {
    
    public static void main (String [] args) throws Exception {
        
        
        // Create the dates
        List<LocalDate> stepDates = new ArrayList<>();
        stepDates.add(LocalDate.now());
        
        // Create the builder
        DataManagerBuilder builder = new DataManagerBuilder(1, stepDates);
        
        // Create the scenarios
        List<ScenarioValues> scenarios = new ArrayList<>();
        
               
        // Create and add the maturities to the scenarios.  The array which contains this should be the same length as the coupon array
         double [] coupon_maturities = getDoubleDates(LocalDate.now().toString(),11, 6); // Array which contains coupon mats as doubles starting 1970-01-01
        
        // Create and add the coupons to the scenarios
        for (int i = 0; i < 11; i++){
            if (i == 0) {
                scenarios.add(new ScenarioValues(new ScenarioIdentifier("FIXED_INCOME", Lists.newArrayList("COUPON","USD")), coupon_maturities[i],null,null, new double[] {0.0}));   
            } else {scenarios.add(new ScenarioValues(new ScenarioIdentifier("FIXED_INCOME", Lists.newArrayList("COUPON","USD")), coupon_maturities[i],null,null, new double[] {0.03}));   
            }  
        }
               
                   
        // create the scenarios for the yield curv
        double [] yield_curve = {365.0, 731.0, 1096.0, 1826.0, 2192.0, 2557.0, 7305.0};
        double [] YIELD_USD =  {0.59,1.91,3.05,4.17,5.86,6.8406,7.75};
        double yield_point [] = new double[1];

        for (int j = 0; j  < yield_curve.length; j++){
            yield_point[0] = YIELD_USD[j];
            scenarios.add(new ScenarioValues(new ScenarioIdentifier("YIELD_CURVE", Lists.newArrayList("USD","COUPON_BEARING_BOND_RATE")), yield_curve[j],null,null, new double[]{YIELD_USD[j]/100.0}));
        }
        
        
               
               
       builder.addScenarios(scenarios);
     
       System.out.println(builder.toString());
        
        //  Note that a second fileName is created and commented so that you can switch between the market data files for single and multiple scenarios easily
       String fileName = "/projects/fpp-library-vaas/fpp-library/src/main/resources/com/trmsys/fpp/library/marketData/gsMarketData_BOND.json";        
       //String fileName = "/projects/fpp-library-vaas/fpp-library/src/main/resources/com/trmsys/fpp/library/marketData/gsMarketData_BOND_multi.json";      
       PrintWriter outputStream = new PrintWriter(fileName);
       outputStream.println(builder.toString());
       outputStream.close();
       
       //  The system out is for you to ensure that the code has finished compiling
       System.out.println(stepDates.toString());
       System.out.println("George's Project");        
    }
    
    private static PrintWriter createFile(String fileName){        
        try{
            File listOfData = new File(fileName);       
            PrintWriter infoToWrite = new PrintWriter(new BufferedWriter(new FileWriter(listOfData)));
        } catch(IOException e){
            System.out.println("An IO error occurred :)");
            System.exit(0);
        }
        return null;
    }
    
    /*yuji getLocalDates start*/
     /**
     * Returns an array with dates 
     * @param start_date
     *        string with the format of "yyyy-mm-dd"
     * @param num_of_dates
     *        number of dates in the array
     * @param increment
     *        number of months to get the next data in the array
     * @return
     *        returns a list of LocalDate list 
     */
    private static List<LocalDate> getLocalDates(String start_date,int num_of_dates,int increment) {
        List<LocalDate> stepDates = new ArrayList<>();
        LocalDate Start_Date=LocalDate.parse(start_date);
        stepDates.add(Start_Date);
        for(int i=0;i<num_of_dates;i++){
            LocalDate new_date=Start_Date.plusMonths(increment);
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
    
}
