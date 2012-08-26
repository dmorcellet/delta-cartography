package delta.carto.vpf.dcw;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import delta.carto.utils.CartoLoggers;

/**
 * Reader for browse geodata files.
 * @author DAM
 */
public class BrowseReader
{
  private static final Logger _logger=CartoLoggers.getCartoLogger();

  private static final byte DELIMITER=(byte)';';
  private static final byte DELIMITER2=(byte)'=';
  private static final byte DELIMITER3=(byte)',';

  /**
   * Constructor.
   */
  public BrowseReader()
  {
    // Nothing to do !
  }

  /**
   * Read bytes until a delimiter byte is reached.
   * @param dis Input stream to read from.
   * @param delimiter Delimiter byte.
   * @return A string made from the read bytes.
   * @throws IOException
   */
  private String readStringUntilDelimiter(DataInputStream dis, byte delimiter)
      throws IOException
  {
    StringBuffer sb=new StringBuffer();
    byte b;
    while(true)
    {
      b=dis.readByte();
      if(b!=delimiter)
      {
        sb.append((char)b);
      }
      else
      {
        break;
      }
    }
    return sb.toString();
  }

  /**
   * Read the header.
   * @param dis Input stream to read from.
   */
  public void readHeader(DataInputStream dis)
  {
    try
    {
      int headerSize=dis.readInt();
      System.out.println("headerSize=["+headerSize+"]");
      dis.readByte(); // Skip ';'
      String tableDescription=readStringUntilDelimiter(dis, DELIMITER);
      System.out.println("tableDescription=["+tableDescription+"]");
      String docFilename=readStringUntilDelimiter(dis, DELIMITER);
      System.out.println("docFilename=["+docFilename+"]");

      while(true)
      {
        String fieldName=readStringUntilDelimiter(dis, DELIMITER2);
        System.out.println("fieldName=["+fieldName+"]");
        String fieldType=readStringUntilDelimiter(dis, DELIMITER3).trim();
        System.out.println("fieldType=["+fieldType+"]");
        String number=readStringUntilDelimiter(dis, DELIMITER3).trim();
        System.out.println("number=["+number+"]");
        String keyType=readStringUntilDelimiter(dis, DELIMITER3).trim();
        System.out.println("keyType=["+keyType+"]");
        String fieldDescription=readStringUntilDelimiter(dis, DELIMITER3).trim();
        System.out.println("fieldDescription=["+fieldDescription+"]");
        String valueDescriptionTable=readStringUntilDelimiter(dis, DELIMITER3).trim();
        System.out.println("valueDescriptionTable=["+valueDescriptionTable+"]");
        String thematicIndex=readStringUntilDelimiter(dis, DELIMITER3).trim();
        System.out.println("thematicIndex=["+thematicIndex+"]");
        dis.readByte(); // Skip a byte
        dis.mark(1);
        if(dis.readByte()==DELIMITER) break;
        dis.reset();
      }
    }
    catch(IOException ioe)
    {
      _logger.error("",ioe);
    }
  }

  /*
      aft -> ../../../extern/data/dcw/browse/po/poarea.aft
      ebr -> ../../../extern/data/dcw/browse/po/ebr.
      edg -> ../../../extern/data/dcw/browse/po/edg.
      edx -> ../../../extern/data/dcw/browse/po/edx.
      end -> ../../../extern/data/dcw/browse/pp/end.
      lft -> ../../../extern/data/dcw/browse/po/poline.lft
      main -> ../../../extern/data/dcw/dht.
      vdt -> ../../../extern/data/dcw/browse/gr/char.vdt
   See table 38 for the format of *.*x files (index files).
   */

