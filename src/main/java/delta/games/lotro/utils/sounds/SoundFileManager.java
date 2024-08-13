package delta.games.lotro.utils.sounds;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.utils.dat.DatInterface;
import delta.lotro.jukebox.core.model.base.SoundDescription;

/**
 * Sound file manager.
 * @author DAM
 */
public class SoundFileManager
{
  private static final Logger LOGGER=LoggerFactory.getLogger(SoundFileManager.class);

  private File _tmpFile;

  /**
   * Constructor.
   */
  public SoundFileManager()
  {
    try
    {
      _tmpFile=File.createTempFile("lotrojukebox",null,new File("."));
      _tmpFile.deleteOnExit();
    }
    catch(Exception e)
    {
      LOGGER.warn("Could not create temp sound file!",e);
    }
  }

  /**
   * Get the managed temp file.
   * @return A temp file.
   */
  public File getTempFile()
  {
    return _tmpFile;
  }

  /**
   * Prepare for playing the given sound.
   * @param description Sound to play.
   * @return <code>true</code> if successful, <code>false</code> otherwise.
   */
  public boolean prepareFile(SoundDescription description)
  {
    DataFacade facade=DatInterface.getInstance().getFacade();
    boolean ok=SoundLoader.loadSound(facade,description.getIdentifier(),_tmpFile);
    return ok;
  }
}
