package com.dpn.projects;

import com.dpn.Commands.MyCommandHandler;
import com.dpn.MyForgeEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ProjectS.MODID, name = ProjectS.NAME, version = ProjectS.VERSION)
public class ProjectS{
    public static final String MODID = "projects";
    public static final String NAME = "Project S";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
    }
    @EventHandler
    public static void init(FMLInitializationEvent event){
        logger.info("initialize FMLServerStartingEvent :" +NAME);
        MinecraftForge.EVENT_BUS.register(new MyForgeEventHandler());
    }
    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event){
        event.registerServerCommand(new MyCommandHandler());
    }
}