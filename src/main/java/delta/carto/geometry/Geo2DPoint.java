package delta.carto.geometry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import delta.carto.geodesy.GeodeticDatum;
import delta.carto.geodesy.GeodeticDatumRegister;

/**
 * Represents a single geographic point.
 * @author DAM
 */
public class Geo2DPoint
{
  private GeodeticDatum _datum;
  private double _latitude;
  private double _longitude;

  /**
   * Default constructor.
   * Datum is the default datum.
   * Geographic coordinates are set to the origin of this datum.
   */
  public Geo2DPoint()
  {
    _datum=GeodeticDatumRegister.getInstance().getDefaultGeodeticDatum();
  }

  /**
   * Constructor with geographic coordinates.
   * Datum is the default datum.
   * @param latitude Latitude coordinate.
   * @param longitude Longitude coordinate.
   */
  public Geo2DPoint(double latitude, double longitude)
  {
    _latitude=latitude;
    _longitude=longitude;
    _datum=GeodeticDatumRegister.getInstance().getDefaultGeodeticDatum();
  }

  /**
   * Full constructor.
   * @param latitude Latitude coordinate.
   * @param longitude Longitude coordinate.
   * @param datum Datum for this point.
   */
  public Geo2DPoint(double latitude, double longitude, GeodeticDatum datum)
  {
    _latitude=latitude;
    _longitude=longitude;
    _datum=datum;
  }

  /**
   * Get the latitude of this point.
   * @return the latitude of this point;
   */
  public double getLatitude()
  {
    return _latitude;
  }

  /**
   * Get the longitude of this point.
   * @return the longitude of this point;
   */
  public double getLongitude()
  {
    return _longitude;
  }

  /**
   * Get the geodetic datum used by this point.
   * @return the geodetic datum used by this point.
   */
  public GeodeticDatum getDatum()
  {
    return _datum;
  }

  /**
   * Writes the contents of this object to the specified stream
   * <code>out</code>.
   * @param out stream to write to.
   * @throws IOException may be raised while using the <code>out</code> stream.
   */
  public void write(DataOutputStream out) throws IOException
  {
    out.writeDouble(_latitude);
    out.writeDouble(_longitude);
    out.writeUTF(_datum.getId());
  }

  /**
   * Reads the contents of this object from the specified stream
   * <code>in</code>, and sets this object's state to reflect read values.
   * @param in stream to read from.
   * @throws IOException may be raised while using the <code>in</code> stream.
   */
  public void read(DataInputStream in) throws IOException
  {
    _latitude=in.readDouble();
    _longitude=in.readDouble();
    String datumID=in.readUTF();
    _datum=GeodeticDatumRegister.getInstance().getGeodeticDatumByName(datumID);
  }
  
  /**
   * Indicates whether some other object is "equal to" this one.
   * @param o the object with which to compare.
   * @return <code>true</code> if this object is the same as the
   * <code>o</code> argument; <code>false</code> otherwise.
   */
  public boolean equals(Object o)
  {
  	if (!(o instanceof Geo2DPoint)) return false;
  	Geo2DPoint p=(Geo2DPoint)o;
  	if (p.getDatum()!=_datum) return false;
  	if (p.getLatitude()!=_latitude) return false;
  	if (p.getLongitude()!=_longitude) return false;
  	return true;
  }
  
  /**
   * Returns a string representation of the object. Overrides the
   * <tt>toString</tt> method defined in class <tt>Object</tt> to offer a
   * displayable version of this object.
   * @return a string representation of the object.
   */
  public String toString()
  {
  	StringBuffer sb=new StringBuffer();
  	sb.append('(');
  	sb.append(_latitude);
  	sb.append(',');
  	sb.append(_longitude);
  	sb.append(')');
  	return sb.toString();
  }
}
