package delta.games.lotro.gui.lore.sounds;

import java.awt.Dimension;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;
import delta.lotro.jukebox.core.model.base.SoundDescription;

/**
 * Controller for a sound choice window.
 * @author DAM
 */
public class SoundChooser
{
  /**
   * Preference file for the columns of the sound chooser.
   */
  private static final String SOUND_CHOOSER_PROPERTIES_ID="SoundChooserColumn";

  /**
   * Choose a sound.
   * @param parentWindow Parent window.
   * @return A sound or <code>null</code>.
   */
  public static SoundDescription chooseSound(WindowController parentWindow)
  {
    //TypedProperties filterProps=parentWindow.getUserProperties("SoundFilter");
    SoundFilter filter=new SoundFilter();
    SoundFilterController filterController=new SoundFilterController(filter);
    TypedProperties props=parentWindow.getUserProperties(SoundChooser.SOUND_CHOOSER_PROPERTIES_ID);
    ObjectChoiceWindowController<SoundDescription> chooser=SoundChooser.buildChooser(parentWindow,props,filter,filterController);
    SoundDescription ret=chooser.editModal();
    return ret;
  }

  /**
   * Build a sound chooser window.
   * @param parent Parent controller.
   * @param prefs Preferences for this window.
   * @param filter Filter to use.
   * @param filterController Filter UI to use.
   * @return the newly built chooser.
   */
  public static ObjectChoiceWindowController<SoundDescription> buildChooser(WindowController parent, TypedProperties prefs, Filter<SoundDescription> filter, ObjectFilterPanelController filterController)
  {
    // Table
    GenericTableController<SoundDescription> soundsTable=SoundsTableBuilder.buildTable();

    // Build and configure chooser
    final ObjectChoiceWindowController<SoundDescription> chooser=new ObjectChoiceWindowController<SoundDescription>(parent,prefs,soundsTable);
    // Filter
    chooser.setFilter(filter,filterController);
    JDialog dialog=chooser.getDialog();
    // Title
    dialog.setTitle("Choose sound:");
    // Dimension
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(1000,dialog.getHeight());
    return chooser;
  }
}
