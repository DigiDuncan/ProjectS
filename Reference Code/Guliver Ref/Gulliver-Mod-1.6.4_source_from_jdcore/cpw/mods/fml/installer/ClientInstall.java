package cpw.mods.fml.installer;

import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonField;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;


public class ClientInstall
  implements ActionType
{
  private List<String> grabbed;
  
  public ClientInstall() {}
  
  public boolean run(File target)
  {
    if (!target.exists())
    {
      JOptionPane.showMessageDialog(null, "There is no minecraft installation at this location!", "Error", 0);
      return false;
    }
    File launcherProfiles = new File(target, "launcher_profiles.json");
    if (!launcherProfiles.exists())
    {
      JOptionPane.showMessageDialog(null, "There is no minecraft launcher profile at this location, you need to run the launcher first!", "Error", 0);
      return false;
    }
    
    File versionRootDir = new File(target, "versions");
    File versionTarget = new File(versionRootDir, VersionInfo.getVersionTarget());
    if ((!versionTarget.mkdirs()) && (!versionTarget.isDirectory()))
    {
      if (!versionTarget.delete())
      {
        JOptionPane.showMessageDialog(null, "There was a problem with the launcher version data. You will need to clear " + versionTarget.getAbsolutePath() + " manually", "Error", 0);

      }
      else
      {
        versionTarget.mkdirs();
      }
    }
    
    File versionJsonFile = new File(versionTarget, VersionInfo.getVersionTarget() + ".json");
    File clientJarFile = new File(versionTarget, VersionInfo.getVersionTarget() + ".jar");
    File minecraftJarFile = VersionInfo.getMinecraftFile(versionRootDir);
    try
    {
      if (VersionInfo.getStripMetaInf())
      {
        copyAndStrip(minecraftJarFile, clientJarFile);
      }
      else
      {
        Files.copy(minecraftJarFile, clientJarFile);
      }
    }
    catch (IOException e1)
    {
      JOptionPane.showMessageDialog(null, "You need to run the version " + VersionInfo.getMinecraftVersion() + " manually at least once", "Error", 0);
      return false;
    }
    File librariesDir = new File(target, "libraries");
    File targetLibraryFile = VersionInfo.getLibraryPath(librariesDir);
    IMonitor monitor = DownloadUtils.buildMonitor();
    List<JsonNode> libraries = VersionInfo.getVersionInfo().getArrayNode(new Object[] { "libraries" });
    monitor.setMaximum(libraries.size() + 2);
    int progress = 2;
    grabbed = Lists.newArrayList();
    List<String> bad = Lists.newArrayList();
    progress = DownloadUtils.downloadInstalledLibraries("clientreq", librariesDir, monitor, libraries, progress, grabbed, bad);
    
    monitor.close();
    if (bad.size() > 0)
    {
      String list = Joiner.on(", ").join(bad);
      JOptionPane.showMessageDialog(null, "These libraries failed to download. Try again.\n" + list, "Error downloading", 0);
      return false;
    }
    
    if ((!targetLibraryFile.getParentFile().mkdirs()) && (!targetLibraryFile.getParentFile().isDirectory()))
    {
      if (!targetLibraryFile.getParentFile().delete())
      {
        JOptionPane.showMessageDialog(null, "There was a problem with the launcher version data. You will need to clear " + targetLibraryFile.getAbsolutePath() + " manually", "Error", 0);
        return false;
      }
      

      targetLibraryFile.getParentFile().mkdirs();
    }
    


    JsonRootNode versionJson = JsonNodeFactories.object(VersionInfo.getVersionInfo().getFields());
    
    try
    {
      BufferedWriter newWriter = Files.newWriter(versionJsonFile, Charsets.UTF_8);
      PrettyJsonFormatter.fieldOrderPreservingPrettyJsonFormatter().format(versionJson, newWriter);
      newWriter.close();
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, "There was a problem writing the launcher version data,  is it write protected?", "Error", 0);
      return false;
    }
    
    try
    {
      VersionInfo.extractFile(targetLibraryFile);
    }
    catch (IOException e)
    {
      JOptionPane.showMessageDialog(null, "There was a problem writing the system library file", "Error", 0);
      return false;
    }
    
    JdomParser parser = new JdomParser();
    

    try
    {
      jsonProfileData = parser.parse(Files.newReader(launcherProfiles, Charsets.UTF_8));
    }
    catch (InvalidSyntaxException e)
    {
      JOptionPane.showMessageDialog(null, "The launcher profile file is corrupted. Re-run the minecraft launcher to fix it!", "Error", 0);
      return false;
    }
    catch (Exception e)
    {
      throw Throwables.propagate(e);
    }
    
    JsonField[] fields;
    JsonField[] fields;
    if (VersionInfo.hasJVMArgs())
    {
      fields = new JsonField[] { JsonNodeFactories.field("name", JsonNodeFactories.string(VersionInfo.getProfileName())), JsonNodeFactories.field("lastVersionId", JsonNodeFactories.string(VersionInfo.getVersionTarget())), JsonNodeFactories.field("javaArgs", JsonNodeFactories.string(VersionInfo.getJVMArgs())) };


    }
    else
    {


      fields = new JsonField[] { JsonNodeFactories.field("name", JsonNodeFactories.string(VersionInfo.getProfileName())), JsonNodeFactories.field("lastVersionId", JsonNodeFactories.string(VersionInfo.getVersionTarget())) };
    }
    



    HashMap<JsonStringNode, JsonNode> profileCopy = Maps.newHashMap(jsonProfileData.getNode(new Object[] { "profiles" }).getFields());
    HashMap<JsonStringNode, JsonNode> rootCopy = Maps.newHashMap(jsonProfileData.getFields());
    profileCopy.put(JsonNodeFactories.string(VersionInfo.getProfileName()), JsonNodeFactories.object(fields));
    JsonRootNode profileJsonCopy = JsonNodeFactories.object(profileCopy);
    
    rootCopy.put(JsonNodeFactories.string("profiles"), profileJsonCopy);
    
    JsonRootNode jsonProfileData = JsonNodeFactories.object(rootCopy);
    
    try
    {
      BufferedWriter newWriter = Files.newWriter(launcherProfiles, Charsets.UTF_8);
      PrettyJsonFormatter.fieldOrderPreservingPrettyJsonFormatter().format(jsonProfileData, newWriter);
      newWriter.close();
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, "There was a problem writing the launch profile,  is it write protected?", "Error", 0);
      return false;
    }
    
    return true;
  }
  
  private void copyAndStrip(File sourceJar, File targetJar) throws IOException
  {
    ZipFile in = new ZipFile(sourceJar);
    ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetJar)));
    
    for (ZipEntry e : Collections.list(in.entries()))
    {
      if (e.isDirectory())
      {
        out.putNextEntry(e);
      }
      else if (!e.getName().startsWith("META-INF"))
      {



        ZipEntry n = new ZipEntry(e.getName());
        n.setTime(e.getTime());
        out.putNextEntry(n);
        out.write(readEntry(in, e));
      }
    }
    
    in.close();
    out.close();
  }
  
  private static byte[] readEntry(ZipFile inFile, ZipEntry entry) throws IOException
  {
    return readFully(inFile.getInputStream(entry));
  }
  
  private static byte[] readFully(InputStream stream) throws IOException
  {
    byte[] data = new byte['က'];
    ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
    int len;
    do
    {
      len = stream.read(data);
      if (len > 0)
      {
        entryBuffer.write(data, 0, len);
      }
    } while (len != -1);
    
    return entryBuffer.toByteArray();
  }
  

  public boolean isPathValid(File targetDir)
  {
    if (targetDir.exists())
    {
      File launcherProfiles = new File(targetDir, "launcher_profiles.json");
      return launcherProfiles.exists();
    }
    return false;
  }
  

  public String getFileError(File targetDir)
  {
    if (targetDir.exists())
    {
      return "The directory is missing a launcher profile. Please run the minecraft launcher first";
    }
    

    return "There is no minecraft directory set up. Either choose an alternative, or run the minecraft launcher to create one";
  }
  


  public String getSuccessMessage()
  {
    return String.format("Successfully installed client profile %s for version %s into launcher and grabbed %d required libraries", new Object[] { VersionInfo.getProfileName(), VersionInfo.getVersion(), Integer.valueOf(grabbed.size()) });
  }
  

  public String getSponsorMessage()
  {
    return MirrorData.INSTANCE.hasMirrors() ? String.format("<html><a href='%s'>Data kindly mirrored by %s</a></html>", new Object[] { MirrorData.INSTANCE.getSponsorURL(), MirrorData.INSTANCE.getSponsorName() }) : null;
  }
}
