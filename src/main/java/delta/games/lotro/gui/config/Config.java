package delta.games.lotro.gui.config;

import java.io.File;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;

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
    LotroCoreConfig coreConfig=LotroCoreConfig.getInstance();

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
    return LotroCoreConfig.getInstance().getParameters();
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
