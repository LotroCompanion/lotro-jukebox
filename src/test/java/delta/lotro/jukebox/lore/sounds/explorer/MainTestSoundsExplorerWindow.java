package delta.lotro.jukebox.lore.sounds.explorer;

import delta.games.lotro.gui.lore.sounds.explorer.SoundsExplorerWindowController;

/**
 * Test class for the sounds explorer window.
 * @author DAM
 */
public class MainTestSoundsExplorerWindow
{
  private void doIt()
  {
    SoundsExplorerWindowController ctrl=new SoundsExplorerWindowController(null);
    ctrl.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestSoundsExplorerWindow().doIt();
  }
}
