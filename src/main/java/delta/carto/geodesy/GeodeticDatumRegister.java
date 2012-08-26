package delta.carto.geodesy;

import java.util.HashMap;

/**
 * Registry for geodetic datums.
 * @author DAM
 */
public class GeodeticDatumRegister
{
  /**
   * The unique instance of this class.
   */
  private static GeodeticDatumRegister _instance;

  /**
   * Storage map for known geodetic datums.
   */
  private HashMap<String, GeodeticDatum> _knownDatums;

  /**
   * Default geodetic datum.
   */
  private GeodeticDatum _defaultDatum;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static GeodeticDatumRegister getInstance()
  {
    if(_instance==null)
    {
      _instance=new GeodeticDatumRegister();
    }
    return _instance;
  }

  /**
   * Private constructor.
   * The sole constructor of this class is private to
   * ensure that this class has at most a single instance.
   */
  private GeodeticDatumRegister()
  {
    _knownDatums=new HashMap<String, GeodeticDatum>();
    initKnownDatums();
  }

  /**
   * Get a geodetic datum by it's name (identifier).
   * @param id Identifier of the geodetic datum to get.
   * @return A geodetic datum or <code>null</code>.
   */
  public GeodeticDatum getGeodeticDatumByName(String id)
  {
    return _knownDatums.get(id);
  }

  /**
   * Register a new geodetic datum.
   * @param datum Geodetic datum to register.
   */
  void registerGeodeticDatum(GeodeticDatum datum)
  {
    _knownDatums.put(datum.getId(), datum);
  }

  /**
   * Get the default geodetic datum.
   * @return the default geodetic datum.
   */
  public GeodeticDatum getDefaultGeodeticDatum()
  {
    if(_defaultDatum==null)
    {
      _defaultDatum=new GeodeticDatum("WGS84");
      registerGeodeticDatum(_defaultDatum);
    }
    return _defaultDatum;
  }
  
  /**
   * Build and register known geodetic datums.
   */
  private void initKnownDatums()
  {
  	GeodeticDatum datum=new GeodeticDatum("NAD27");
  	registerGeodeticDatum(datum);
  }
}
