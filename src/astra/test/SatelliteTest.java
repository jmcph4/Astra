/**
 * Unit tests for the {@link Satellite} class.
 */
package astra.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import astra.Satellite;
import org.junit.Assert;

/**
 * @author Jack McPherson
 *
 */
public class SatelliteTest
{
    
    private Satellite satelliteA;
    private Satellite satelliteB;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        Calendar launchYearA = new GregorianCalendar();
        launchYearA.set(Calendar.YEAR, 1998);
        
        Calendar epochA = new GregorianCalendar();
        epochA.set(Calendar.YEAR, 2008);
        epochA.set(Calendar.DAY_OF_YEAR, 264);
        
        this.satelliteA = new Satellite("ISS (ZARYA)", 25544, "U", launchYearA,
                        67, 1, epochA, -0.00002182f, 0.0f, -11606.4f, 0,
                        51.6416f, 247.4627f, 0.0006703f, 130.5360f, 325.0288f,
                        15.72125391f, 56353);
        
        Calendar launchYearB = new GregorianCalendar();
        launchYearB.set(Calendar.YEAR, 1998);
        
        Calendar epochB = new GregorianCalendar();
        epochB.set(Calendar.YEAR, 2015);
        epochB.set(Calendar.DAY_OF_YEAR, 53);
        
        this.satelliteB = new Satellite("NOAA 15", 25338, "U", launchYearB,
                        30, 1, epochB, 0.00000187f, 0.0f, 97801.4f, 0,
                        98.7693f, 52.1527f, 0.0011546f, 138.1703f, 329.3111f,
                        14.255691348f, 72510);
    }
    
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }
    
    /**
     * Test method for {@link astra.Satellite#Satellite()}.
     */
    @Test
    public void testSatelliteDefaultNormal()
    {
        Satellite expected = new Satellite();
        Satellite actual = new Satellite();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSatelliteSetNumberNegative()
    {
        Satellite actual = new Satellite();
        actual.setNum(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSatelliteSetLaunchNumZero()
    {
        Satellite actual = new Satellite();
        actual.setLaunchNum(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSatelliteSetLaunchNumNegative()
    {
        Satellite actual = new Satellite();
        actual.setLaunchNum(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSatelliteSetLaunchPieceZero()
    {
        Satellite actual = new Satellite();
        actual.setLaunchPiece(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSatelliteSetLaunchPieceNegative()
    {
        Satellite actual = new Satellite();
        actual.setLaunchPiece(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSatelliteSetRevolutionsNegative()
    {
        Satellite actual = new Satellite();
        actual.setRevolutions(-1);
    }
    
    /**
     * Test method for {@link astra.Satellite#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject()
    {
        Assert.assertTrue(this.satelliteA.equals(satelliteA));
        Assert.assertTrue(this.satelliteB.equals(satelliteB));
        Assert.assertFalse(this.satelliteA.equals(this.satelliteB));
    }
    
    /**
     * Test method for {@link astra.Satellite#toString()}.
     */
    @Test
    public void testToString()
    {
        String expected = "Satellite [name=ISS (ZARYA), num=25544, " +
                            "classification=U, " + 
                            "launchYear=1998, " + 
                            "launchNum=67, " + 
                            "launchPiece=1, " +
                            "epoch=2008-09-20, " +
                            "ftdmm2=-2.182E-5, stdmm6=0.0, drag=-11606.4, " + 
                            "ephemeris=0, inclination=51.6416, " + 
                            "rightAscension=247.4627, " + 
                            "eccentricity=6.703E-4, perigee=130.536, " + 
                            "meanAnomaly=325.0288, meanMotion=15.721254, " + 
                            "revolutions=56353]";
        String actual = this.satelliteA.toString();
        
        Assert.assertEquals(expected, actual);
    }
    
}
