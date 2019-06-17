package cpw.mods.fml.installer;

import java.io.File;

public abstract interface ActionType
{
  public abstract boolean run(File paramFile);
  
  public abstract boolean isPathValid(File paramFile);
  
  public abstract String getFileError(File paramFile);
  
  public abstract String getSuccessMessage();
  
  public abstract String getSponsorMessage();
}
