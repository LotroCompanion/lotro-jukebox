package delta.games.lotro.gui.config;

import java.io.File;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.lotro.jukebox.core.config.LotroJukeboxCoreConfig;

/**
 * Configuration.
 * @author DAM
 */
public final class Config
{
  private static Config _instance=new Config();

  private Preferences _preferences;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static Config getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private Config()
  {
    LotroJukeboxCoreConfig coreConfig=LotroJukeboxCoreConfig.getInstance();

    // User data
    File userDir=coreConfig.getUserDataDir();
    // - preferences
    File preferencesDir=new File(userDir,"preferences");
    _preferences=new Preferences(preferencesDir);
  }

  /**
   * Get the configuration parameters.
   * @return the configuration parameters.
   */
  public TypedProperties getParameters()
  {
    return LotroJukeboxCoreConfig.getInstance().getParameters();
  }

  /**
   * Get the preferences manager.
   * @return the preferences manager.
   */
  public Preferences getPreferences()
  {
    return _preferences;
  }
}
