package delta.games.lotro.gui.utils;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.icons.ApplicationIcons;

/**
 * Shared UI utilities.
 * @author DAM
 */
public class SharedUiUtils
{
  /**
   * Get the path for a toolbar icon.
   * @param iconName Icon name.
   * @return A path.
   */
  public static String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  /**
   * Initialize the application icons.
   */
  public static void initApplicationIcons()
  {
    List<String> iconPaths=new ArrayList<String>();
    int[] sizes={16,32,48,256};
    for(int size : sizes)
    {
      String iconPath="/resources/gui/ring/ring"+size+".png";
      iconPaths.add(iconPath);
    }
    ApplicationIcons.setApplicationIconPaths(iconPaths);
  }
}
