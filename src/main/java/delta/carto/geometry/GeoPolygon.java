package delta.carto.geometry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import delta.carto.geodesy.GeodeticDatum;
import delta.carto.geodesy.GeodeticDatumRegister;

/**
 * Represents a geographic polygon.
 * @author DAM
 */
public class GeoPolygon
{
  private GeodeticDatum _datum;
  private double[] _latitudes;
  private double[] _longitudes;
  private int _nbPoints;

  /**
   * Default constructor.
   */
  public GeoPolygon()
  {
    this(10,GeodeticDatumRegister.getInstance().getDefaultGeodeticDatum());
  }

  /**
   * Constructor with a startup size for points storage.
   * @param size Initial size of points storage.
   */
  public GeoPolygon(int size)
  {
    this(size,GeodeticDatumRegister.getInstance().getDefaultGeodeticDatum());
  }

  /**
   * Full constructor.
   * @param size Initial size of points storage.
   * @param datum Datum of this shape.
   */
  public GeoPolygon(int size, GeodeticDatum datum)
  {
    _latitudes=new double[size];
    _longitudes=new double[size];
    _nbPoints=0;
    _datum=datum;
  }

  /**
   * Get the latitude of a point of this polygon.
   * @param pointIndex Index of desired point.
   * @return the latitude of the targeted point;
   */
  public double getLatitude(int pointIndex)
  {
    return _latitudes[pointIndex];
  }

  /**
   * Get the longitude of a point of this polygon.
   * @param pointIndex Index of desired point.
   * @return the longitude of the targeted point;
   */
  public double getLongitude(int pointIndex)
  {
    return _longitudes[pointIndex];
  }

  /**
   * Get the point at specified index.
   * @param pointIndex Index of desired point.
   * @return a copy of the internal point.
   */
  public Geo2DPoint getPoint(int pointIndex)
  {
    Geo2DPoint point=new Geo2DPoint(_latitudes[pointIndex],_longitudes[pointIndex],_datum);
    return point;
  }

  /**
   * Set the point at specified index.
   * @param latitude Latitude to set.
   * @param longitude Longitude to set.
   * @param pointIndex Targeted index.
   */
  public void setPoint(double latitude, double longitude, int pointIndex)
  {
    _latitudes[pointIndex]=latitude;
    _longitudes[pointIndex]=longitude;
  }

  /**
   * Set the point at specified index.
   * @param point Values to set.
   * @param pointIndex Targeted index.
   */
  public void setPoint(Geo2DPoint point, int pointIndex)
  {
    // todo Check datum
    _latitudes[pointIndex]=point.getLatitude();
    _longitudes[pointIndex]=point.getLongitude();
  }

  /**
   * Add a new point at the end of this polygon.
   * @param latitude Latitude of new point.
   * @param longitude Longitude of new point.
   */
  public void addPoint(double latitude, double longitude)
  {
    ensureSize(_nbPoints+1,true);
    _latitudes[_nbPoints]=latitude;
    _longitudes[_nbPoints]=longitude;
    _nbPoints++;
  }

  /**
   * Add a new point at the end of this polygon.
   * @param point Value of the new point.
   */
  public void addPoint(Geo2DPoint point)
  {
    // todo Check datum
    ensureSize(_nbPoints+1,true);
    _latitudes[_nbPoints]=point.getLatitude();
    _longitudes[_nbPoints]=point.getLongitude();
    _nbPoints++;
  }

  /**
   * Get the number of points in this polygon.
   * @return the number of points in this polygon.
   */
  public int getNumberOfPoints()
  {
    return _nbPoints;
  }

  /**
   * Get the geodetic datum used by this polygon.
   * @return the geodetic datum used by this polygon.
   */
  public GeodeticDatum getDatum()
  {
    return _datum;
  }

  /**
   * Ensure that the internal storage size is big enough to store
   * <code>nbPoints</code> points.
   * @param nbPoints Minimum number of points to store.
   * @param keepOldValues Indicates if point values should be kept if the
   * internal storage has to be extended.
   */
  private void ensureSize(int nbPoints, boolean keepOldValues)
  {
    if (_latitudes.length<nbPoints)
    {
      int newSize=_latitudes.length;
      while (newSize<nbPoints)
      {
        newSize*=2;
      }
      double[] newLatitudes=new double[newSize];
      double[] newLongitudes=new double[newSize];
      if (keepOldValues)
      {
        for(int i=0;i<nbPoints;i++)
        {
          newLatitudes[i]=_latitudes[i];
          newLongitudes[i]=_longitudes[i];
        }
      }
      _latitudes=newLatitudes;
      _longitudes=newLongitudes;
    }
  }

  /**
   * Writes the contents of this object to the specified stream <code>out</code>.
   * @param out stream to write to.
   * @throws IOException may be raised while using the <code>out</code>
   * stream.
   */
  public void write(DataOutputStream out) throws IOException
  {
    out.writeInt(_nbPoints);
    for(int i=0;i<_nbPoints;i++)
    {
      out.writeDouble(_latitudes[i]);
      out.writeDouble(_longitudes[i]);
    }
    out.writeUTF(_datum.getId());
  }

  /**
   * Reads the contents of this object from the specified stream <code>in</code>,
   * and sets this object's state to reflect read values.
   * @param in stream to read from.
   * @throws IOException may be raised while using the <code>in</code> stream.
   */
  public void read(DataInputStream in) throws IOException
  {
    int n=in.readInt();
    ensureSize(n,false);
    for(int i=0;i<n;i++)
    {
      _latitudes[i]=in.readDouble();
      _longitudes[i]=in.readDouble();
    }
    _nbPoints=n;
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
    if (!(o instanceof GeoPolygon)) return false;
    GeoPolygon p=(GeoPolygon)o;
    if (p.getDatum()!=_datum) return false;
    int nbPoints=p.getNumberOfPoints();
    if (nbPoints!=_nbPoints) return false;
    for(int i=0;i<nbPoints;i++)
    {
      if (p._latitudes[i]!=_latitudes[i]) return false;
      if (p._longitudes[i]!=_longitudes[i]) return false;
    }
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
    sb.append(_nbPoints);
    sb.append(" pts) ");
    for(int i=0;i<_nbPoints;i++)
    {
      sb.append('(');
      sb.append(_latitudes[i]);
      sb.append(',');
      sb.append(_longitudes[i]);
      sb.append(')');
    }
    return sb.toString();
  }
}
