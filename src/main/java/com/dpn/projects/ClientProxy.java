package com.dpn.projects;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{
    @Override
    public void init(FMLInitializationEvent event){

    }
    public void preInit(FMLPreInitializationEvent event){
        OBJLoader.INSTANCE.addDomain("ProjectS");
    }
}
