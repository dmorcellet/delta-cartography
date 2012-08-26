package delta.carto.geometry;

import delta.common.utils.math.Constants;
import delta.common.utils.math.geometry.Conversions;

/**
 * Tools related to geographic/cartographic computations.
 * @author DAM
 */
public abstract class Tools
{
  private static final double METERS_TO_NAUTICAL_MILES_FACTOR=1.0/(1852.0*60.0);
  private static final double NAUTICAL_MILES_TO_METERS_FACTOR=1852.0*60.0;
  private static final double METERS_TO_EARTH_SURFACE_RADIANS_FACTOR=Conversions.DEGREES_TO_RADIANS_FACTOR*METERS_TO_NAUTICAL_MILES_FACTOR;
  private static final double EARTH_SURFACE_RADIANS_TO_METERS_FACTOR=Conversions.RADIANS_TO_DEGREES_FACTOR*NAUTICAL_MILES_TO_METERS_FACTOR;

  /**
   * Computes the angle between North and the line going from point p1 to point
   * p2 (positive value means clockwise).
   * @param p1 First point.
   * @param p2 Second point.
   * @return The computed value (degrees).
   */
  public static double getAngle(Geo2DPoint p1, Geo2DPoint p2)
  {
    double ret;
    double dx=p2.getLongitude()-p1.getLongitude();
    double dy=p2.getLatitude()-p1.getLatitude();

    if(dx==0.0)
    {
      if(dy>=0.0)
      {
        ret=0.0;
      }
      else
      {
        ret=Math.PI;
      }
    }
    else
    {
      if(dx>0.0)
      {
        ret=Math.PI/2-Math.atan(dy/dx);
      }
      else
      {
        ret=3*Math.PI/2-Math.atan(dy/dy);
      }
    }
    ret=Conversions.radiansToDegrees(ret);

    return ret;
  }

  /**
   * Computes an orthodromic segmentation of an ellipse.
   * @param center Center of ellipse.
   * @param semiMajorAxisLength Length semi-major axis (meters).
   * @param semiMinorAxisLength Length semi-minor axis (meters).
   * @param heading Heading of ellipse (degrees from North).
   * @param nbSegments Number of segments/computed points.
   * @return The computed polygon.
   */
  public static GeoPolygon orthodromicEllipseSegmentation(Geo2DPoint center, double semiMajorAxisLength, double semiMinorAxisLength, double heading, int nbSegments)
  {
    GeoPolygon ret;

    if(semiMajorAxisLength==0)
    {
      ret=new GeoPolygon(1);
      ret.addPoint(center);
    }
    else
    {
      if(semiMinorAxisLength==0)
      {
        ret=new GeoPolygon(2);
        ret.addPoint(orthodromicExtension(center, semiMajorAxisLength, heading));
        heading+=180.0;
        ret.addPoint(orthodromicExtension(center, semiMajorAxisLength, heading));
      }
      else
      {
        ret=new GeoPolygon(nbSegments);
        // Normal case
        double step=Constants.PIx2/nbSegments;
        double rHeading=0.0;
        double rBaseHeading=Conversions.degreesToRadians(heading);
        double squareSemiMajorAxis=semiMajorAxisLength*semiMajorAxisLength;
        double squareSemiMinorAxis=semiMinorAxisLength*semiMinorAxisLength;
        for(int i=0;i<nbSegments;i++)
        {
          double cosa=Math.cos(rHeading-rBaseHeading);
          double sina=Math.sin(rHeading-rBaseHeading);
          double cosa2=cosa*cosa;
          double sina2=sina*sina;
          double length=1/Math.sqrt((cosa2/squareSemiMajorAxis)+(sina2/squareSemiMinorAxis));
          ret.addPoint(orthodromicExtension(center, length, Conversions.radiansToDegrees(rHeading)));
          rHeading+=step;
        }
      }
    }
    return ret;
  }

  /**
   * Computes an orthodromic segmentation of an arc.
   * @param center Center of arc.
   * @param radius Radius of arc (meters).
   * @param startHeading Heading of arc start (degrees from North).
   * @param angle Angle of arc (degrees).
   * @param nbSegments Number of segments/computed points.
   * @return The computed polygon.
   */
  public static GeoPolygon orthodromicArcSegmentation(Geo2DPoint center, double radius, double startHeading, double angle, int nbSegments)
  {
    GeoPolygon ret;

    if(radius<=0.0)
    {
      ret=new GeoPolygon(1);
      ret.addPoint(center);
    }
    else
    {
      ret=new GeoPolygon(nbSegments+1);
      double step=angle/nbSegments;
      double currentHeading=startHeading;
      for(int i=0;i<=nbSegments;i++)
      {
        ret.addPoint(orthodromicExtension(center, radius, currentHeading));
        currentHeading+=step;
      }
    }
    return ret;
  }

