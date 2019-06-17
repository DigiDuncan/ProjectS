package cpw.mods.fml.installer;

import argo.jdom.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import javax.swing.JOptionPane;

public class ServerInstall
  implements ActionType
{
  public static boolean headless;
  private List<String> grabbed;
  
  public ServerInstall() {}
  
  public boolean run(File target)
  {
    if ((target.exists()) && (!target.isDirectory()))
    {
      if (!headless)
        JOptionPane.showMessageDialog(null, "There is a file at this location, the server cannot be installed here!", "Error", 0);
      return false;
    }
    
    File librariesDir = new File(target, "libraries");
    if (!target.exists())
    {
      target.mkdirs();
    }
    librariesDir.mkdir();
    IMonitor monitor = DownloadUtils.buildMonitor();
    if ((headless) && (MirrorData.INSTANCE.hasMirrors()))
    {
      monitor.setNote(getSponsorMessage());
    }
    List<JsonNode> libraries = VersionInfo.getVersionInfo().getArrayNode(new Object[] { "libraries" });
    monitor.setMaximum(libraries.size() + 2);
    int progress = 2;
    grabbed = Lists.newArrayList();
    List<String> bad = Lists.newArrayList();
    String mcServerURL = String.format("https://s3.amazonaws.com/Minecraft.Download/versions/{MCVER}/minecraft_server.{MCVER}.jar".replace("{MCVER}", VersionInfo.getMinecraftVersion()), new Object[0]);
    File mcServerFile = new File(target, "minecraft_server." + VersionInfo.getMinecraftVersion() + ".jar");
    if (!mcServerFile.exists())
    {
      monitor.setNote("Considering minecraft server jar");
      monitor.setProgress(1);
      monitor.setNote(String.format("Downloading minecraft server version %s", new Object[] { VersionInfo.getMinecraftVersion() }));
      DownloadUtils.downloadFile("minecraft server", mcServerFile, mcServerURL, null);
      monitor.setProgress(2);
    }
    progress = DownloadUtils.downloadInstalledLibraries("serverreq", librariesDir, monitor, libraries, progress, grabbed, bad);
    
    monitor.close();
    if (bad.size() > 0)
    {
      String list = Joiner.on(", ").join(bad);
      if (!headless) {
        JOptionPane.showMessageDialog(null, "These libraries failed to download. Try again.\n" + list, "Error downloading", 0);
      } else
        System.err.println("These libraries failed to download, try again. " + list);
      return false;
    }
    try
    {
      File targetRun = new File(target, VersionInfo.getContainedFile());
      VersionInfo.extractFile(targetRun);
    }
    catch (IOException e)
    {
      if (!headless) {
        JOptionPane.showMessageDialog(null, "An error occurred installing the library", "Error", 0);
      } else
        System.err.println("An error occurred installing the distributable");
      return false;
    }
    
    return true;
  }
  

  public boolean isPathValid(File targetDir)
  {
    return (targetDir.exists()) && (targetDir.isDirectory()) && (targetDir.list().length == 0);
  }
  

  public String getFileError(File targetDir)
  {
    if (!targetDir.exists())
    {
      return "The specified directory does not exist<br/>It will be created";
    }
    if (!targetDir.isDirectory())
    {
      return "The specified path needs to be a directory";
    }
    

    return "There are already files at the target directory";
  }
  


  public String getSuccessMessage()
  {
    return String.format("Successfully downloaded minecraft server, downloaded %d libraries and installed %s", new Object[] { Integer.valueOf(grabbed.size()), VersionInfo.getProfileName() });
  }
  

  public String getSponsorMessage()
  {
    return MirrorData.INSTANCE.hasMirrors() ? String.format(headless ? "Data kindly mirrored by %2$s at %1$s" : "<html><a href='%s'>Data kindly mirrored by %s</a></html>", new Object[] { MirrorData.INSTANCE.getSponsorURL(), MirrorData.INSTANCE.getSponsorName() }) : null;
  }
}
