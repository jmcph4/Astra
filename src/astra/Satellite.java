package astra;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Jack McPherson
 * */

/**
 * Representation of a satellite orbiting Earth.
 * 
 */
public class Satellite
{   
    private String name;
    private int num;
    private String classification;
    private Calendar launchYear;
    private int launchNum;
    private int launchPiece;
    private Calendar epoch;
    private float ftdmm2;
    private float stdmm6;
    private float drag;
    private int ephemeris;
    
    // Keplerians
    private float inclination;
    private float rightAscension;
    private float eccentricity;
    private float perigee;
    private float meanAnomaly;
    private float meanMotion;
    private int revolutions;
    
    private final String TIMESTAMP_FORMAT = "yyyy-MM-dd";
    
    /**
     * Default constructor for the class. Initialises all fields to their
     * default values.
     */
    public Satellite()
    {
        this.name = "";
        this.classification = "";
        this.num = 0;
        this.launchYear = new GregorianCalendar();
        this.launchNum = 1;
        this.launchPiece = 1;
        this.epoch = new GregorianCalendar();
        this.ftdmm2 = 0;
        this.stdmm6 = 0;
        this.drag = 0;
        this.ephemeris = 0;
        
        this.inclination = 0;
        this.rightAscension = 0;
        this.eccentricity = 0;
        this.perigee = 0;
        this.meanAnomaly = 0;
        this.meanMotion = 0;
        this.revolutions = 0;
    }
    
    /**
     * @param name
     *          name of the satellite
     * @param number
     *          satellite number
     * @param classification
     *          classification of the satellite
     * @param internationalDesignatorLaunchYear
     *          year the satellite was launched
     * @param internationalDesignatorLaunchNumber
     *         what number the launch was of the launch year
     * @param internationalDesignatorLaunchPiece
     *          piece of the launch
     * @param epoch
     *          the astronomical epoch of the orbital elements
     * @param ftdmm2
     *          first time derivative of mean motion divided by 2
     * @param stdmm6
     *          second time derivative of mean motion divided by 6
     * @param BSTARDrag
     *          the drag term represented as BSTAR drag
     * @param ephemerisType
     *          the type of ephemeris
     * @param inclination
     *          the inclination, in degrees, of the satellite
     * @param rightAscensionAscendingNode
     *          the right ascension of the ascending node (in degrees)
     * @param eccentricity
     *          the eccentricity of the satellite (in degrees)
     * @param argumentOfPerigee
     *          the argument of perigee (in degrees)
     * @param meanAnomaly
     *          the mean anomaly of the satellite
     * @param meanMotion
     *          the mean motion of the satellite
     * @param revolutionsPerDay
     *          the number of revolutions per day, as at the epoch
     * 
     * @throws NullPointerException
     *             if <code>name</code>, <code>classification</code>, or
     *             <code>epoch</code> are <code>null</code>
     * @throws IllegalArgumentException
     *             if
     */
    public Satellite(String name, int number, String classification,
                    Calendar internationalDesignatorLaunchYear,
                    int internationalDesignatorLaunchNumber,
                    int internationalDesignatorLaunchPiece, Calendar epoch,
                    float ftdmm2, float stdmm6, float BSTARDrag,
                    int ephemerisType, float inclination,
                    float rightAscensionAscendingNode, float eccentricity,
                    float argumentOfPerigee, float meanAnomaly,
                    float meanMotion, int revolutionsPerDay)
    {
        this.setName(name);
        this.setNum(number);
        this.setClassification(classification);
        this.setLaunchYear(internationalDesignatorLaunchYear);
        this.setLaunchNum(internationalDesignatorLaunchNumber);
        this.setLaunchPiece(internationalDesignatorLaunchPiece);
        this.setEpoch(epoch);
        this.setFtdmm2(ftdmm2);
        this.setStdmm6(stdmm6);
        this.setDrag(BSTARDrag);
        this.setEphemeris(ephemerisType);
        
        // Keplerians
        this.setInclination(inclination);
        this.setRightAscension(rightAscensionAscendingNode);
        this.setEccentricity(eccentricity);
        this.setPerigee(argumentOfPerigee);
        this.setMeanAnomaly(meanAnomaly);
        this.setMeanMotion(meanMotion);
        this.setRevolutions(revolutionsPerDay);
    }
    
    /**
     * @return satellite name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @param name
     *            satellite name
     */
    public void setName(String name)
    {
        if(name == null)
        {
            throw new NullPointerException();
        }
        
        this.name = name;
    }
    
    /**
     * @return satellite number
     */
    public int getNum()
    {
        return num;
    }
    
    /**
     * @param num
     *            satellite number
     */
    public void setNum(int num)
    {
        if(num < 0)
        {
            throw new IllegalArgumentException("Negative satellite number");
        }
        
        this.num = num;
    }
    
