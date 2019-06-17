package cpw.mods.fml.installer;

public abstract interface IMonitor
{
  public abstract void setMaximum(int paramInt);
  
  public abstract void setNote(String paramString);
  
  public abstract void setProgress(int paramInt);
  
  public abstract void close();
}
