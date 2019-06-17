package cpw.mods.fml.installer;

import java.io.File;
import java.io.IOException;

public class ExtractAction implements ActionType
{
  public static boolean headless;
  
  public ExtractAction() {}
  
  public boolean run(File target)
  {
    File file = new File(target, VersionInfo.getContainedFile());
    try
    {
      VersionInfo.extractFile(file);
    }
    catch (IOException e)
    {
      if (!headless)
        javax.swing.JOptionPane.showMessageDialog(null, "An error occurred extracting file", "Error", 0);
      return false;
    }
    return true;
  }
  

  public boolean isPathValid(File targetDir)
  {
    return (targetDir.exists()) && (targetDir.isDirectory());
  }
  

  public String getFileError(File targetDir)
  {
    return !targetDir.isDirectory() ? "Target is not a directory" : !targetDir.exists() ? "Target directory does not exist" : "";
  }
  

  public String getSuccessMessage()
  {
    return "Extracted successfully";
  }
  

  public String getSponsorMessage()
  {
    return null;
  }
}
