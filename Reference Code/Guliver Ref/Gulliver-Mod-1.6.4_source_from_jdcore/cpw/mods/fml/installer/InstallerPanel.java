package cpw.mods.fml.installer;

import com.google.common.base.Throwables;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


public class InstallerPanel
  extends JPanel
{
  private File targetDir;
  private ButtonGroup choiceButtonGroup;
  private JTextField selectedDirText;
  private JLabel infoLabel;
  private JButton sponsorButton;
  private JDialog dialog;
  private JPanel sponsorPanel;
  private JPanel fileEntryPanel;
  
  private class FileSelectAction
    extends AbstractAction
  {
    private FileSelectAction() {}
    
    public void actionPerformed(ActionEvent e)
    {
      JFileChooser dirChooser = new JFileChooser();
      dirChooser.setFileSelectionMode(1);
      dirChooser.setFileHidingEnabled(false);
      dirChooser.ensureFileIsVisible(targetDir);
      dirChooser.setSelectedFile(targetDir);
      int response = dirChooser.showOpenDialog(InstallerPanel.this);
      switch (response)
      {
      case 0: 
        targetDir = dirChooser.getSelectedFile();
        InstallerPanel.this.updateFilePath();
        break;
      }
      
    }
  }
  

  private class SelectButtonAction
    extends AbstractAction
  {
    private SelectButtonAction() {}
    
    public void actionPerformed(ActionEvent e)
    {
      InstallerPanel.this.updateFilePath();
    }
  }
  
  public InstallerPanel(File targetDir)
  {
    setLayout(new BoxLayout(this, 1));
    BufferedImage image;
    try
    {
      image = ImageIO.read(SimpleInstaller.class.getResourceAsStream(VersionInfo.getLogoFileName()));
    }
    catch (IOException e)
    {
      throw Throwables.propagate(e);
    }
    
    JPanel logoSplash = new JPanel();
    logoSplash.setLayout(new BoxLayout(logoSplash, 1));
    ImageIcon icon = new ImageIcon(image);
    JLabel logoLabel = new JLabel(icon);
    logoLabel.setAlignmentX(0.5F);
    logoLabel.setAlignmentY(0.5F);
    logoLabel.setSize(image.getWidth(), image.getHeight());
    logoSplash.add(logoLabel);
    JLabel tag = new JLabel(VersionInfo.getWelcomeMessage());
    tag.setAlignmentX(0.5F);
    tag.setAlignmentY(0.5F);
    logoSplash.add(tag);
    tag = new JLabel(VersionInfo.getVersion());
    tag.setAlignmentX(0.5F);
    tag.setAlignmentY(0.5F);
    logoSplash.add(tag);
    
    logoSplash.setAlignmentX(0.5F);
    logoSplash.setAlignmentY(0.0F);
    add(logoSplash);
    
    sponsorPanel = new JPanel();
    sponsorPanel.setLayout(new BoxLayout(sponsorPanel, 0));
    sponsorPanel.setAlignmentX(0.5F);
    sponsorPanel.setAlignmentY(0.5F);
    






    sponsorButton = new JButton();
    sponsorButton.setAlignmentX(0.5F);
    sponsorButton.setAlignmentY(0.5F);
    sponsorButton.setBorderPainted(false);
    sponsorButton.setOpaque(false);
    sponsorButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          Desktop.getDesktop().browse(new URI(sponsorButton.getToolTipText()));
          EventQueue.invokeLater(new Runnable()
          {
            public void run()
            {
              dialog.toFront();
              dialog.requestFocus();
            }
          });
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(InstallerPanel.this, "An error occurred launching the browser", "Error launching browser", 0);
        }
      }
    });
    sponsorPanel.add(sponsorButton);
    
    add(sponsorPanel);
    
    choiceButtonGroup = new ButtonGroup();
    
    JPanel choicePanel = new JPanel();
    choicePanel.setLayout(new BoxLayout(choicePanel, 1));
    boolean first = true;
    SelectButtonAction sba = new SelectButtonAction(null);
    for (InstallerAction action : InstallerAction.values())
    {
      if ((action != InstallerAction.SERVER) || (!VersionInfo.hasNoServerOption()))
      {


        JRadioButton radioButton = new JRadioButton();
        radioButton.setAction(sba);
        radioButton.setText(action.getButtonLabel());
        radioButton.setActionCommand(action.name());
        radioButton.setToolTipText(action.getTooltip());
        radioButton.setSelected(first);
        radioButton.setAlignmentX(0.0F);
        radioButton.setAlignmentY(0.5F);
        choiceButtonGroup.add(radioButton);
        choicePanel.add(radioButton);
        first = false;
      }
    }
    choicePanel.setAlignmentX(1.0F);
    choicePanel.setAlignmentY(0.5F);
    add(choicePanel);
    JPanel entryPanel = new JPanel();
    entryPanel.setLayout(new BoxLayout(entryPanel, 0));
    
    this.targetDir = targetDir;
    selectedDirText = new JTextField();
    selectedDirText.setEditable(false);
    selectedDirText.setToolTipText("Path to minecraft");
    selectedDirText.setColumns(30);
    
    entryPanel.add(selectedDirText);
    JButton dirSelect = new JButton();
    dirSelect.setAction(new FileSelectAction(null));
    dirSelect.setText("...");
    dirSelect.setToolTipText("Select an alternative minecraft directory");
    entryPanel.add(dirSelect);
    
    entryPanel.setAlignmentX(0.0F);
    entryPanel.setAlignmentY(0.0F);
    infoLabel = new JLabel();
    infoLabel.setHorizontalTextPosition(2);
    infoLabel.setVerticalTextPosition(1);
    infoLabel.setAlignmentX(0.0F);
    infoLabel.setAlignmentY(0.0F);
    infoLabel.setForeground(Color.RED);
    infoLabel.setVisible(false);
    
    fileEntryPanel = new JPanel();
    fileEntryPanel.setLayout(new BoxLayout(fileEntryPanel, 1));
    fileEntryPanel.add(infoLabel);
    fileEntryPanel.add(Box.createVerticalGlue());
    fileEntryPanel.add(entryPanel);
    fileEntryPanel.setAlignmentX(0.5F);
    fileEntryPanel.setAlignmentY(0.0F);
    add(fileEntryPanel);
    updateFilePath();
  }
  

  private void updateFilePath()
  {
    try
    {
      targetDir = targetDir.getCanonicalFile();
      selectedDirText.setText(targetDir.getPath());
    }
    catch (IOException e) {}
    



    InstallerAction action = InstallerAction.valueOf(choiceButtonGroup.getSelection().getActionCommand());
    boolean valid = action.isPathValid(targetDir);
    
    String sponsorMessage = action.getSponsorMessage();
    if (sponsorMessage != null)
    {
      sponsorButton.setText(sponsorMessage);
      sponsorButton.setToolTipText(action.getSponsorURL());
      if (action.getSponsorLogo() != null)
      {
        sponsorButton.setIcon(action.getSponsorLogo());
      }
      else
      {
        sponsorButton.setIcon(null);
      }
      sponsorPanel.setVisible(true);
    }
    else
    {
      sponsorPanel.setVisible(false);
    }
    if (valid)
    {
      selectedDirText.setForeground(Color.BLACK);
      infoLabel.setVisible(false);
      fileEntryPanel.setBorder(null);
    }
    else
    {
      selectedDirText.setForeground(Color.RED);
      fileEntryPanel.setBorder(new LineBorder(Color.RED));
      infoLabel.setText("<html>" + action.getFileError(targetDir) + "</html>");
      infoLabel.setVisible(true);
    }
    if (dialog != null)
    {
      dialog.invalidate();
      dialog.pack();
    }
  }
  
  public void run()
  {
    JOptionPane optionPane = new JOptionPane(this, -1, 2);
    
    Frame emptyFrame = new Frame("Mod system installer");
    emptyFrame.setUndecorated(true);
    emptyFrame.setVisible(true);
    emptyFrame.setLocationRelativeTo(null);
    dialog = optionPane.createDialog(emptyFrame, "Mod system installer");
    dialog.setDefaultCloseOperation(2);
    dialog.setVisible(true);
    int result = ((Integer)(optionPane.getValue() != null ? optionPane.getValue() : Integer.valueOf(-1))).intValue();
    if (result == 0)
    {
      InstallerAction action = InstallerAction.valueOf(choiceButtonGroup.getSelection().getActionCommand());
      if (action.run(targetDir))
      {
        JOptionPane.showMessageDialog(null, action.getSuccessMessage(), "Complete", 1);
      }
    }
    dialog.dispose();
    emptyFrame.dispose();
  }
}
