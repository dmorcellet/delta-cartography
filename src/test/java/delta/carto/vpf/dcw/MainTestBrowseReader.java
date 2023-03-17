package delta.carto.vpf.dcw;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Main class used to test the browse reader.
 * @author DAM
 */
public class MainTestBrowseReader
{
  private static final Logger LOGGER=Logger.getLogger(MainTestBrowseReader.class);

  /**
   * Constructor.
   */
  public MainTestBrowseReader()
  {
    String root="d:\\dada\\dev\\data\\DCW\\browse\\da";
    File dir=new File(root);
    handleDirectory(dir);
  }

  /**
   * Handle a directory.
   * @param directory Directory to handle.
   */
  private void handleDirectory(File directory)
  {
    File[] childs=directory.listFiles();
    for(int i=0;i<childs.length;i++)
    {
      if(childs[i].isDirectory())
      {
        handleDirectory(childs[i]);
      }
      else
      {
        String path=childs[i].getAbsolutePath();
        // Ignore :
        // Variable length index
        // Spatial index
        // Thematic index
        if(!(path.endsWith("x")||path.endsWith("si")||path.endsWith("ti")))
        {
          handleFile(path);
        }
      }
    }
  }

  /**
   * Handle a single data file.
   * @param path Path of file.
   */
  private void handleFile(String path)
  {
    try
    {
      FileInputStream fis=new FileInputStream(path);
      BufferedInputStream bis=new BufferedInputStream(fis);
      DataInputStream dis=new DataInputStream(bis);
      BrowseReader r=new BrowseReader();
      r.readHeader(dis);
      dis.close();
      bis.close();
      fis.close();
    }
    catch(IOException e)
    {
      LOGGER.error("",e);
    }
  }

  /**
   * Main method for this test.
   * @param args Useless.
   */
  public static void main(String[] args)
  {
    new MainTestBrowseReader();
  }
}
