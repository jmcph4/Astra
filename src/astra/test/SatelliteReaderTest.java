/**
 * 
 */
package astra.test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import astra.Satellite;
import astra.SatelliteReader;

/**
 * @author Jack McPherson
 *
 */
public class SatelliteReaderTest
{
    
    private List<String> validFiles;
    private ArrayList<String> invalidFiles;
    
    private final String testPath = "tests/tle/";
    private List<List<Satellite>> satelliteObjects;
    
    /**
     * @throws java.lang.Exception
     *          if any exception occurs underneath
     */
    @Before
    public void setUp() throws Exception
    {
        // list of valid TLE files
        this.validFiles = new ArrayList<String>();
        this.validFiles.add(testPath + "valid01.txt");
        this.validFiles.add(testPath + "valid02.txt");
        
        // list of invalid TLE files
        this.invalidFiles = new ArrayList<String>();
        this.invalidFiles.add(testPath + "invalid01_emptyfile.txt");
        this.invalidFiles.add(testPath + "invalid02_excessivelines.txt");
        this.invalidFiles.add(testPath + "invalid03_negsatnum.txt");
        this.invalidFiles.add(testPath + "invalid04_neglaunchnum.txt");
        this.invalidFiles.add(testPath + "invalid05_zerolaunchnum.txt");
        this.invalidFiles.add(testPath + "invalid06_neglaunchpiece.txt");
        
        this.satelliteObjects = new ArrayList<List<Satellite>>();
        this.satelliteObjects.add(new ArrayList<Satellite>());
        this.satelliteObjects.add(new ArrayList<Satellite>());
        
        // Calendar objects
        Calendar launchYear1 = new GregorianCalendar();
        launchYear1.set(Calendar.YEAR, 1998);
        
        Calendar epoch1 = new GregorianCalendar();
        epoch1.set(Calendar.YEAR, 2017);
        epoch1.set(Calendar.DAY_OF_YEAR, 126);
        
        Calendar launchYear2 = new GregorianCalendar();
        launchYear2.set(Calendar.YEAR, 2011);
        
        Calendar epoch2 = new GregorianCalendar();
        epoch2.set(Calendar.YEAR, 2017);
        epoch2.set(Calendar.DAY_OF_YEAR, 128);
        
        Calendar launchYear3 = new GregorianCalendar();
        launchYear3.set(Calendar.YEAR, 1974);
        
        Calendar epoch3 = new GregorianCalendar();
        epoch3.set(Calendar.YEAR, 2017);
        epoch3.set(Calendar.DAY_OF_YEAR, 130);
        
        Calendar launchYear4 = new GregorianCalendar();
        launchYear4.set(Calendar.YEAR, 1974);
        
        Calendar epoch4 = new GregorianCalendar();
        epoch4.set(Calendar.YEAR, 2017);
        epoch4.set(Calendar.DAY_OF_YEAR, 128);
        
        // satellite objects
        this.satelliteObjects.add(new ArrayList<Satellite>());
        
        this.satelliteObjects.get(0).add(new Satellite("ISS (ZARYA)", 25544,
                        "U", launchYear1, 67, 1, epoch1, 0.00002780f, 0.0f,
                        49495.4f, 0, 51.6401f, 245.6477f, 0.0005666f, 129.9909f,
                        47.4633f, 15.53976999f, 55286));
        this.satelliteObjects.get(0).add(new Satellite("TIANGONG 1", 37820, "U",
                        launchYear2, 53, 1, epoch2, 0.00017038f, 0.0f, 10346.3f,
                        0, 42.7592f, 83.7323f, 0.0017363f, 158.5208f, 331.2752f,
                        15.770008103f, 21677));
        
        this.satelliteObjects.get(1).add(new Satellite("MOLNIYA 2-9", 7276, "U",
                        launchYear3, 26, 1, epoch3, 0.00000186f, 0.0f, 14533.2f,
                        0, 62.7587f, 178.3305f, 0.6865880f, 287.4755f, 12.5476f,
                        2.450977612f, 3668));
        this.satelliteObjects.get(1).add(new Satellite("MOLNIYA 2-10", 7376,
                        "U", launchYear4, 56, 1, epoch4, -0.00001767f, 0.0f,
                        26226.3f, 0, 62.8847f, 308.5504f, 0.7362834f, 292.3246f,
                        8.4617f, 2.011226003f, 12833));
    }
    
