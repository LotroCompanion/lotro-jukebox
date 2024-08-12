package delta.games.lotro;

import java.util.Locale;

import javax.swing.JFrame;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.gui.main.MainFrameController;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.lotro.jukebox.core.config.LotroJukeboxCoreConfig;

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
    GuiFactory.setPreferences(LotroJukeboxCoreConfig.getInstance().getPreferences());
    Locale.setDefault(Locale.US);
    SharedUiUtils.initApplicationIcons();
    MainFrameController controller=new MainFrameController();
    JFrame frame=controller.getFrame();
    frame.setVisible(true);
  }
}
