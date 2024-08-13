package delta.games.lotro.utils.sounds;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.utils.io.FileIO;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.dat.data.SoundInfo;
import delta.games.lotro.dat.loaders.SoundInfoLoader;

/**
 * Utility method to load sound data from the DAT files.
 * @author DAM
 */
public class SoundLoader
{
  private static final Logger LOGGER=LoggerFactory.getLogger(SoundLoader.class);

  /**
   * Load a sound file.
   * @param facade Data facade.
   * @param soundInfoID Sound info ID. 
   * @param toFile File to write to.
   * @return <code>true</code> if successful, <code>false</code> otherwise.
   */
  public static boolean loadSound(DataFacade facade, int soundInfoID, File toFile)
  {
    byte[] data=facade.loadData(soundInfoID);
    if (data==null)
    {
      LOGGER.warn("Could not load sound ID="+soundInfoID);
      return false;
    }
    ByteArrayInputStream bis=new ByteArrayInputStream(data);
    SoundInfo soundInfo=SoundInfoLoader.decodeSoundInfo(bis);
    if (soundInfo==null)
    {
      LOGGER.warn("Could not decode sound ID="+soundInfoID);
      return false;
    }
    int soundID=soundInfo.getSoundID();
    byte[] soundData=facade.loadData(soundID);
    if (soundData==null)
    {
      LOGGER.warn("Could not load raw sound for sound ID="+soundInfoID);
      return false;
    }
    byte[] rawSoundData=SoundInfoLoader.decodeSound(new ByteArrayInputStream(soundData));
    if (rawSoundData==null)
    {
      LOGGER.warn("Could not decode sound for sound ID="+soundInfoID);
      return false;
    }
    boolean ok=FileIO.writeFile(toFile,rawSoundData);
    return ok;
  }
}
