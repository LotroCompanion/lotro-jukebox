package delta.games.lotro.utils.cfg;

import java.io.File;

import delta.common.utils.ListenersManager;
import delta.games.lotro.dat.data.DatConfiguration;
import delta.lotro.jukebox.core.config.LotroJukeboxCoreConfig;
import delta.lotro.jukebox.core.config.UserConfig;
import delta.lotro.jukebox.core.config.labels.LabelsConfiguration;

/**
 * Configuration of the LotroCompanion application.
 * @author DAM
 */
public class ApplicationConfiguration
{
  // DAT
  private static final String DAT_CONFIGURATION="DatConfiguration";
  private static final String CLIENT_PATH="ClientPath";

  private static final ApplicationConfiguration _instance=new ApplicationConfiguration();
  private DatConfiguration _datConfiguration;
  private LabelsConfiguration _labelsConfiguration;
  private ListenersManager<ConfigurationListener> _listeners;

  /**
   * Get the application configuration.
   * @return the application configuration.
   */
  public static final ApplicationConfiguration getInstance()
  {
    return _instance;
  }

  /**
   * Constructor.
   */
  private ApplicationConfiguration()
  {
    initConfiguration();
    _listeners=new ListenersManager<ConfigurationListener>();
  }

  /**
   * Get the DAT configuration.
   * @return the DAT configuration.
   */
  public DatConfiguration getDatConfiguration()
  {
    return _datConfiguration;
  }

  /**
   * Get the labels configuration.
   * @return the labels configuration.
   */
  public LabelsConfiguration getLabelsConfiguration()
  {
    return _labelsConfiguration;
  }

  /**
   * Get the configuration listeners.
   * @return the configuration listeners.
   */
  public ListenersManager<ConfigurationListener> getListeners()
  {
    return _listeners;
  }

  private void initConfiguration()
  {
    _datConfiguration=new DatConfiguration();
    UserConfig config=UserConfig.getInstance();
    // DAT
    String clientPath=config.getStringValue(DAT_CONFIGURATION,CLIENT_PATH,null);
    if (clientPath!=null)
    {
      File rootPath=new File(clientPath);
      _datConfiguration.setRootPath(rootPath);
    }
    // Labels
    _labelsConfiguration=LotroJukeboxCoreConfig.getInstance().getLabelsConfiguration();
    // Save...
    saveConfiguration();
  }

  /**
   * Save configuration.
   */
  public void saveConfiguration()
  {
    UserConfig userCfg=UserConfig.getInstance();
    // LOTRO client path
    String clientPath=_datConfiguration.getRootPath().getAbsolutePath();
    userCfg.setStringValue(DAT_CONFIGURATION,CLIENT_PATH,clientPath);
    // Labels
    _labelsConfiguration.save(userCfg);
    // Save configuration
    UserConfig.getInstance().save();
  }

  /**
   * Called when the configuration has been updated.
   */
  public void configurationUpdated()
  {
    for(ConfigurationListener listener : _listeners)
    {
      listener.configurationUpdated(this);
    }
  }
}
