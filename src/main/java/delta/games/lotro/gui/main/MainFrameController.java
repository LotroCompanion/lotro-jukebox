package delta.games.lotro.gui.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.Preferences;
import delta.games.lotro.gui.about.AboutDialogController;
import delta.games.lotro.gui.about.CreditsDialogController;
import delta.games.lotro.gui.configuration.ConfigurationDialogController;
import delta.games.lotro.gui.misc.paypal.PaypalButtonController;
import delta.games.lotro.gui.player.JukeboxPlayerPanelController;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.utils.cfg.ApplicationConfiguration;
import delta.lotro.jukebox.core.config.LotroJukeboxCoreConfig;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController extends DefaultWindowController implements ActionListener
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="MAIN_WINDOW";

  private static final String SETTINGS_COMMAND="settingsCommand";
  private static final String ABOUT_COMMAND="aboutCommand";

  private JukeboxPlayerPanelController _jukeboxPanel;
  private ToolbarController _toolbarMisc;
  private PaypalButtonController _paypalButton;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _windowsManager=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    String appName=Labels.getLabel("main.window.title");
    frame.setTitle(appName);
    frame.pack();
    frame.setLocation(100,100);
    frame.getContentPane().setBackground(GuiFactory.getBackgroundColor());
    return frame;
  }

  @Override
  protected JMenuBar buildMenuBar()
  {
    JMenuBar menuBar=GuiFactory.buildMenuBar();
    JMenu fileMenu=GuiFactory.buildMenu(Labels.getLabel("main.window.menu.file"));
    JMenuItem quit=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.quit"));
    ActionListener alQuit=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doQuit();
      }
    };
    quit.addActionListener(alQuit);
    fileMenu.add(quit);
    menuBar.add(fileMenu);

    // Help
    JMenu helpMenu=GuiFactory.buildMenu(Labels.getLabel("main.window.menu.help"));
    // - about
    JMenuItem aboutMenuItem=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.about"));
    aboutMenuItem.setActionCommand(ABOUT_COMMAND);
    aboutMenuItem.addActionListener(this);
    helpMenu.add(aboutMenuItem);
    // - credits
    JMenuItem creditsMenuItem=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.credits"));
    ActionListener alCredits=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doCredits();
      }
    };
    creditsMenuItem.addActionListener(alCredits);
    helpMenu.add(creditsMenuItem);

    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(helpMenu);
    return menuBar;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JComponent buildContents()
  {
    // Jukebox
    _jukeboxPanel=new JukeboxPlayerPanelController(this);
    // Misc
    _toolbarMisc=buildToolBarMisc();
    // Paypal
    _paypalButton=new PaypalButtonController();

    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    JPanel jukeboxPanel=_jukeboxPanel.getPanel();
    jukeboxPanel.setBorder(GuiFactory.buildTitledBorder("Jukebox"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,0),0,0);
    panel.add(jukeboxPanel,c);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    panel.add(_toolbarMisc.getToolBar(),c);
    c.gridx++;
    c=new GridBagConstraints(c.gridx,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    panel.add(_paypalButton.getButton(),c);
    c.gridx++;
    return panel;
  }

  private ToolbarController buildToolBarMisc()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Settings
    String settingsIconPath=SharedUiUtils.getToolbarIconPath("settings");
    String settingsTooltip=Labels.getLabel("main.window.toolbar.settings.tooltip");
    String settingsAltText=Labels.getLabel("main.window.toolbar.settings.altText");
    ToolbarIconItem settingsIconItem=new ToolbarIconItem(SETTINGS_COMMAND,settingsIconPath,SETTINGS_COMMAND,settingsTooltip,settingsAltText);
    model.addToolbarIconItem(settingsIconItem);
    // About
    String aboutIconPath=SharedUiUtils.getToolbarIconPath("about");
    String aboutTooltip=Labels.getLabel("main.window.toolbar.about.tooltip");
    String aboutAltText=Labels.getLabel("main.window.toolbar.about.altText");
    ToolbarIconItem aboutIconItem=new ToolbarIconItem(ABOUT_COMMAND,aboutIconPath,ABOUT_COMMAND,aboutTooltip,aboutAltText);
    model.addToolbarIconItem(aboutIconItem);
    // Border
    String border=Labels.getLabel("main.window.toolbar.border.misc");
    controller.getToolBar().setBorder(GuiFactory.buildTitledBorder(border));
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  @Override
  protected void doWindowClosing()
  {
    doQuit();
  }

  private void doSettings()
  {
    ApplicationConfiguration configuration=ApplicationConfiguration.getInstance();
    ConfigurationDialogController dialog=new ConfigurationDialogController(this,configuration);
    dialog.getDialog().setLocationRelativeTo(this.getWindow());
    dialog.show(true);
  }

  @Override
  public void actionPerformed(ActionEvent event)
  {
    String cmd=event.getActionCommand();
    if (SETTINGS_COMMAND.equals(cmd))
    {
      doSettings();
    }
    else if (ABOUT_COMMAND.equals(cmd))
    {
      doAbout();
    }
  }

  private void doAbout()
  {
    String id=AboutDialogController.IDENTIFIER;
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      JFrame thisFrame=getFrame();
      controller=new AboutDialogController(this);
      _windowsManager.registerWindow(controller);
      Window w=controller.getWindow();
      w.setLocationRelativeTo(thisFrame);
      Point p=w.getLocation();
      w.setLocation(p.x+100,p.y+100);
    }
    controller.bringToFront();
  }

  private void doCredits()
  {
    String id=CreditsDialogController.IDENTIFIER;
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      JFrame thisFrame=getFrame();
      controller=new CreditsDialogController(this);
      _windowsManager.registerWindow(controller);
      Window w=controller.getWindow();
      w.setLocationRelativeTo(thisFrame);
    }
    controller.bringToFront();
  }

  private void doQuit()
  {
    String question=Labels.getLabel("main.window.quit.question");
    String title=Labels.getLabel("main.window.quit.title");
    int result=GuiFactory.showQuestionDialog(getFrame(),question,title,JOptionPane.YES_NO_OPTION);
    if (result==JOptionPane.OK_OPTION)
    {
      dispose();
    }
    Preferences preferences=LotroJukeboxCoreConfig.getInstance().getPreferences();
    preferences.saveAllPreferences();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    super.dispose();
    if (_jukeboxPanel!=null)
    {
      _jukeboxPanel.dispose();
      _jukeboxPanel=null;
    }
    if (_toolbarMisc!=null)
    {
      _toolbarMisc.dispose();
      _toolbarMisc=null;
    }
    if (_paypalButton!=null)
    {
      _paypalButton.dispose();
      _paypalButton=null;
    }
  }
}