  /*
        //LireDHT(ld);
        //LireEND(ld);
        //LireCharVDT(ld);
        //LireEDG(ld);
        //LireLFT(ld);
        //LireAFT(ld);
        LireEBR(ld);
      }

      void
      LecteurVPF::LireDHT(LecteurDonnees& ld)
      {
        EntierNaturel32Bits id=LireEntier(ld);
        TRACE("Id = " << id);
        ChaineDeCaracteres databaseName=LireChaine(ld,8);
        TRACE("Database name [" << databaseName << "]");
        ChaineDeCaracteres databaseDesc=LireChaine(ld,100);
        TRACE("Database description [" << databaseDesc << "]");
        ChaineDeCaracteres mediaStandard=LireChaine(ld,20);
        TRACE("Media standard [" << mediaStandard << "]");
        ChaineDeCaracteres originator=LireChaine(ld,50);
        TRACE("Originator [" << originator << "]");
        ChaineDeCaracteres addressee=LireChaine(ld,100);
        TRACE("Addressee [" << addressee << "]");
        ChaineDeCaracteres mediaVolumes=LireChaine(ld,1);
        TRACE("Media volumes [" << mediaVolumes << "]");
        ChaineDeCaracteres seqNumbers=LireChaine(ld,1);
        TRACE("Seq numbers [" << seqNumbers << "]");
        ChaineDeCaracteres numDataSets=LireChaine(ld,1);
        TRACE("Number of data sets [" << numDataSets << "]");
        ChaineDeCaracteres securityClass=LireChaine(ld,1);
        TRACE("Security class [" << securityClass << "]");
        ChaineDeCaracteres downgrading=LireChaine(ld,3);
        TRACE("Downgrading [" << downgrading << "]");
        ChaineDeCaracteres downgradingDate=LireDate(ld);
        TRACE("Downgrading date [" << downgradingDate << "]");
        ChaineDeCaracteres releasibility=LireChaine(ld,20);
        TRACE("Releasibility [" << releasibility << "]");
        ChaineDeCaracteres otherStdName=LireChaine(ld,50);
        TRACE("Other std name [" << otherStdName << "]");
        ChaineDeCaracteres otherStdDate=LireDate(ld);
        TRACE("Other std date [" << otherStdDate << "]");
        ChaineDeCaracteres otherStdVersion=LireChaine(ld,10);
        TRACE("Other std version [" << otherStdVersion << "]");
        ChaineDeCaracteres transmittalId=LireChaine(ld,1);
        TRACE("TransmittalId [" << transmittalId << "]");
        ChaineDeCaracteres editionNumber=LireChaine(ld,10);
        TRACE("Edition number [" << editionNumber << "]");
        ChaineDeCaracteres editionDate=LireDate(ld);
        TRACE("Edition date [" << editionDate << "]");
      }

      void
      LecteurVPF::LireEND(LecteurDonnees& ld)
      {
        while (ld.FinDeFlux()==FAUX)
          {
            EntierNaturel32Bits id=LireEntier(ld);
            Reel x,y;
            LireCoordonnees(ld,x,y);
            TRACE("Id = " << id << " : (" << x << "," << y << ")");
          }
      }

      void
      LecteurVPF::LireEBR(LecteurDonnees& ld)
      {
        while (ld.FinDeFlux()==FAUX)
          {
            EntierNaturel32Bits id=LireEntier(ld);
            Reel xmin=LireReel(ld);
            Reel ymin=LireReel(ld);
            Reel xmax=LireReel(ld);
            Reel ymax=LireReel(ld);
            TRACE("Id = " << id << ": [(" << xmin << "," << ymin << ")->(" << xmax << "," << ymax << ")]");
          }
      }

      void
      LecteurVPF::LireIntVDT(LecteurDonnees& ld)
      {
        while (ld.FinDeFlux()==FAUX)
          {
            EntierNaturel32Bits id=LireEntier(ld);
            TRACE("Id = " << id);
            ChaineDeCaracteres name=LireChaine(ld,12);
            TRACE("   Name of feature table [" << name << "]");
            ChaineDeCaracteres attributeName=LireChaine(ld,16);
            TRACE("   Attribute name [" << attributeName << "]");
            EntierNaturel32Bits value=LireEntier(ld);
            TRACE("   Value = " << value);
            ChaineDeCaracteres description=LireChaine(ld,50);
            TRACE("   Description [" << description << "]");
          }
      }

      void
      LecteurVPF::LireCharVDT(LecteurDonnees& ld)
      {
        while (ld.FinDeFlux()==FAUX)
          {
            EntierNaturel32Bits id=LireEntier(ld);
            TRACE("Id = " << id);
            ChaineDeCaracteres name=LireChaine(ld,12);
            TRACE("   Name of feature table [" << name << "]");
            ChaineDeCaracteres attributeName=LireChaine(ld,16);
            TRACE("   Attribute name [" << attributeName << "]");
            ChaineDeCaracteres value=LireChaine(ld,3);
            TRACE("   Value = " << value);
            ChaineDeCaracteres description=LireChaine(ld,50);
            TRACE("   Description [" << description << "]");
          }
      }

      void
      LecteurVPF::LireLFT(LecteurDonnees& ld)
      {
        while (ld.FinDeFlux()==FAUX)
          {
            EntierNaturel32Bits id=LireEntier(ld);
            EntierNaturel32Bits type=LireEntier(ld);
            TRACE("Id = " << id << ", Type = " << type);
          }
      }

      void
      LecteurVPF::LireAFT(LecteurDonnees& ld)
      {
        while (ld.FinDeFlux()==FAUX)
          {
            EntierNaturel32Bits id=LireEntier(ld);
            EntierNaturel32Bits type=LireEntier(ld);
            TRACE("Id = " << id << ", Type = " << type);
          }
      }

      EntierNaturel32Bits
      LecteurVPF::LireEntier(LecteurDonnees& ld)
      {
        EntierNaturel32Bits retour;
        ld.Lire(retour);
        return retour;
      }

      Reel
      LecteurVPF::LireReel(LecteurDonnees& ld)
      {
        Reel retour;
        ld.Lire(retour);
        return retour;
      }

      ChaineDeCaracteres
      LecteurVPF::LireChaine(LecteurDonnees& ld, EntierNaturel32Bits taille)
      {
        ChaineDeCaracteres retour;
        for(EntierNaturel32Bits i=0;i<taille;i++)
          {
            OctetSigne c=ld.LireOctetSigne();
            retour+=c;
          }
        retour.SupprimerBlancs();
        return retour;
      }

      ChaineDeCaracteres
      LecteurVPF::LireDate(LecteurDonnees& ld)
      {
        return LireChaine(ld,20);
      }

      void
      LecteurVPF::LireCoordonnees(LecteurDonnees& ld, Reel& x, Reel& y)
      {
        ld.Lire(x);
        ld.Lire(y);
      }
    }
   */
}
