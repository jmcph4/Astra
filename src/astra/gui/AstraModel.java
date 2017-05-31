package astra.gui;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import astra.Satellite;
import astra.SatelliteReader;

/**
 * Model component of the Astra application
 * 
 * */
public class AstraModel
{   
    private List<Satellite> satellites;
    
    /**
     * Constructor for the {@link AstraModel} class
     * 
     * @param defaultTLEFileName
     *          the path to the default TLE file to load
     * @throws IOException
     *          if there is an error in reading the file
     * @throws ParseException
     *          if the syntax of the TLE is invalid
     * */
    public AstraModel(String defaultTLEFileName) throws IOException,
        ParseException
    {
        if(defaultTLEFileName == null)
        {
            throw new NullPointerException();
        }
               
        SatelliteReader reader = new SatelliteReader(defaultTLEFileName);
        
        this.satellites = reader.read();
    }
    
    /**
     * @return satellite list
     * 
     * */
    public List<Satellite> getSatelliteList()
    {
        return this.satellites;
    }
    
    /**
     * @param satellite
     *          the satellite to return the description for
     * @return satellite description
     * 
     * */
    public Map<String, String> getSatelliteDescription(Satellite satellite)
    {
        if(satellite == null)
        {
            throw new NullPointerException();
        }
        
        if(!this.satellites.contains(satellite))
        {
            throw new IllegalArgumentException("No such satellite exists");
        }
        
        String satelliteString = satellite.toString();
        
        int startIndex = satelliteString.indexOf("[") + 1;
        int endIndex = satelliteString.indexOf("]");
        
        String strippedSatelliteString = satelliteString.trim().substring(
                        startIndex, endIndex);
        
        List<String> pairs = new ArrayList<String>(
                        Arrays.asList(strippedSatelliteString.split(","))); 
        
        List<String> cells = new ArrayList<String>();
        
        for(int i=0;i<pairs.size();i++)
        {
            cells.addAll(Arrays.asList(pairs.get(i).split("=")));
        }
        
        Map<String, String> description = new HashMap<String, String>();
        
        for(int i=1;i<pairs.size();i++)
        {
            if(i % 2 != 0)
            {
                description.put(AstraModel.capitalise(cells.get(i - 1).trim()),
                                cells.get(i));
            }
        }
        
        return description;
    }
    
    /**
     * Capitalises <code>s</code>
     * 
     * @param s
     *          string to be capitalised
     * @return capitalised string
     * 
     * */
    private static String capitalise(String s)
    {
        if(s == null)
        {
            throw new NullPointerException();
        }
        
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
