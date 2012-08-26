package delta.carto.geometry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import delta.carto.geodesy.GeodeticDatum;
import delta.carto.geodesy.GeodeticDatumRegister;

/**
 * Represents a geographic rectangle.
 * @author DAM
 */
public class GeoRectangle
{
  private GeodeticDatum _datum;
  private double _minLatitude;
  private double _maxLatitude;
  private double _minLongitude;
  private double _maxLongitude;

  /**
   * Default constructor.
   * Datum is the default datum.
   * Geographic coordinates are set to the origin of this datum.
   */
  public GeoRectangle()
  {
    GeodeticDatum datum=GeodeticDatumRegister.getInstance().getDefaultGeodeticDatum();
    init(0,0,0,0,datum);
  }

  /**
   * Constructor with geographic coordinates.
   * Datum is the default datum.
   * @param lat1 Latitude of point 1.
   * @param long1 Longitude of point 1.
   * @param lat2 Latitude of point 2.
   * @param long2 Longitude of point 2.
   */
  public GeoRectangle(double lat1, double long1, double lat2, double long2)
  {
    GeodeticDatum datum=GeodeticDatumRegister.getInstance().getDefaultGeodeticDatum();
    init(lat1,long1,lat2,long2,datum);
  }

  /**
   * Constructor with two points.
   * Datum is the default datum.
   * @param p1 Point 1.
   * @param p2 Point 2.
   */
  public GeoRectangle(Geo2DPoint p1, Geo2DPoint p2)
  {
    GeodeticDatum datum=GeodeticDatumRegister.getInstance().getDefaultGeodeticDatum();
    init(p1,p2,datum);
  }

  /**
   * Constructor with two points.
   * @param p1 Point 1.
   * @param p2 Point 2.
   * @param datum Datum of this shape.
   */
  public GeoRectangle(Geo2DPoint p1, Geo2DPoint p2, GeodeticDatum datum)
  {
    init(p1,p2,datum);
  }

  /**
   * Constructor with geographic coordinates.
   * @param lat1 Latitude of point 1.
   * @param long1 Longitude of point 1.
   * @param lat2 Latitude of point 2.
   * @param long2 Longitude of point 2.
   * @param datum Datum of this shape.
   */
  public GeoRectangle(double lat1, double long1, double lat2, double long2, GeodeticDatum datum)
  {
    init(lat1,long1,lat2,long2,datum);
  }

  private void init(Geo2DPoint p1, Geo2DPoint p2, GeodeticDatum datum)
  {
    // todo Check datum
    double lat1=p1.getLatitude();
    double long1=p1.getLongitude();
    double lat2=p2.getLatitude();
    double long2=p2.getLongitude();
    init(lat1,long1,lat2,long2,datum);
  }

  private void init(double lat1, double long1, double lat2, double long2, GeodeticDatum datum)
  {
    if (lat1<lat2)
    {
      _minLatitude=lat1;
      _maxLatitude=lat2;
    }
    else
    {
      _minLatitude=lat2;
      _maxLatitude=lat1;
    }
    if (long1<long2)
    {
      _minLongitude=long1;
      _maxLongitude=long2;
    }
    else
    {
      _minLongitude=long2;
      _maxLongitude=long1;
    }
    _datum=datum;
  }

  /**
   * Get the minimum latitude of this point.
   * @return the minimum latitude of this point;
   */
  public double getMinLatitude()
  {
    return _minLatitude;
  }

  /**
   * Get the maximum latitude of this point.
   * @return the maximum latitude of this point;
   */
  public double getMaxLatitude()
  {
    return _maxLatitude;
  }

  /**
   * Get the minimum longitude of this point.
   * @return the minimum longitude of this point;
   */
  public double getMinLongitude()
  {
    return _minLongitude;
  }

  /**
   * Get the maximum longitude of this point.
   * @return the maximum longitude of this point;
   */
  public double getMaxLongitude()
  {
    return _maxLongitude;
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
    out.writeDouble(_minLatitude);
    out.writeDouble(_maxLatitude);
    out.writeDouble(_minLongitude);
    out.writeDouble(_maxLongitude);
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
    _minLatitude=in.readDouble();
    _maxLatitude=in.readDouble();
    _minLongitude=in.readDouble();
    _maxLongitude=in.readDouble();
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
    if (!(o instanceof GeoRectangle)) return false;
    GeoRectangle r=(GeoRectangle)o;
    if (r._datum!=_datum) return false;
    if (r._maxLatitude!=_maxLatitude) return false;
    if (r._minLatitude!=_minLatitude) return false;
    if (r._maxLongitude!=_maxLongitude) return false;
    if (r._minLongitude!=_minLongitude) return false;
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
    sb.append(_minLatitude);
    sb.append(',');
    sb.append(_minLongitude);
    sb.append(") -> (");
    sb.append(_maxLatitude);
    sb.append(',');
    sb.append(_maxLongitude);
    sb.append(')');
    return sb.toString();
  }
}
