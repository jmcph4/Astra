package astra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Jack McPherson
 */

/**
 * Reads and parses a Two Line Element (TLE) file into a <code>Satellite</code>
 * object.
 */
public class SatelliteReader
{
    private String fileName;
    private List<String> lines;
    
    // error messages
    private final String ERR_MSG_EMPTY_FILE = "Empty file";
    private final String ERR_MSG_INVALID_SAT_NUM = "Invalid satellite number";
    private final String ERR_MSG_INVALID_LAUNCH_NUM = "Invalid launch number";
    private final String ERR_MSG_INVALID_LAUNCH_YEAR = "Invalid launch year";
    private final String ERR_MSG_INVALID_EPOCH = "Invalid epoch";
    private final String ERR_MSG_INVALID_FTDMM2 = "Invalid first time "
                    + "derivative of mean motion";
    private final String ERR_MSG_INVALID_STDMM6 = "Invalid second time "
                    + "derivative of mean motion";
    private final String ERR_MSG_INVALID_DRAG = "Invalid BSTAR drag term";
    private final String ERR_MSG_INVALID_ASCENSION = "Invalid right ascension "
                    + "of the ascending node";
    private final String ERR_MSG_INVALID_ECCENTRICITY = "Invalid eccentricity";
    private final String ERR_MSG_INVALID_INCLINATION = "Invalid inclination";
    private final String ERR_MSG_INVALID_MEAN_ANOM = "Invalid mean anomaly";
    private final String ERR_MSG_INVALID_MEAN_MOTION = "Invalid mean motion";
    private final String ERR_MSG_INVALID_PERIGEE = "Invalid argument of "
                    + "perigee";
    private final String ERR_MSG_INVALID_REVS = "Invalid number of revolutions"
                    + " at epoch";
    private final String ERR_MSG_INVALID_EPHEMERIS = "Invalid ephemeris type";
    
    // ranges for parsing elements
    private final int RANGE_START_SAT_NAME = 0;
    private final int RANGE_END_SAT_NAME = 23;
    private final int RANGE_START_SAT_NUM = 2;
    private final int RANGE_END_SAT_NUM = 7;
    private final int RANGE_START_CLASSIFICATION = 7;
    private final int RANGE_END_CLASSIFICATION = 8;
    private final int RANGE_START_LAUNCH_YEAR = 9;
    private final int RANGE_END_LAUNCH_YEAR = 11;
    private final int RANGE_START_LAUNCH_NUM = 11;
    private final int RANGE_END_LAUNCH_NUM = 14;
    private final int RANGE_START_LAUNCH_PIECE = 14;
    private final int RANGE_END_LAUNCH_PIECE = 16;
    private final int RANGE_START_EPOCH_YEAR = 17;
    private final int RANGE_END_EPOCH_YEAR = 20;
    private final int RANGE_START_EPOCH_DAY = 20;
    private final int RANGE_END_EPOCH_DAY = 31;
    private final int RANGE_START_FTDMM2 = 33;
    private final int RANGE_END_FTDMM2 = 43;
    private final int RANGE_START_STDMM6 = 45;
    private final int RANGE_END_STDMM6 = 51;
    private final int RANGE_START_DRAG = 53;
    private final int RANGE_END_DRAG = 61;
    private final int RANGE_START_EPHEMERIS = 62;
    private final int RANGE_END_EPHEMERIS = 63;
    private final int RANGE_START_INCLINATION = 8;
    private final int RANGE_END_INCLINATION = 17;
    private final int RANGE_START_ASCENSION = 17;
    private final int RANGE_END_ASCENSION = 25;
    private final int RANGE_START_ECCENTRICITY = 26;
    private final int RANGE_END_ECCENTRICITY = 33;
    private final int RANGE_START_PERIGEE = 34;
    private final int RANGE_END_PERIGEE = 43;
    private final int RANGE_START_MEAN_ANOM = 43;
    private final int RANGE_END_MEAN_ANOM = 51;
    private final int RANGE_START_MEAN_MOTION = 52;
    private final int RANGE_END_MEAN_MOTION = 62;
    private final int RANGE_START_REVOLUTIONS = 64;
    private final int RANGE_END_REVOLUTIONS = 69;
    
