package delta.carto.geometry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import delta.carto.geodesy.GeodeticDatum;

/**
 * Represents a three-dimensional geographic point.
 * @author DAM
 */
public class Geo3DPoint extends Geo2DPoint
{
  private double _altitude;

  /**
   * Default constructor.
   * Datum is the default datum.
   * Geographic coordinates are set to the origin of this datum.
   * Altitude is set to zero.
   */
  public Geo3DPoint()
  {
    super();
  }

  /**
   * Constructor with geographic coordinates.
   * Datum is the default datum.
   * Altitude is set to zero.
   * @param latitude Latitude coordinate.
   * @param longitude Longitude coordinate.
   */
  public Geo3DPoint(double latitude, double longitude)
  {
    super(latitude, longitude);
  }

  /**
   * Constructor with geographic coordinates.
   * Datum is the default datum.
   * @param latitude Latitude coordinate.
   * @param longitude Longitude coordinate.
   * @param altitude Altitude.
   */
  public Geo3DPoint(double latitude, double longitude, double altitude)
  {
    super(latitude, longitude);
    _altitude=altitude;
  }

  /**
   * Full constructor.
   * @param latitude Latitude coordinate.
   * @param longitude Longitude coordinate.
   * @param altitude Altitude.
   * @param datum Datum for this point.
   */
  public Geo3DPoint(double latitude, double longitude, double altitude, GeodeticDatum datum)
  {
    super(latitude, longitude, datum);
    _altitude=altitude;
  }

  /**
   * Get the altitude of this point.
   * @return the altitude of this point;
   */
  public double getAltitude()
  {
    return _altitude;
  }

  /**
   * Writes the contents of this object to the specified stream
   * <code>out</code>.
   * @param out stream to write to.
   * @throws IOException may be raised while using the <code>out</code> stream.
   */
  @Override
  public void write(DataOutputStream out) throws IOException
  {
    super.write(out);
    out.writeDouble(_altitude);
  }

  /**
   * Reads the contents of this object from the specified stream
   * <code>in</code>, and sets this object's state to reflect read values.
   * @param in stream to read from.
   * @throws IOException may be raised while using the <code>in</code> stream.
   */
  @Override
  public void read(DataInputStream in) throws IOException
  {
    super.read(in);
    _altitude=in.readDouble();
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * @param o the object with which to compare.
   * @return <code>true</code> if this object is the same as the
   * <code>o</code> argument; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof Geo3DPoint)) return false;
    Geo3DPoint p=(Geo3DPoint)o;
    if (!super.equals(p)) return false;
    return (p.getAltitude()==_altitude);
  }

  @Override
  public int hashCode()
  {
    return super.hashCode()+Double.hashCode(_altitude);
  }

  /**
   * Returns a string representation of the object. Overrides the
   * <tt>toString</tt> method defined in class <tt>Object</tt> to offer a
   * displayable version of this object.
   * @return a string representation of the object.
   */
  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append('(');
    sb.append(getLatitude());
    sb.append(',');
    sb.append(getLongitude());
    sb.append(')');
    return sb.toString();
  }
}
