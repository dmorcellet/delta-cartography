package delta.carto.geodesy;

/**
 * Represents a geodetic datum. 
 * @author DAM
 */
public class GeodeticDatum
{
  private final String _id;

  /**
   * Package private constructor.
   * @param id Identifier for this geodetic datum.
   */
  GeodeticDatum(String id)
  {
    _id=id;
  }

  /**
   * Get the ID of this geodetic datum.
   * @return the ID of this geodetic datum.
   */
  public String getId()
  {
    return _id;
  }
}
