package delta.games.lotro;

import java.util.Locale;

import javax.swing.JFrame;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.L10nConfiguration;
import delta.common.utils.l10n.LocalizedFormats;
import delta.games.lotro.gui.config.Config;
import delta.games.lotro.gui.main.MainFrameController;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.utils.cfg.ApplicationConfiguration;

/**
 * Main for LOTRO jukebox.
 * @author DAM
 */
public class Main
{
  /**
   * Main method of LOTRO companion.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    GuiFactory.init();
    GuiFactory.setPreferences(Config.getInstance().getPreferences());
    L10nConfiguration l10nCfg=ApplicationConfiguration.getInstance().getL10nConfiguration();
    LocalizedFormats.init(l10nCfg);
    Locale.setDefault(Locale.US);
    SharedUiUtils.initApplicationIcons();
    MainFrameController controller=new MainFrameController();
    JFrame frame=controller.getFrame();
    frame.setVisible(true);
  }
}
