package delta.games.lotro.gui.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.lotro.jukebox.core.config.labels.AvailableLabelsDefinition;
import delta.lotro.jukebox.core.config.labels.DefinitionOfAvailableLabels;
import delta.lotro.jukebox.core.config.labels.LabelsConfiguration;
import delta.lotro.jukebox.core.config.labels.LabelsEntry;

/**
 * Controller for a panel to edit the labels configuration.
 * @author DAM
 */
public class LabelsConfigurationPanelController extends AbstractPanelController
{
  private DefinitionOfAvailableLabels _availableCfgs;
  private ComboBoxController<LabelsEntry> _appLabelsCB;

  /**
   * Constructor.
   */
  public LabelsConfigurationPanelController()
  {
    super();
    _availableCfgs=new DefinitionOfAvailableLabels();
    _appLabelsCB=buildComboBox(_availableCfgs.getAppLabels());
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Application labels
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(GuiFactory.buildLabel("Application labels:"),c); // I18n
    c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_appLabelsCB.getComboBox(),c);
    panel.setBorder(GuiFactory.buildTitledBorder("Labels")); // I18n
    return panel;
  }

  /**
   * Set the configuration to show.
   * @param cfg Configuration to show.
   */
  public void setConfig(LabelsConfiguration cfg)
  {
    // Application labels
    LabelsEntry appEntry=_availableCfgs.getAppLabels().getEntryByKey(cfg.getAppLabelsKey());
    if (appEntry==null)
    {
      appEntry=_availableCfgs.getAppLabels().getDefault();
    }
    _appLabelsCB.selectItem(appEntry);
  }

  /**
   * Save the configuration to the given storage.
   * @param cfg Storage to use.
   */
  public void saveTo(LabelsConfiguration cfg)
  {
    cfg.setAppLabelsKey(_appLabelsCB.getSelectedItem().getKey());
  }

  private ComboBoxController<LabelsEntry> buildComboBox(AvailableLabelsDefinition cfg)
  {
    ComboBoxController<LabelsEntry> ret=new ComboBoxController<LabelsEntry>();
    List<LabelsEntry> entries=cfg.getEntries();
    for(LabelsEntry entry : entries)
    {
      ret.addItem(entry,entry.getLabel());
    }
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _availableCfgs=null;
    if (_appLabelsCB!=null)
    {
      _appLabelsCB.dispose();
      _appLabelsCB=null;
    }
  }
}