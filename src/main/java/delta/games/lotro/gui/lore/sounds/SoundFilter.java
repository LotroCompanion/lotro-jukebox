package delta.games.lotro.gui.lore.sounds;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.lotro.jukebox.core.model.base.SoundDescription;
import delta.lotro.jukebox.core.model.base.filter.SoundFormatFilter;
import delta.lotro.jukebox.core.model.base.filter.SoundNameFilter;

/**
 * Sound filter.
 * @author DAM
 */
public class SoundFilter implements Filter<SoundDescription>
{
  private Filter<SoundDescription> _filter;

  private SoundNameFilter _nameFilter;
  private SoundFormatFilter _formatFilter;

  /**
   * Constructor.
   */
  public SoundFilter()
  {
    List<Filter<SoundDescription>> filters=new ArrayList<Filter<SoundDescription>>();
    // Name
    _nameFilter=new SoundNameFilter();
    filters.add(_nameFilter);
    // Format
    _formatFilter=new SoundFormatFilter(null);
    filters.add(_formatFilter);
    _filter=new CompoundFilter<SoundDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on sound name.
   * @return an sound name filter.
   */
  public SoundNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on sound format.
   * @return a sound format filter.
   */
  public SoundFormatFilter getFormatFilter()
  {
    return _formatFilter;
  }

  @Override
  public boolean accept(SoundDescription sound)
  {
    return _filter.accept(sound);
  }
}