    /**
     * @throws java.lang.Exception
     *          if any exception occurs underneath
     */
    @After
    public void tearDown() throws Exception
    {
    }
    
    /**
     * Test method for
     * {@link astra.SatelliteReader#SatelliteReader(java.lang.String)}.
     * 
     * @throws ParseException
     *          if an error occurs while validating the TLE file
     * @throws IOException
     *          if an error occurs while reading the TLE file
     */
    @Test
    public void testSatelliteReader() throws IOException, ParseException
    {
        SatelliteReader expectedReader = new SatelliteReader(
                        this.validFiles.get(0));
        
        SatelliteReader actualReader = new SatelliteReader(
                        this.validFiles.get(0));
        
        Assert.assertTrue(expectedReader.equals(actualReader));
    }
    
    /**
     * Test method for {@link astra.SatelliteReader#read()}.
     * 
     * @throws ParseException
     *          if an error occurs while validating the TLE file
     * @throws IOException
     *          if an error occurs while reading the TLE file
     */
    @Test
    public void testReadNormal() throws IOException, ParseException
    {
        List<Satellite> expected = this.satelliteObjects.get(0);
        
        SatelliteReader actualReader = new SatelliteReader(
                        this.validFiles.get(0));
        
        List<Satellite> actual = actualReader.read();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testReadNoTerminatingNewline()
                    throws IOException, ParseException
    {
        List<Satellite> expected = this.satelliteObjects.get(1);
        SatelliteReader actualReader = new SatelliteReader(
                        this.validFiles.get(1));
        List<Satellite> actual = actualReader.read();
        
        Assert.assertEquals(expected.toString(), actual.toString());
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = ParseException.class)
    public void testSatelliteReaderEmptyFile()
                    throws IOException, ParseException
    {
        SatelliteReader actual = new SatelliteReader(this.invalidFiles.get(0));
        actual.hashCode(); // suppress warning
    }
    
    @Test
    public void testReadNegativeSatelliteNumber()
                    throws IOException, ParseException
    {
        SatelliteReader actualReader = new SatelliteReader(
                        this.invalidFiles.get(2));
        try
        {
            List<Satellite> actualSatelliteList = actualReader.read();
            actualSatelliteList.hashCode(); // suppress warnings
            Assert.fail("ParseException not thrown");
        }
        catch(ParseException e)
        {
            Assert.assertEquals("Negative satellite number", e.getMessage());
            Assert.assertEquals(27, e.getErrorOffset());
        }
    }
    
    @Test
    public void testReadNegativeLaunchNumber()
                    throws IOException, ParseException
    {
        SatelliteReader actualReader = new SatelliteReader(
                        this.invalidFiles.get(3));
        try
        {
            List<Satellite> actualSatelliteList = actualReader.read();
            actualSatelliteList.hashCode(); // suppress warnings
            Assert.fail("ParseException not thrown");
        }
        catch(ParseException e)
        {
            Assert.assertEquals("Non-positive launch number", e.getMessage());
            Assert.assertEquals(37, e.getErrorOffset());
        }
    }
    
    @Test
    public void testReadZeroLaunchNumber() throws IOException, ParseException
    {
        SatelliteReader actualReader = new SatelliteReader(
                        this.invalidFiles.get(4));
        try
        {
            List<Satellite> actualSatelliteList = actualReader.read();
            actualSatelliteList.hashCode(); // suppress warnings
            Assert.fail("ParseException not thrown");
        }
        catch(ParseException e)
        {
            Assert.assertEquals("Non-positive launch number", e.getMessage());
            Assert.assertEquals(37, e.getErrorOffset());
        }
    }
    
}
