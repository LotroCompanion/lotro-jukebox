package delta.games.lotro.gui.lore.sounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.lotro.jukebox.core.model.base.SoundDescription;
import delta.lotro.jukebox.core.model.base.filter.SoundDescriptionFilter;
import delta.lotro.jukebox.core.model.context.SoundContextKeys;
import delta.lotro.jukebox.core.model.context.SoundContextsFacade;
import delta.lotro.jukebox.core.model.context.SoundContextsManager;
import delta.lotro.jukebox.core.model.context.filter.SoundContextFilter;

/**
 * Sound filter.
 * @author DAM
 */
public class SoundFilter implements Filter<SoundDescription>
{
  private Filter<SoundDescription> _filter;
  private SoundDescriptionFilter _descriptionFilter;
  private Map<String,SoundContextFilter> _contextFilters;

  /**
   * Constructor.
   */
  public SoundFilter()
  {
    List<Filter<SoundDescription>> filters=new ArrayList<Filter<SoundDescription>>();
    _descriptionFilter=new SoundDescriptionFilter();
    filters.add(_descriptionFilter);
    _contextFilters=new HashMap<String,SoundContextFilter>();
    for(String contextKey : SoundContextKeys.KEYS)
    {
      SoundContextsManager mgr=SoundContextsFacade.getInstance().getContext(contextKey);
      SoundContextFilter filter=new SoundContextFilter(mgr);
      _contextFilters.put(contextKey,filter);
      filters.add(filter);
    }
    _filter=new CompoundFilter<SoundDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on sound description.
   * @return a sound desctiption filter.
   */
  public SoundDescriptionFilter getDescriptionFilter()
  {
    return _descriptionFilter;
  }

  /**
   * Get the filter for the given context.
   * @param contextKey Context key.
   * @return A sound context filter or <code>null</code> if not found.
   */
  public SoundContextFilter getSoundContextFilter(String contextKey)
  {
    return _contextFilters.get(contextKey);
  }

  @Override
  public boolean accept(SoundDescription sound)
  {
    return _filter.accept(sound);
  }
}