    private final int LINE_STEP = 3;
    
    /**
     * @param fileName
     *            the location of the TLE file
     * @throws IOException
     *             if the file cannot be opened or read
     * @throws ParseException
     *             if there are too many lines in the TLE file
     */
    public SatelliteReader(String fileName) throws IOException, ParseException
    {
        if(fileName == null)
        {
            throw new NullPointerException();
        }
        
        if(fileName.isEmpty())
        {
            throw new IllegalArgumentException("Empty file name");
        }
        
        File file = new File(fileName);
        
        if(!file.exists())
        {
            throw new IOException("File does not exist");
        }
        
        if(!file.canRead())
        {
            throw new IOException("Permission denied");
        }
        
        this.fileName = fileName;
        
        FileReader reader = new FileReader(file);
        BufferedReader buffReader = new BufferedReader(reader);
        
        String line = "";
        this.lines = new ArrayList<String>();
        
        while((line = buffReader.readLine()) != null)
        {
            this.lines.add(line);
        }
        
        buffReader.close();
        reader.close();
        
        // check file isn't empty
        if(this.lines.size() == 0)
        {
            throw new ParseException(this.ERR_MSG_EMPTY_FILE, 0);
        }
    }
    
    public List<Satellite> read() throws ParseException
    {
        List<Satellite> satellites = new ArrayList<Satellite>();
        int pos = 0;
        
        for(int i = 0; i < Math.floor(this.lines.size() / this.LINE_STEP); i++)
        {
            try
            {
                satellites.add(this.readEntry(i * this.LINE_STEP));
            }
            catch(ParseException e)
            {
                throw new ParseException(e.getMessage(),
                                pos + e.getErrorOffset());
            }
        }
        
        return satellites;
    }
    
    /**
     * Reads the TLE file associated with this object and returns a valid
     * <code>Satellite</code> object.
     * 
     * @param start
     *            the index of <code>this.lines</code> to start reading at
     *            the index of <code>this.lines</code> to stop reading at
     * @return satellite
     * @throws ParseException
     *             if the TLE file is invalid
     */
    public Satellite readEntry(int start) throws ParseException
    {
        // parameters to Satellite()
        String name = "";
        int number = 0;
        String classification = "";
        Calendar launchYear = new GregorianCalendar();
        int launchNum = 1;
        int launchPart = 1;
        Calendar epoch;
        float ftdmm2 = 0;
        float stdmm6 = 0;
        float drag = 0;
        int ephemeris = 0;
        float inclination = 0;
        float eccentricity = 0;
        float meanAnomaly = 0;
        float meanMotion = 0;
        float perigee = 0;
        int revolutions = 0;
        
        float ascension = 0;
        
        Satellite satellite = new Satellite();
        
        // parse line-by-line
        for(int i = 0; i + start < this.lines.size(); i++)
        {
            String currLine = lines.get(i + start);
            
            if(i == 0)
            {
                // satellite name
                name = currLine.substring(this.RANGE_START_SAT_NAME,
                                this.RANGE_END_SAT_NAME).trim();
                
                try
                {
                    satellite.setName(name);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(), 0);
                }
            }
            else if(i == 1)
            {
                // satellite number
                String numberString = currLine
                                .substring(this.RANGE_START_SAT_NUM,
                                                this.RANGE_END_SAT_NUM)
                                .toString().trim();
                
                try
                {
                    number = Integer.parseInt(numberString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_SAT_NUM,
                                    lines.get(start).length() + 3);
                }
                
                try
                {
                    satellite.setNum(number);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 3);
                }
                
                // classification
                classification = currLine
                                .substring(this.RANGE_START_CLASSIFICATION,
                                                this.RANGE_END_CLASSIFICATION)
                                .trim();
                
                try
                {
                    satellite.setClassification(classification);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 8);
                }
                
