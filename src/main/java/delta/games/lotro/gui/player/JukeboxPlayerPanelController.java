package delta.games.lotro.gui.player;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaUtils;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.sounds.SoundChooser;
import delta.games.lotro.utils.sounds.SoundFileManager;
import delta.lotro.jukebox.core.model.base.SoundDescription;
import delta.soundplayer.api.SoundFormat;
import delta.soundplayer.ui.SoundPlayerPanelController;

/**
 * Jukebox panel controller.
 * @author DAM
 */
public class JukeboxPlayerPanelController extends AbstractPanelController
{
  private SoundPlayerPanelController _soundPlayer;
  private SoundFileManager _soundFileMgr;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public JukeboxPlayerPanelController(WindowController parent)
  {
    super(parent);
    _soundPlayer=new SoundPlayerPanelController();
    _soundFileMgr=new SoundFileManager();
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_soundPlayer.getPanel(),c);
    // Choose button
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JButton chooseSound=GuiFactory.buildButton("Choose...");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doChooseSound();
      }
    };
    chooseSound.addActionListener(al);
    ret.add(chooseSound,c);
    return ret;
  }

  private void doChooseSound()
  {
    WindowController parent=AreaUtils.findParentWindowController(this);
    SoundDescription sound=SoundChooser.chooseSound(parent);
    if (sound==null)
    {
      return;
    }
    boolean ok=_soundFileMgr.prepareFile(sound);
    if (!ok)
    {
      return;
    }
    File soundFile=_soundFileMgr.getTempFile();
    _soundPlayer.start(soundFile,getFormat(sound));
  }

  private SoundFormat getFormat(SoundDescription sound)
  {
    if (sound.getFormat()==delta.lotro.jukebox.core.model.base.SoundFormat.WAV) return SoundFormat.WAV;
    if (sound.getFormat()==delta.lotro.jukebox.core.model.base.SoundFormat.OGG_VORBIS) return SoundFormat.OGG_VORBIS;
    return null;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_soundPlayer!=null)
    {
      _soundPlayer.dispose();
      _soundPlayer=null;
    }
  }
}
