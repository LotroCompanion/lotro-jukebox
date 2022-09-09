package delta.games.lotro.gui.lore.sounds;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.lotro.jukebox.core.model.SoundDescription;
import delta.lotro.jukebox.core.model.SoundFormat;
import delta.lotro.jukebox.core.model.filter.SoundFormatFilter;
import delta.lotro.jukebox.core.model.filter.SoundNameFilter;

/**
 * Controller for a sound filter edition panel.
 * @author DAM
 */
public class SoundFilterController implements ActionListener
{
  // Data
  private SoundFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Emotes attributes UI --
  private JTextField _contains;
  private ComboBoxController<SoundFormat> _format;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public SoundFilterController(SoundFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<SoundDescription> getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
      setFilter();
      filterUpdated();
    }
    return _panel;
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  protected void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _format.selectItem(null);
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    SoundNameFilter nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Format
    SoundFormatFilter formatFilter=_filter.getFormatFilter();
    SoundFormat format=formatFilter.getFormat();
    _format.selectItem(format);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Sound attributes
    JPanel soundPanel=buildSoundPanel();
    Border border=GuiFactory.buildTitledBorder("Sound");
    soundPanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(soundPanel,c);

    // Reset
    _reset=GuiFactory.buildButton("Reset");
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildSoundPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      line1Panel.add(GuiFactory.buildLabel("Name filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      line1Panel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          SoundNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Profession
    {
      JLabel label=GuiFactory.buildLabel("Format:");
      line1Panel.add(label);
      _format=buildFormatCombobox();
      line1Panel.add(_format.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    return panel;
  }

  private ComboBoxController<SoundFormat> buildFormatCombobox()
  {
    ComboBoxController<SoundFormat> ctrl=new ComboBoxController<SoundFormat>();
    ctrl.addEmptyItem("");
    ctrl.addItem(SoundFormat.OGG_VORBIS,"OGG Vorbis");
    ctrl.addItem(SoundFormat.WAV,"WAV");
    ctrl.selectItem(null);
    ItemSelectionListener<SoundFormat> listener=new ItemSelectionListener<SoundFormat>()
    {
      @Override
      public void itemSelected(SoundFormat value)
      {
        SoundFormatFilter filter=_filter.getFormatFilter();
        filter.setFormat(value);
        filterUpdated();
      }
    };
    ctrl.addListener(listener);
    return ctrl;
  }

 /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_format!=null)
    {
      _format.dispose();
      _format=null;
    }
    _contains=null;
    _reset=null;
  }
}
