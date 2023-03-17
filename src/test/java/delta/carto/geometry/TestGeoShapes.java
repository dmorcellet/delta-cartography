package delta.carto.geometry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import delta.carto.geodesy.GeodeticDatum;
import delta.carto.geodesy.GeodeticDatumRegister;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test class for geographic shapes.
 * @author DAM
 */
public class TestGeoShapes extends TestCase
{
  /**
   * Constructor.
   */
  public TestGeoShapes()
  {
  	super("GeoShapes test");
  }

  /**
   * Test some basic usage of geographic shapes.
   */
  public void testBasic()
  {
  	GeodeticDatum datum=GeodeticDatumRegister.getInstance().getGeodeticDatumByName("NAD27");

    Geo2DPoint geo=new Geo2DPoint(12, 13);
    Geo2DPoint geoRef=new Geo2DPoint(12, 13, datum);
    Assert.assertSame(datum,geoRef.getDatum());
    System.out.println("geoRef="+geoRef);
    GeoPolygon geoPolygon=new GeoPolygon();
    geoPolygon.addPoint(4, 5);
    geoPolygon.addPoint(2, 3);
    geoPolygon.addPoint(geo);
    Assert.assertEquals(3,geoPolygon.getNumberOfPoints());
    GeoRectangle geoRect=new GeoRectangle(2, 3, 4, 5);
    System.out.println("geoRect="+geoRect);
  }

  /**
   * Test geographic/cartographic computation tools.
   */
  public void testTools()
  {
    int nbPoints=10;
    {
      Geo2DPoint geo=new Geo2DPoint(12, 13);
      GeoPolygon ellipse=Tools.orthodromicEllipseSegmentation(geo, 100, 30, 45, nbPoints);
      System.out.println("Ellipse : "+ellipse);
      HeadingDistance hd=Tools.orthodromicHeadingDistance(geo, ellipse.getPoint(nbPoints-1));
      System.out.println("HeadingDistance : "+hd);
    }

    {
      Geo2DPoint geo=new Geo2DPoint(12, 13);
      GeoPolygon arc=Tools.orthodromicArcSegmentation(geo, 100, 30, 45, nbPoints);
      System.out.println("Arc : "+arc);
    }
  }

  /**
   * Test geographic shapes I/O.
   */
  public void testIO()
  {
  	try
  	{
    	GeodeticDatum datum=GeodeticDatumRegister.getInstance().getGeodeticDatumByName("NAD27");

    	// Build objects
	    Geo2DPoint geo=new Geo2DPoint(12, 13);
	    Geo2DPoint geoRef=new Geo2DPoint(12, 13, datum);
	    GeoPolygon geoPolygon=new GeoPolygon();
	    geoPolygon.addPoint(4, 5);
	    geoPolygon.addPoint(2, 3);
	    geoPolygon.addPoint(geo);
	    GeoRectangle rect=new GeoRectangle(2, 3, 4, 5);
	
	    // Output
	    ByteArrayOutputStream os=new ByteArrayOutputStream(100);
	    DataOutputStream dos=new DataOutputStream(os);
	    geo.write(dos);
	    geoRef.write(dos);
	    geoPolygon.write(dos);
	    rect.write(dos);
	
	    // Read from stream & check for equality
	    byte[] data=os.toByteArray();
	    ByteArrayInputStream is=new ByteArrayInputStream(data);
	    DataInputStream dis=new DataInputStream(is);
	
	    Geo2DPoint geo2=new Geo2DPoint();
	    geo2.read(dis);
	    Assert.assertEquals(geo,geo2);
	    Geo2DPoint geoRef2=new Geo2DPoint();
	    geoRef2.read(dis);
	    Assert.assertEquals(geoRef,geoRef2);
	    GeoPolygon geoPolygon2=new GeoPolygon();
	    geoPolygon2.read(dis);
	    Assert.assertEquals(geoPolygon,geoPolygon2);
	    GeoRectangle rect2=new GeoRectangle();
	    rect2.read(dis);
	    Assert.assertEquals(rect,rect2);
  	}
  	catch(IOException ioe)
  	{
  		Assert.assertTrue(false);
  	}
  }
}
