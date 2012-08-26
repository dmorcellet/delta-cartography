package delta.carto.utils;

import org.apache.log4j.Logger;

import delta.common.utils.traces.LoggersRegistry;

/**
 * Management class for all carto loggers.
 * @author DAM
 */
public abstract class CartoLoggers
{
  /**
   * Name of the "CARTO" logger.
   */
  public static final String CARTO="CARTO";

  private static final Logger _cartoLogger=LoggersRegistry.getLogger(CARTO);

  /**
   * Get the logger used for carto (CARTO).
   * @return the logger used for carto.
   */
  public static Logger getCartoLogger()
  {
    return _cartoLogger;
  }
}
