package delta.carto.projection;

/**
 * Implements the Mercator projection.
 * @author DAM
 */
public class MercatorProjection
{
  private static final double PI_DIV_360=Math.PI/360;
  private static final double PI_DIV_4=Math.PI/4;
  private static final double MAX_ANGLE=(Math.PI/2)-0.017453292;
  private static final double MIN_ANGLE=0.017453292;

  private double _factor;

  /**
   * Default constructor.
   */
  public MercatorProjection()
  {
    _factor=1000.0;
  }

  /**
   * Constructor.
   * @param factor Factor.
   */
  public MercatorProjection(double factor)
  {
    _factor=factor;
  }

  /**
   * Get the factor used for computations.
   * @return the factor used for computations.
   */
  public double getFactor()
  {
    return _factor;
  }

  /**
   * Transform a series of latitude/longitude points.
   * @param x Longitudes.
   * @param y Latitudes.
   */
  public void transform(double[] x, double[] y)
  {
    assert x!=null;assert y!=null;
    assert x.length==y.length;

    int n=x.length;
    for(int i=0;i<n;i++)
    {
      x[i]=x[i]*_factor;
      // y between -90 and 90 degrees
      // angle between 0 and PI/2
      double angle=(y[i]*PI_DIV_360)+PI_DIV_4;

      // Check extreme values
      if(angle<MIN_ANGLE)
      {
        angle=MIN_ANGLE;
      }
      else
      {
        if(angle>MAX_ANGLE)
        {
          angle=MAX_ANGLE;
        }
      }

      y[i]=_factor*Math.log(Math.tan(angle));
    }
  }
}
