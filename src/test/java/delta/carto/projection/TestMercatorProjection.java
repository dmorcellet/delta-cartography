package delta.carto.projection;

import junit.framework.TestCase;

/**
 * Unit test class for the Mercator projection implementation.
 * @author DAM
 */
public class TestMercatorProjection extends TestCase
{
  /**
   * Constructor.
   */
  public TestMercatorProjection()
  {
    super("Mercator projection test");
  }
  
  /**
   * Test the Mercator projection of some points.
   */
  public void testMercatorProjection()
  {
    MercatorProjection p=new MercatorProjection(1000);

    {
      double[] x=
          {0, 180, -180, 180, -180, 0, 0};
      double[] x2=x.clone();
      double[] y=
          {0, 90, 90, -90, -90, 45, -45};
      double[] y2=y.clone();
      p.transform(x2, y2);

      for(int i=0;i<x.length;i++)
      {
        System.out.println("("+x[i]+","+y[i]+") -> ("+x2[i]+","+y2[i]+")");
      }
    }

    {
      double[] x=new double[90];
      double[] y=new double[90];
      for(int i=0;i<x.length;i++)
      {
        x[i]=4.0;
        y[i]=i;
      }
      double[] x2=x.clone();
      double[] y2=y.clone();
      p.transform(x2, y2);
      for(int i=0;i<x.length;i++)
      {
        System.out.println("("+x[i]+","+y[i]+") -> ("+x2[i]+","+y2[i]+")");
      }
    }
  }
}
