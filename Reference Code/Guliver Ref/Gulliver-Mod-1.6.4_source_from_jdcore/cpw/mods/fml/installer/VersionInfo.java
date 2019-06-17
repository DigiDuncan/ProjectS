package cpw.mods.fml.installer;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class VersionInfo
{
  public static final VersionInfo INSTANCE = new VersionInfo();
  public final JsonRootNode versionData;
  
  public VersionInfo()
  {
    InputStream installProfile = getClass().getResourceAsStream("/install_profile.json");
    JdomParser parser = new JdomParser();
    
    try
    {
      versionData = parser.parse(new InputStreamReader(installProfile, Charsets.UTF_8));
    }
    catch (Exception e)
    {
      throw Throwables.propagate(e);
    }
  }
  
  public static String getProfileName()
  {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "profileName" });
  }
  
  public static String getVersionTarget()
  {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "target" });
  }
  
  public static File getLibraryPath(File root) {
    String path = INSTANCEversionData.getStringValue(new Object[] { "install", "path" });
    String[] split = (String[])Iterables.toArray(Splitter.on(':').omitEmptyStrings().split(path), String.class);
    File dest = root;
    Iterable<String> subSplit = Splitter.on('.').omitEmptyStrings().split(split[0]);
    for (String part : subSplit)
    {
      dest = new File(dest, part);
    }
    dest = new File(new File(dest, split[1]), split[2]);
    String fileName = split[1] + "-" + split[2] + ".jar";
    return new File(dest, fileName);
  }
  
  public static String getVersion()
  {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "version" });
  }
  
  public static String getWelcomeMessage()
  {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "welcome" });
  }
  
  public static String getLogoFileName()
  {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "logo" });
  }
  
  public static boolean getStripMetaInf()
  {
    try
    {
      return INSTANCEversionData.getBooleanValue(new Object[] { "install", "stripMeta" }).booleanValue();
    }
    catch (Exception e) {}
    
    return false;
  }
  

  public static JsonNode getVersionInfo()
  {
    return INSTANCEversionData.getNode(new Object[] { "versionInfo" });
  }
  
  public static File getMinecraftFile(File path)
  {
    return new File(new File(path, getMinecraftVersion()), getMinecraftVersion() + ".jar");
  }
  
  public static String getContainedFile() {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "filePath" });
  }
  
  public static void extractFile(File path) throws IOException {
    INSTANCE.doFileExtract(path);
  }
  
  private void doFileExtract(File path) throws IOException
  {
    InputStream inputStream = getClass().getResourceAsStream("/" + getContainedFile());
    OutputSupplier<FileOutputStream> outputSupplier = Files.newOutputStreamSupplier(path);
    ByteStreams.copy(inputStream, outputSupplier);
  }
  
  public static String getMinecraftVersion()
  {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "minecraft" });
  }
  
  public static String getMirrorListURL()
  {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "mirrorList" });
  }
  
  public static boolean hasMirrors()
  {
    return INSTANCEversionData.isStringValue(new Object[] { "install", "mirrorList" });
  }
  
  public static String getJVMArgs()
  {
    return INSTANCEversionData.getStringValue(new Object[] { "install", "jvmArgs" });
  }
  
  public static boolean hasJVMArgs()
  {
    return INSTANCEversionData.isStringValue(new Object[] { "install", "jvmArgs" });
  }
  
  public static boolean hasNoServerOption()
  {
    return INSTANCEversionData.isStringValue(new Object[] { "install", "noServerOption" });
  }
}