  /**
   * Computes the point located at a given distance from a given point, following
   * a given heading.
   * @param startPoint Point to start from.
   * @param length Distance to use (meters).
   * @param heading Heading to follow (degrees).
   * @return The computed point.
   */
  public static Geo2DPoint orthodromicExtension(Geo2DPoint startPoint, double length, double heading)
  {
    if(length<=0.0)
    {
      return startPoint;
    }

    // Convert everything to radians
    double rLat1=Conversions.degreesToRadians(startPoint.getLatitude());
    double rLong1=Conversions.degreesToRadians(startPoint.getLongitude());
    double rHeading=Conversions.degreesToRadians(heading);
    double rTheta=length*METERS_TO_EARTH_SURFACE_RADIANS_FACTOR;

    // Bring heading back to [0,2PI]
    while(rHeading>Constants.PIx2)
    {
      rHeading-=Constants.PIx2;
    }
    while(rHeading<0)
    {
      rHeading+=Constants.PIx2;
    }

    double rLat2=Math.asin(Math.sin(rLat1)*Math.cos(rTheta)+Math.cos(rLat1)*Math.sin(rTheta)*Math.cos(rHeading));
    double rLong2;

    // Dealing with special cases
    if((rLat1==Constants.PId2)||(rLat1==Constants.nPId2))
    {
      // Heading indicates the meridian to use
      rLong2=rHeading;
    }
    else
    {
      if((rLat2==Constants.PId2)||(rLat2==Constants.nPId2))
      {
        // We're following a meridian
        rLong2=rLong1;
      }
      // Normal case
      else
      {
        double dcos=(Math.cos(rTheta)-Math.sin(rLat1)*Math.sin(rLat2))/(Math.cos(rLat1)*Math.cos(rLat2));

        if((rHeading>=0)&&(rHeading<=Math.PI))
        {
          if(dcos>=0)
          {
            rLong2=rLong1+Math.acos(Math.min(dcos, 1));
          }
          else
          {
            rLong2=rLong1+Math.acos(Math.max(dcos, -1));
          }
        }
        else
        {
          if((rHeading>Math.PI)&&(rHeading<=Constants.PIx2))
          {
            if(dcos>=0)
            {
              rLong2=rLong1-Math.acos(Math.min(dcos, 1));
            }
            else
            {
              rLong2=rLong1-Math.acos(Math.max(dcos, -1));
            }
          }
          else
          {
            rLong2=0;
          }
        }
      }
    }

    double lat2=Conversions.radiansToDegrees(rLat2);
    double long2=Conversions.radiansToDegrees(rLong2);
    while(long2>180)
    {
      long2-=360;
    }
    while(long2<-180)
    {
      long2+=360;
    }

    return new Geo2DPoint(lat2, long2);
  }

  /**
   * Computes heading and distance between two points.
   * @param startPoint Start point.
   * @param endPoint End point.
   * @return The computed heading and distance.
   */
  public static HeadingDistance orthodromicHeadingDistance(Geo2DPoint startPoint, Geo2DPoint endPoint)
  {
    double rLat1=Conversions.degreesToRadians(startPoint.getLatitude());
    double rLat2=Conversions.degreesToRadians(endPoint.getLatitude());
    double dLong=Conversions.degreesToRadians(endPoint.getLongitude()-startPoint.getLongitude());

    double divide=Math.tan(rLat2)*Math.cos(rLat1)-Math.sin(rLat1)*Math.cos(dLong);

    double rv;
    if(divide!=0)
    {
      if((dLong==0)&&(rLat2-rLat1<0))
      {
        rv=Math.PI;
      }
      else
      {
        rv=nonIEEEfmod(Math.atan2(Math.sin(dLong), divide), Constants.PIx2);
      }
    }
    else
    {
      if(dLong>0)
      {
        rv=nonIEEEfmod(Constants.PId2, Constants.PIx2);
      }
      else
      {
        rv=nonIEEEfmod(Constants.nPId2, Constants.PIx2);
      }
    }

    double heading=Conversions.radiansToDegrees(rv);
    double distance;
    double dcos=Math.sin(rLat1)*Math.sin(rLat2)+Math.cos(rLat1)*Math.cos(rLat2)*Math.cos(dLong);
    if(dcos>=0)
    {
      distance=Math.acos(Math.min(dcos, 1));
    }
    else
    {
      distance=Math.acos(Math.max(dcos, -1));
    }

    distance*=EARTH_SURFACE_RADIANS_TO_METERS_FACTOR;

    return new HeadingDistance(heading, distance);
  }

  /**
   * Special modulo computed as follows
   * If x>0 and y>0 returns IEEEmodulo,
   * If x<0 and y<0 returns -IEEEmodulo,
   * If x<0 and y>0 returns y-IEEEmodulo,
   * If x>0 and y<0 returns y+IEEEmodulo.
   * @param x First operand.
   * @param y Second operand.
   * @return The computed value.
   */
  private static double nonIEEEfmod(double x, double y)
  {
    double mod=Math.IEEEremainder(Math.abs(x), Math.abs(y));
    if(x<0)
    {
      if(y<0)
      {
        mod=-mod;
      }
      else
      {
        mod=y-mod;
      }
    }
    else
    {
      if(y<0)
      {
        mod=y+mod;
      }
    }
    return mod;
  }
}
