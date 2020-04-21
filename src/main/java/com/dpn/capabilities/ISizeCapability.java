package com.dpn.capabilities;

import net.minecraft.nbt.NBTTagCompound;

public interface ISizeCapability {
	float getBaseSize();
	float getScale();
	void setBaseSize(float baseSize);
	void setScale(float scale);
	void setScaleNoMorph(float scale);
	float getActualSize();
	float getActualScale();
	float getActualScaleNoClamp();
	void setActualScale(float actualScale);
	float getPrevScale();
	
	int getMorphTime();
	int getMaxMorphTime();
	void setMorphing();
	void incrementMorphTime();

	NBTTagCompound saveNBT();
	void loadNBT(NBTTagCompound compound);

	void setCameraHeight(float baseSize);
	float getCameraHeight();
}
