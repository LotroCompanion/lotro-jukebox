package delta.games.lotro.gui.lore.sounds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.gui.utils.DurationCellRenderer;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.lotro.jukebox.core.model.SoundDescription;
import delta.lotro.jukebox.core.model.SoundFormat;
import delta.lotro.jukebox.core.model.SoundsManager;

/**
 * Builder for a table that shows sounds.
 * @author DAM
 */
public class SoundsTableBuilder
{
  /**
   * Build a table with sounds.
   * @return the new table.
   */
  public static GenericTableController<SoundDescription> buildTable()
  {
    // Load data
    List<SoundDescription> sounds=loadSounds();

    // Build table
    ListDataProvider<SoundDescription> provider=new ListDataProvider<SoundDescription>(sounds);
    GenericTableController<SoundDescription> table=new GenericTableController<SoundDescription>(provider);
    List<DefaultTableColumnController<SoundDescription,?>> columns=buildColumns();
    for(DefaultTableColumnController<SoundDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    // Setup columns
    TableColumnsManager<SoundDescription> columnsManager=table.getColumnsManager();
    List<String> columnIds=getDefaultColumnIds();
    columnsManager.setDefaultColumnsIds(columnIds);
    columnsManager.setColumns(columnIds);

    // Init table
    /*JTable jtable=*/table.getTable();
    return table;
  }

  /**
   * Build the columns for a table of sounds.
   * @return A list of columns for a table of sounds.
   */
  private static List<DefaultTableColumnController<SoundDescription,?>> buildColumns()
  {
    List<DefaultTableColumnController<SoundDescription,?>> ret=new ArrayList<DefaultTableColumnController<SoundDescription,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<SoundDescription,Integer> idCell=new CellDataProvider<SoundDescription,Integer>()
      {
        @Override
        public Integer getData(SoundDescription sound)
        {
          return Integer.valueOf(sound.getIdentifier());
        }
      };
      DefaultTableColumnController<SoundDescription,Integer> idColumn=new DefaultTableColumnController<SoundDescription,Integer>(SoundColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<SoundDescription,String> nameCell=new CellDataProvider<SoundDescription,String>()
      {
        @Override
        public String getData(SoundDescription sound)
        {
          return sound.getName();
        }
      };
      DefaultTableColumnController<SoundDescription,String> nameColumn=new DefaultTableColumnController<SoundDescription,String>(SoundColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,-1,200);
      ret.add(nameColumn);
    }
    // Date
    {
      CellDataProvider<SoundDescription,Date> soundDateCell=new CellDataProvider<SoundDescription,Date>()
      {
        @Override
        public Date getData(SoundDescription sound)
        {
          long soundDate=sound.getTimestamp()*1000;
          return (soundDate!=0)?new Date(soundDate):null;
        }
      };
      DefaultTableColumnController<SoundDescription,Date> soundDateColumn=new DefaultTableColumnController<SoundDescription,Date>(SoundColumnIds.DATE.name(),"Date",Date.class,soundDateCell);
      ColumnsUtils.configureDateTimeColumn(soundDateColumn);
      ret.add(soundDateColumn);
    }
    // Format
    {
      CellDataProvider<SoundDescription,SoundFormat> formatCell=new CellDataProvider<SoundDescription,SoundFormat>()
      {
        @Override
        public SoundFormat getData(SoundDescription sound)
        {
          return sound.getFormat();
        }
      };
      DefaultTableColumnController<SoundDescription,SoundFormat> formatColumn=new DefaultTableColumnController<SoundDescription,SoundFormat>(SoundColumnIds.FORMAT.name(),"Format",SoundFormat.class,formatCell);
      formatColumn.setWidthSpecs(80,80,80);
      ret.add(formatColumn);
    }
    // Size
    {
      CellDataProvider<SoundDescription,Long> sizeCell=new CellDataProvider<SoundDescription,Long>()
      {
        @Override
        public Long getData(SoundDescription sound)
        {
          return Long.valueOf(sound.getRawSize());
        }
      };
      DefaultTableColumnController<SoundDescription,Long> sizeColumn=new DefaultTableColumnController<SoundDescription,Long>(SoundColumnIds.SIZE.name(),"Size",Long.class,sizeCell);
      ColumnsUtils.configureLongColumn(sizeColumn);
      ret.add(sizeColumn);
    }
    // Duration
    {
      CellDataProvider<SoundDescription,Integer> durationCell=new CellDataProvider<SoundDescription,Integer>()
      {
        @Override
        public Integer getData(SoundDescription sound)
        {
          return Integer.valueOf(sound.getDuration()/1000);
        }
      };
      DefaultTableColumnController<SoundDescription,Integer> durationColumn=new DefaultTableColumnController<SoundDescription,Integer>(SoundColumnIds.DURATION.name(),"Duration",Integer.class,durationCell);
      durationColumn.setWidthSpecs(80,100,80);
      durationColumn.setCellRenderer(new DurationCellRenderer());
      ret.add(durationColumn);
    }
    // Sample rate
    {
      CellDataProvider<SoundDescription,Float> sampleRateCell=new CellDataProvider<SoundDescription,Float>()
      {
        @Override
        public Float getData(SoundDescription sound)
        {
          return Float.valueOf(sound.getSampleRate());
        }
      };
      DefaultTableColumnController<SoundDescription,Float> sampleRateColumn=new DefaultTableColumnController<SoundDescription,Float>(SoundColumnIds.SAMPLE_RATE.name(),"Sample Rate",Float.class,sampleRateCell);
      sampleRateColumn.setWidthSpecs(80,100,80);
      ret.add(sampleRateColumn);
    }
    return ret;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(SoundColumnIds.ID.name());
    columnIds.add(SoundColumnIds.NAME.name());
    columnIds.add(SoundColumnIds.DATE.name());
    columnIds.add(SoundColumnIds.FORMAT.name());
    columnIds.add(SoundColumnIds.SIZE.name());
    columnIds.add(SoundColumnIds.DURATION.name());
    columnIds.add(SoundColumnIds.SAMPLE_RATE.name());
    columnIds.add(SoundColumnIds.TYPE.name());

    return columnIds;
  }

  private static List<SoundDescription> loadSounds()
  {
    List<SoundDescription> sounds=SoundsManager.getInstance().getAllSounds();
    return sounds;
  }
}
