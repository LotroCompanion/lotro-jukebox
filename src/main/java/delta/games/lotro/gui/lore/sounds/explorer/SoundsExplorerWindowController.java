package delta.games.lotro.gui.lore.sounds.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.sounds.SoundFilter;
import delta.games.lotro.gui.lore.sounds.SoundFilterController;
import delta.games.lotro.gui.lore.sounds.SoundsTableBuilder;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.lotro.jukebox.core.model.SoundDescription;

/**
 * Controller for the sounds explorer window.
 * @author DAM
 */
public class SoundsExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="SOUNDS_EXPLORER";

  private SoundFilterController _filterController;
  private GenericTablePanelController<SoundDescription> _panelController;
  private GenericTableController<SoundDescription> _tableController;
  private SoundFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public SoundsExplorerWindowController(WindowController parent)
  {
    super(parent);
    _filter=new SoundFilter();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Sounds explorer");
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(950,700);
    return frame;
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
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    _tableController=SoundsTableBuilder.buildTable();
    // - filter
    _tableController.setFilter(_filter);
    // - preferences
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("SoundsExplorer");
    _tableController.getPreferencesManager().setPreferences(prefs);
    // Table panel
    _panelController=new GenericTablePanelController<SoundDescription>(this,_tableController);
    _panelController.getConfiguration().setBorderTitle("Emotes");
    JPanel tablePanel=_panelController.getPanel();
    // Filter UI
    _filterController=new SoundFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(null),c);
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
