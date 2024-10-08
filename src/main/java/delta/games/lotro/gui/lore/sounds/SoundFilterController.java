package delta.games.lotro.gui.lore.sounds;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;
import delta.lotro.jukebox.core.config.DataFiles;
import delta.lotro.jukebox.core.model.base.SoundDescription;
import delta.lotro.jukebox.core.model.base.SoundFormat;
import delta.lotro.jukebox.core.model.base.filter.SoundFormatFilter;
import delta.lotro.jukebox.core.model.base.filter.SoundNameFilter;
import delta.lotro.jukebox.core.model.context.SoundContext;
import delta.lotro.jukebox.core.model.context.SoundContextKeys;
import delta.lotro.jukebox.core.model.context.SoundContextsFacade;
import delta.lotro.jukebox.core.model.context.SoundContextsManager;
import delta.lotro.jukebox.core.model.context.filter.SoundContextFilter;
import delta.lotro.jukebox.core.utils.NamedComparator;

/**
 * Controller for a sound filter edition panel.
 * @author DAM
 */
public class SoundFilterController extends ObjectFilterPanelController implements ActionListener
{
  // Data
  private SoundFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Emotes attributes UI --
  private JTextField _contains;
  private ComboBoxController<SoundFormat> _format;
  private List<ComboBoxController<SoundContext>> _contexts;
  // Controllers
  private DynamicTextEditionController _textController;

  /**
   * Constructor.
   * @param filter Managed filter.
   */
  public SoundFilterController(SoundFilter filter)
  {
    _filter=filter;
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

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _format.selectItem(null);
      _contains.setText("");
      for(ComboBoxController<SoundContext> contextCtrl : _contexts)
      {
        contextCtrl.selectItem(null);
      }
    }
  }

  private void setFilter()
  {
    // Name
    SoundNameFilter nameFilter=_filter.getDescriptionFilter().getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Format
    SoundFormatFilter formatFilter=_filter.getDescriptionFilter().getFormatFilter();
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
          SoundNameFilter nameFilter=_filter.getDescriptionFilter().getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Format
    {
      JLabel label=GuiFactory.buildLabel("Format:");
      line1Panel.add(label);
      _format=buildFormatCombobox();
      line1Panel.add(_format.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;
    // Contexts
    _contexts=new ArrayList<ComboBoxController<SoundContext>>();
    for(String contextKey : SoundContextKeys.KEYS)
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      ComboBoxController<SoundContext> comboBoxController=buildContextCombobox(contextKey);
      _contexts.add(comboBoxController);
      String contextLabel=getContextLabel(contextKey);
      JLabel label=GuiFactory.buildLabel(contextLabel+":");
      linePanel.add(label);
      linePanel.add(comboBoxController.getComboBox());

      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    return panel;
  }

  private String getContextLabel(String contextKey)
  {
    if (DataFiles.AREAS.equals(contextKey)) return "Areas";
    if (DataFiles.DUNGEONS.equals(contextKey)) return "Dungeons";
    if (DataFiles.INSTRUMENTS.equals(contextKey)) return "Instruments";
    if (DataFiles.MUSIC_ITEMS.equals(contextKey)) return "Music items";
    return "?";
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
        SoundFormatFilter filter=_filter.getDescriptionFilter().getFormatFilter();
        filter.setFormat(value);
        filterUpdated();
      }
    };
    ctrl.addListener(listener);
    return ctrl;
  }

  private ComboBoxController<SoundContext> buildContextCombobox(String soundContext)
  {
    SoundContextsManager mgr=SoundContextsFacade.getInstance().getContext(soundContext);
    ComboBoxController<SoundContext> ctrl=new ComboBoxController<SoundContext>();
    ctrl.addEmptyItem("");
    List<SoundContext> contexts=mgr.getAllSoundContexts();
    Collections.sort(contexts,new NamedComparator<SoundContext>());
    for(SoundContext context : contexts)
    {
      ctrl.addItem(context,context.getName());
    }
    ctrl.selectItem(null);
    ItemSelectionListener<SoundContext> listener=new ItemSelectionListener<SoundContext>()
    {
      @Override
      public void itemSelected(SoundContext value)
      {
        SoundContextFilter filter=_filter.getSoundContextFilter(soundContext);
        Integer contextID=null;
        if (value!=null)
        {
          contextID=Integer.valueOf(value.getIdentifier());
        }
        filter.setContextID(contextID);
        filterUpdated();
      }
    };
    ctrl.addListener(listener);
    return ctrl;
  }

  @Override
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
    if (_contexts!=null)
    {
      for(ComboBoxController<SoundContext> ctrl : _contexts)
      {
        ctrl.dispose();
      }
      _contexts=null;
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