                // launch year
                String launchYearString = currLine
                                .substring(this.RANGE_START_LAUNCH_YEAR,
                                                this.RANGE_END_LAUNCH_YEAR)
                                .trim();
                
                int launchYearNum = SatelliteReader
                                .parseLaunchYear(launchYearString);
                
                launchYear.set(Calendar.YEAR, launchYearNum);
                
                try
                {
                    satellite.setLaunchYear(launchYear);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 12);
                }
                
                // launch number
                String launchNumString = currLine
                                .substring(this.RANGE_START_LAUNCH_NUM,
                                                this.RANGE_END_LAUNCH_NUM)
                                .trim();
                
                try
                {
                    launchNum = Integer.parseInt(launchNumString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_LAUNCH_NUM,
                                    lines.get(start).length() + 13);
                }
                
                try
                {
                    satellite.setLaunchNum(launchNum);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 13);
                }
                
                // launch part
                String launchPartString = currLine
                                .substring(this.RANGE_START_LAUNCH_PIECE,
                                                this.RANGE_END_LAUNCH_PIECE)
                                .trim();
                launchPart = SatelliteReader.parseLaunchPart(launchPartString);
                
                try
                {
                    satellite.setLaunchPiece(launchPart);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 15);
                }
                
                // epoch year
                String epochYearString = currLine
                                .substring(this.RANGE_START_EPOCH_YEAR,
                                                this.RANGE_END_EPOCH_YEAR)
                                .trim();
                
                // epoch day
                String epochDayString = currLine
                                .substring(this.RANGE_START_EPOCH_DAY,
                                                this.RANGE_END_EPOCH_DAY)
                                .trim();
                
                // build Date object for epoch
                epoch = SatelliteReader.parseEpoch(epochYearString,
                                epochDayString);
                
                if(epoch == null)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_EPOCH,
                                    lines.get(start).length() + 22);
                }
                
                try
                {
                    satellite.setEpoch(epoch);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 22);
                }
                
                // ftdmm2
                String ftdmm2String = currLine
                                .substring(this.RANGE_START_FTDMM2,
                                                this.RANGE_END_FTDMM2)
                                .trim();
                
                try
                {
                    ftdmm2 = Float.parseFloat(ftdmm2String);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_FTDMM2,
                                    lines.get(start).length() + 34);
                }
                
                try
                {
                    satellite.setFtdmm2(ftdmm2);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 34);
                }
                
                // stdmm6
                String stdmm6String = currLine
                                .substring(this.RANGE_START_STDMM6,
                                                this.RANGE_END_STDMM6)
                                .trim();
                
                try
                {
                    stdmm6 = SatelliteReader
                                    .parseHyphenatedDecimal(stdmm6String);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_STDMM6,
                                    lines.get(start).length() + 45);
                }
                
                try
                {
                    satellite.setStdmm6(stdmm6);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 45);
                }
                
                // BSTAR drag term
                String dragString = currLine.substring(this.RANGE_START_DRAG,
                                this.RANGE_END_DRAG).trim();
                
                try
                {
                    drag = SatelliteReader.parseHyphenatedDecimal(dragString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_DRAG,
                                    lines.get(start).length() + 54);
                }
                
                try
                {
                    satellite.setDrag(drag);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 54);
                }
                
                // ephemeris type
                String ephemerisString = currLine
                                .substring(this.RANGE_START_EPHEMERIS,
                                                this.RANGE_END_EPHEMERIS)
                                .trim();
                
                try
                {
                    ephemeris = Integer.parseInt(ephemerisString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_EPHEMERIS,
                                    lines.get(start).length() + 63);
                }
                
                try
                {
                    satellite.setEphemeris(ephemeris);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length() + 63);
                }
            }
            else if(i == 2)
            {
                // inclination
                String inclinationString = currLine
                                .substring(this.RANGE_START_INCLINATION,
                                                this.RANGE_END_INCLINATION)
                                .trim();
                
                try
                {
                    inclination = Float.parseFloat(inclinationString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_INCLINATION,
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 9);
                }
                
                try
                {
                    satellite.setInclination(inclination);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 9);
                }
                
                // right ascension of the ascending node
                String ascensionString = currLine.substring(
                                this.RANGE_START_ASCENSION,
                                this.RANGE_END_ASCENSION);
                
                try
                {
                    ascension = Float.parseFloat(ascensionString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_ASCENSION,
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 18);
                }
                
                try
                {
                    satellite.setRightAscension(ascension);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 18);
                }
                
                // eccentricity
                String eccentricityString = currLine.substring(
                                this.RANGE_START_ECCENTRICITY,
                                this.RANGE_END_ECCENTRICITY);
                
                try
                {
                    eccentricity = SatelliteReader
                                    .parseEccentricity(eccentricityString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_ECCENTRICITY,
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 27);
                }
                
                try
                {
                    satellite.setEccentricity(eccentricity);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 27);
                }
                
                // argument of perigee
                String perigeeString = currLine.substring(
                                this.RANGE_START_PERIGEE,
                                this.RANGE_END_PERIGEE);
                
                try
                {
                    perigee = Float.parseFloat(perigeeString);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_PERIGEE,
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 35);
                }
                
                try
                {
                    satellite.setPerigee(perigee);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 35);
                }
                
                // mean anomaly
                String meanAnomalyString = currLine.substring(
                                this.RANGE_START_MEAN_ANOM,
                                this.RANGE_END_MEAN_ANOM);
                
                try
                {
                    meanAnomaly = Float.parseFloat(meanAnomalyString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_MEAN_ANOM,
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 44);
                }
                
                try
                {
                    satellite.setMeanAnomaly(meanAnomaly);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 44);
                }
                
                // mean motion
                String meanMotionString = currLine.substring(
                                this.RANGE_START_MEAN_MOTION,
                                this.RANGE_END_MEAN_MOTION);
                
                try
                {
                    meanMotion = Float.parseFloat(meanMotionString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_MEAN_MOTION,
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 53);
                }
                
                try
                {
                    satellite.setMeanMotion(meanMotion);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 53);
                }
                
                // revolutions at epoch
                String revolutionsString = currLine
                                .substring(this.RANGE_START_REVOLUTIONS,
                                                this.RANGE_END_REVOLUTIONS)
                                .trim();
                
                try
                {
                    revolutions = Integer.parseInt(revolutionsString);
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(this.ERR_MSG_INVALID_REVS,
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 64);
                }
                
                try
                {
                    satellite.setRevolutions(revolutions);
                }
                catch(IllegalArgumentException e)
                {
                    throw new ParseException(e.getMessage(),
                                    lines.get(start).length()
                                                    + lines.get(start + 1)
                                                                    .length()
                                                    + 64);
                }
            }
        }
        
        return satellite;
    }
    
    private static int parseLaunchPart(String s)
    {
        if(s == null)
        {
            throw new NullPointerException();
        }
        
        if(s.isEmpty())
        {
            throw new IllegalArgumentException("Empty string");
        }
        
        if(s.length() > 3)
        {
            throw new IllegalArgumentException("String too long");
        }
        
        List<Integer> elems = new ArrayList<Integer>();
        
        for(int i = 0; i < s.length(); i++)
        {
            elems.add(s.charAt(i) - '@');
        }
        
        int sum = 0;
        
        for(int i = 0; i < elems.size(); i++)
        {
            sum += elems.get(i);
        }
        
        return sum;
    }
    
    private static int parseLaunchYear(String s)
    {
        if(s == null)
        {
            throw new NullPointerException();
        }
        
        if(s.isEmpty())
        {
            throw new IllegalArgumentException("Empty string");
        }
        
        if(s.length() > 2)
        {
            throw new IllegalArgumentException("String too long");
        }
        
        int launchYear = 0;
        
        Calendar current = new GregorianCalendar();
        int currentYearNum = current.get(Calendar.YEAR);
        
        if(Integer.parseInt(s) > currentYearNum % 1000)
        {
            launchYear = Integer.parseInt("19" + s);
        }
        else
        {
            launchYear = Integer.parseInt("20" + s);
        }
        
        return launchYear;
    }
    
    private static float parseHyphenatedDecimal(String s)
    {
        if(s == null)
        {
            throw new NullPointerException();
        }
        
        String formatted = "";
        
        // check if it's negative to avoid clobbering the sign indicator
        if(s.charAt(0) != '-')
        {
            formatted = s.replace('-', '.');
        }
        else
        {
            formatted = s.substring(1).replace('-', '.');
            formatted = "-" + formatted;
        }
        
        float stdmm6 = Float.parseFloat(formatted);
        
        return stdmm6;
    }
    
    private static Calendar parseEpoch(String yearString, String dayFracString)
    {
        if(yearString == null || dayFracString == null)
        {
            throw new NullPointerException();
        }
        
        int year = Integer.parseInt("20" + yearString);
        int day = 0;
        
        try
        {
            day = Integer.parseInt(dayFracString.split("\\.")[0]);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return null;
        }
        
        Calendar epoch = Calendar.getInstance();
        epoch.set(Calendar.YEAR, year);
        epoch.set(Calendar.DAY_OF_YEAR, day);
        
        return epoch;
    }
    
    private static float parseEccentricity(String s)
    {
        if(s == null)
        {
            throw new NullPointerException();
        }
        
        String formattedEccentricity = "0." + s;
        float eccentricity = 0;
        
        eccentricity = Float.parseFloat(formattedEccentricity);
        
        return eccentricity;
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
        result = prime * result + ((ERR_MSG_EMPTY_FILE == null) ? 0
                        : ERR_MSG_EMPTY_FILE.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_ASCENSION == null) ? 0
                        : ERR_MSG_INVALID_ASCENSION.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_DRAG == null) ? 0
                        : ERR_MSG_INVALID_DRAG.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_ECCENTRICITY == null) ? 0
                        : ERR_MSG_INVALID_ECCENTRICITY.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_EPHEMERIS == null) ? 0
                        : ERR_MSG_INVALID_EPHEMERIS.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_EPOCH == null) ? 0
                        : ERR_MSG_INVALID_EPOCH.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_FTDMM2 == null) ? 0
                        : ERR_MSG_INVALID_FTDMM2.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_INCLINATION == null) ? 0
                        : ERR_MSG_INVALID_INCLINATION.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_LAUNCH_NUM == null) ? 0
                        : ERR_MSG_INVALID_LAUNCH_NUM.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_LAUNCH_YEAR == null) ? 0
                        : ERR_MSG_INVALID_LAUNCH_YEAR.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_MEAN_ANOM == null) ? 0
                        : ERR_MSG_INVALID_MEAN_ANOM.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_MEAN_MOTION == null) ? 0
                        : ERR_MSG_INVALID_MEAN_MOTION.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_PERIGEE == null) ? 0
                        : ERR_MSG_INVALID_PERIGEE.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_REVS == null) ? 0
                        : ERR_MSG_INVALID_REVS.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_SAT_NUM == null) ? 0
                        : ERR_MSG_INVALID_SAT_NUM.hashCode());
        result = prime * result + ((ERR_MSG_INVALID_STDMM6 == null) ? 0
                        : ERR_MSG_INVALID_STDMM6.hashCode());
        result = prime * result + ((lines == null) ? 0 : lines.hashCode());
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
        SatelliteReader other = (SatelliteReader) obj;
        if(ERR_MSG_EMPTY_FILE == null)
        {
            if(other.ERR_MSG_EMPTY_FILE != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_EMPTY_FILE.equals(other.ERR_MSG_EMPTY_FILE))
        {
            return false;
        }
        if(ERR_MSG_INVALID_ASCENSION == null)
        {
            if(other.ERR_MSG_INVALID_ASCENSION != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_ASCENSION
                        .equals(other.ERR_MSG_INVALID_ASCENSION))
        {
            return false;
        }
        if(ERR_MSG_INVALID_DRAG == null)
        {
            if(other.ERR_MSG_INVALID_DRAG != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_DRAG.equals(other.ERR_MSG_INVALID_DRAG))
        {
            return false;
        }
        if(ERR_MSG_INVALID_ECCENTRICITY == null)
        {
            if(other.ERR_MSG_INVALID_ECCENTRICITY != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_ECCENTRICITY
                        .equals(other.ERR_MSG_INVALID_ECCENTRICITY))
        {
            return false;
        }
        if(ERR_MSG_INVALID_EPHEMERIS == null)
        {
            if(other.ERR_MSG_INVALID_EPHEMERIS != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_EPHEMERIS
                        .equals(other.ERR_MSG_INVALID_EPHEMERIS))
        {
            return false;
        }
        if(ERR_MSG_INVALID_EPOCH == null)
        {
            if(other.ERR_MSG_INVALID_EPOCH != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_EPOCH.equals(other.ERR_MSG_INVALID_EPOCH))
        {
            return false;
        }
        if(ERR_MSG_INVALID_FTDMM2 == null)
        {
            if(other.ERR_MSG_INVALID_FTDMM2 != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_FTDMM2.equals(other.ERR_MSG_INVALID_FTDMM2))
        {
            return false;
        }
        if(ERR_MSG_INVALID_INCLINATION == null)
        {
            if(other.ERR_MSG_INVALID_INCLINATION != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_INCLINATION
                        .equals(other.ERR_MSG_INVALID_INCLINATION))
        {
            return false;
        }
        if(ERR_MSG_INVALID_LAUNCH_NUM == null)
        {
            if(other.ERR_MSG_INVALID_LAUNCH_NUM != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_LAUNCH_NUM
                        .equals(other.ERR_MSG_INVALID_LAUNCH_NUM))
        {
            return false;
        }
        if(ERR_MSG_INVALID_LAUNCH_YEAR == null)
        {
            if(other.ERR_MSG_INVALID_LAUNCH_YEAR != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_LAUNCH_YEAR
                        .equals(other.ERR_MSG_INVALID_LAUNCH_YEAR))
        {
            return false;
        }
        if(ERR_MSG_INVALID_MEAN_ANOM == null)
        {
            if(other.ERR_MSG_INVALID_MEAN_ANOM != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_MEAN_ANOM
                        .equals(other.ERR_MSG_INVALID_MEAN_ANOM))
        {
            return false;
        }
        if(ERR_MSG_INVALID_MEAN_MOTION == null)
        {
            if(other.ERR_MSG_INVALID_MEAN_MOTION != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_MEAN_MOTION
                        .equals(other.ERR_MSG_INVALID_MEAN_MOTION))
        {
            return false;
        }
        if(ERR_MSG_INVALID_PERIGEE == null)
        {
            if(other.ERR_MSG_INVALID_PERIGEE != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_PERIGEE.equals(other.ERR_MSG_INVALID_PERIGEE))
        {
            return false;
        }
        if(ERR_MSG_INVALID_REVS == null)
        {
            if(other.ERR_MSG_INVALID_REVS != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_REVS.equals(other.ERR_MSG_INVALID_REVS))
        {
            return false;
        }
        if(ERR_MSG_INVALID_SAT_NUM == null)
        {
            if(other.ERR_MSG_INVALID_SAT_NUM != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_SAT_NUM.equals(other.ERR_MSG_INVALID_SAT_NUM))
        {
            return false;
        }
        if(ERR_MSG_INVALID_STDMM6 == null)
        {
            if(other.ERR_MSG_INVALID_STDMM6 != null)
            {
                return false;
            }
        }
        else if(!ERR_MSG_INVALID_STDMM6.equals(other.ERR_MSG_INVALID_STDMM6))
        {
            return false;
        }
        if(lines == null)
        {
            if(other.lines != null)
            {
                return false;
            }
        }
        else if(!lines.equals(other.lines))
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
        return "SatelliteReader[" + this.fileName + "]";
    }
}
