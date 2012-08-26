package delta.carto.geometry;

/**
 * Represents a heading and distance pair.
 * @author DAM
 */
public class HeadingDistance
{
  private double _heading;
  private double _distance;

  /**
   * Constructor.
   * @param heading Heading.
   * @param distance Distance.
   */
  public HeadingDistance(double heading, double distance)
  {
    _heading=heading;
    _distance=distance;
  }

  /**
   * Get the value for heading.
   * @return the value for heading.
   */
  public double getHeading()
  {
    return _heading;
  }

  /**
   * Get the value for distance.
   * @return the value for distance.
   */
  public double getDistance()
  {
    return _distance;
  }

  /**
   * Returns a string representation of the object. Overrides the
   * <tt>toString</tt> method defined in class <tt>Object</tt> to offer a
   * displayable version of this object.
   * @return a string representation of the object.
   */
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
  	sb.append('(');
  	sb.append(_heading);
  	sb.append(',');
  	sb.append(_distance);
  	sb.append(')');
  	return sb.toString();
  }
}
