package delta.games.lotro.gui.about;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.BrowserHyperlinkAction;
import delta.common.ui.swing.labels.HyperLinkController;

/**
 * Controller for a 'credits' panel.
 * @author DAM
 */
public class CreditsPanelController
{
  private JPanel _panel;

  /**
   * Constructor.
   */
  public CreditsPanelController()
  {
    // Nothing!
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c;

    int x=0;
    int y=0;

    // Icon (if any)
    String iconPath="/resources/gui/ring/ring48.png";
    Image icon=IconsManager.getImage(iconPath);
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,20,10,5),0,0);
    ImageIcon imageIcon=new ImageIcon(icon);
    JLabel lbIcon=new JLabel(imageIcon);
    panel.add(lbIcon,c);
    x++;

    // Credits
    JLabel lbName=new JLabel("Credits");
    lbName.setFont(lbName.getFont().deriveFont(Font.BOLD,36));
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,20),0,0);
    panel.add(lbName,c);
    y++;

    String[] labels=new String[]{
        "LOTRO-Wiki",
        "The Lord of Rings Online (Turbine/SSG)"
    };
    String[] urls=new String[]{
        "http://lotro-wiki.com/",
        "https://www.lotro.com/en"
    };

    // Credit lines
    int nbCredits=Math.min(labels.length,urls.length);
    for(int i=0;i<nbCredits;i++)
    {
      BrowserHyperlinkAction creditAction=new BrowserHyperlinkAction(urls[i],labels[i]);
      HyperLinkController creditLink=new HyperLinkController(creditAction);
      JLabel linkLabel=creditLink.getLabel();
      c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
      panel.add(linkLabel,c);
      y++;
    }

    return panel;
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
    }
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