    /**
     * @return classification
     */
    public String getClassification()
    {
        return classification;
    }
    
    /**
     * @param classification
     *            classification
     */
    public void setClassification(String classification)
    {
        if(classification == null)
        {
            throw new NullPointerException();
        }
        
        this.classification = classification;
    }
    
    /**
     * @return launch year
     */
    public Calendar getLaunchYear()
    {
        return launchYear;
    }
    
    /**
     * @param launchYear
     *            launch year
     */
    public void setLaunchYear(Calendar launchYear)
    {
        if(launchYear == null)
        {
            throw new NullPointerException();
        }
        
        this.launchYear = launchYear;
    }
    
    /**
     * @return launch number
     */
    public int getLaunchNum()
    {
        return launchNum;
    }
    
    /**
     * @param launchNum
     *            launch number
     */
    public void setLaunchNum(int launchNum)
    {
        if(launchNum <= 0)
        {
            throw new IllegalArgumentException("Non-positive launch number");
        }
        
        this.launchNum = launchNum;
    }
    
    /**
     * @return launchPiece
     */
    public int getLaunchPiece()
    {
        return launchPiece;
    }
    
    /**
     * @param launchPiece
     *            launch piece
     */
    public void setLaunchPiece(int launchPiece)
    {
        if(launchPiece <= 0)
        {
            throw new IllegalArgumentException("Non-positive launch piece");
        }
        
        this.launchPiece = launchPiece;
    }
    
    /**
     * @return epoch
     */
    public Calendar getEpoch()
    {
        return epoch;
    }
    
    /**
     * @param epoch
     *            epoch
     */
    public void setEpoch(Calendar epoch)
    {
        if(epoch == null)
        {
            throw new NullPointerException();
        }
        
        this.epoch = epoch;
    }
    
    /**
     * @return first time derivative of mean motion divided by 2
     */
    public float getFtdmm2()
    {
        return ftdmm2;
    }
    
    /**
     * @param ftdmm2
     *            first time derivative of mean motion divided by 2
     */
    public void setFtdmm2(float ftdmm2)
    {
        this.ftdmm2 = ftdmm2;
    }
    
    /**
     * @return second time derivative of mean motion divided by 6
     */
    public float getStdmm6()
    {
        return stdmm6;
    }
    
    /**
     * @param stdmm6
     *            second time derivative of mean motion divided by 6
     */
    public void setStdmm6(float stdmm6)
    {
        this.stdmm6 = stdmm6;
    }
    
    /**
     * @return BSTAR drag term
     */
    public float getDrag()
    {
        return drag;
    }
    
    /**
     * @param drag
     *            BSTAR drag term
     */
    public void setDrag(float drag)
    {
        this.drag = drag;
    }
    
    /**
     * @return ephemeris type
     */
    public int getEphemeris()
    {
        return ephemeris;
    }
    
    /**
     * @param ephemeris
     *            ephemeris type
     */
    public void setEphemeris(int ephemeris)
    {
        this.ephemeris = ephemeris;
    }
    
    /**
     * @return inclination
     */
    public float getInclination()
    {
        return inclination;
    }
    
    /**
     * @param inclination
     *            inclination (&deg;)
     */
    public void setInclination(float inclination)
    {
        this.inclination = inclination;
    }
    
    /**
     * @return right ascension of ascending node (&deg;)
     */
    public float getRightAscension()
    {
        return rightAscension;
    }
    
    /**
     * @param rightAscension
     *            right ascension of ascending node (&deg;)
     */
    public void setRightAscension(float rightAscension)
    {
        this.rightAscension = rightAscension;
    }
    
    /**
     * @return eccentricity (&deg;)
     */
    public float getEccentricity()
    {
        return eccentricity;
    }
    
    /**
     * @param eccentricity
     *            eccentricity (&deg;)
     */
    public void setEccentricity(float eccentricity)
    {
        this.eccentricity = eccentricity;
    }
    
    /**
     * @return argument of perigee (&deg;)
     */
    public float getPerigee()
    {
        return perigee;
    }
    
    /**
     * @param perigee
     *            argument of perigee (&deg;)
     */
    public void setPerigee(float perigee)
    {
        this.perigee = perigee;
    }
    
    /**
     * @return mean anomaly
     */
    public float getMeanAnomaly()
    {
        return meanAnomaly;
    }
    
    /**
     * @param meanAnomaly
     *            mean anomaly
     */
    public void setMeanAnomaly(float meanAnomaly)
    {
        this.meanAnomaly = meanAnomaly;
    }
    
    /**
     * @return mean motion
     */
    public float getMeanMotion()
    {
        return meanMotion;
    }
    
    /**
     * @param meanMotion
     *            mean motion
     */
    public void setMeanMotion(float meanMotion)
    {
        this.meanMotion = meanMotion;
    }
    
    /**
     * @return revolutions per day as at epoch
     */
    public int getRevolutions()
    {
        return revolutions;
    }
    
    /**
     * @param revolutions
     *            revolutions
     */
    public void setRevolutions(int revolutions)
    {
        if(revolutions < 0)
        {
            throw new IllegalArgumentException("Negative revolutions at epoch");
        }
        
        this.revolutions = revolutions;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classification == null) ? 0
                        : classification.hashCode());
        result = prime * result + Float.floatToIntBits(drag);
        result = prime * result + Float.floatToIntBits(eccentricity);
        result = prime * result + ephemeris;
        result = prime * result + ((epoch == null) ? 0 : epoch.hashCode());
        result = prime * result + Float.floatToIntBits(ftdmm2);
        result = prime * result + Float.floatToIntBits(inclination);
        result = prime * result + launchNum;
        result = prime * result + launchPiece;
        result = prime * result
                        + ((launchYear == null) ? 0 : launchYear.hashCode());
        result = prime * result + Float.floatToIntBits(meanAnomaly);
        result = prime * result + Float.floatToIntBits(meanMotion);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + num;
        result = prime * result + Float.floatToIntBits(perigee);
        result = prime * result + Float.floatToIntBits(revolutions);
        result = prime * result + Float.floatToIntBits(rightAscension);
        result = prime * result + Float.floatToIntBits(stdmm6);
        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        Satellite other = (Satellite) obj;
        if(classification == null)
        {
            if(other.classification != null)
            {
                return false;
            }
        }
        else if(!classification.equals(other.classification))
        {
            return false;
        }
        if(Float.floatToIntBits(drag) != Float.floatToIntBits(other.drag))
        {
            return false;
        }
        if(Float.floatToIntBits(eccentricity) != Float
                        .floatToIntBits(other.eccentricity))
        {
            return false;
        }
        if(ephemeris != other.ephemeris)
        {
            return false;
        }
        if(epoch == null)
        {
            if(other.epoch != null)
            {
                return false;
            }
        }
        else if(!epoch.equals(other.epoch))
        {
            return false;
        }
        if(Float.floatToIntBits(ftdmm2) != Float.floatToIntBits(other.ftdmm2))
        {
            return false;
        }
        if(Float.floatToIntBits(inclination) != Float
                        .floatToIntBits(other.inclination))
        {
            return false;
        }
        if(launchNum != other.launchNum)
        {
            return false;
        }
        if(launchPiece != other.launchPiece)
        {
            return false;
        }
        if(launchYear == null)
        {
            if(other.launchYear != null)
            {
                return false;
            }
        }
        else if(!launchYear.equals(other.launchYear))
        {
            return false;
        }
        if(Float.floatToIntBits(meanAnomaly) != Float
                        .floatToIntBits(other.meanAnomaly))
        {
            return false;
        }
        if(Float.floatToIntBits(meanMotion) != Float
                        .floatToIntBits(other.meanMotion))
        {
            return false;
        }
        if(name == null)
        {
            if(other.name != null)
            {
                return false;
            }
        }
        else if(!name.equals(other.name))
        {
            return false;
        }
        if(num != other.num)
        {
            return false;
        }
        if(Float.floatToIntBits(perigee) != Float.floatToIntBits(other.perigee))
        {
            return false;
        }
        if(Float.floatToIntBits(revolutions) != Float
                        .floatToIntBits(other.revolutions))
        {
            return false;
        }
        if(Float.floatToIntBits(rightAscension) != Float
                        .floatToIntBits(other.rightAscension))
        {
            return false;
        }
        if(Float.floatToIntBits(stdmm6) != Float.floatToIntBits(other.stdmm6))
        {
            return false;
        }
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        SimpleDateFormat launchYearFormat = new SimpleDateFormat("yyyy");
        String launchYearString = launchYearFormat.format(
                        this.launchYear.getTime());
        
        SimpleDateFormat epochFormat = new SimpleDateFormat(
                        this.TIMESTAMP_FORMAT);
        String epochString = epochFormat.format(this.epoch.getTime());
        
        return "Satellite [name=" + name + ", num=" + num + ", classification="
                        + classification + ", launchYear=" + launchYearString
                        + ", launchNum=" + launchNum + ", launchPiece="
                        + launchPiece + ", epoch=" + epochString + ", ftdmm2="
                        + ftdmm2 + ", stdmm6=" + stdmm6 + ", drag=" + drag
                        + ", ephemeris=" + ephemeris + ", inclination="
                        + inclination + ", rightAscension=" + rightAscension
                        + ", eccentricity=" + eccentricity + ", perigee="
                        + perigee + ", meanAnomaly=" + meanAnomaly
                        + ", meanMotion=" + meanMotion + ", revolutions="
                        + revolutions + "]";
    }
    
}
