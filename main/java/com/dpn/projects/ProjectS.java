package com.dpn.projects;

import com.dpn.Commands.*;
import com.dpn.Config;
import com.dpn.MyForgeEventHandler;
import com.dpn.capabilities.DefaultSizeCapability;
import com.dpn.capabilities.ISizeCapability;
import com.dpn.capabilities.SizeCapabilityStorage;
import com.dpn.proxy.CommonProxy;
import com.dpn.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.config.Configuration;
import java.io.File;

@Mod(modid = ProjectS.MODID, name = ProjectS.NAME, version = ProjectS.VERSION)
public class ProjectS{
    public static final String MODID = "projects";
    public static final String NAME = "Project S";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @SidedProxy(clientSide = "com.dpn.proxy.ClientProxy", serverSide = "com.dpn.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Configuration config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        CapabilityManager.INSTANCE.register(ISizeCapability.class, new SizeCapabilityStorage(), DefaultSizeCapability.class);
        PacketHandler.registerMessages();

        File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "projects.cfg"));
        Config.readConfig();

        proxy.preInit(event);
    }
    @EventHandler
    public static void init(FMLInitializationEvent event){
        logger.info("initialize FMLServerStartingEvent :" +NAME);
        MinecraftForge.EVENT_BUS.register(new MyForgeEventHandler());
    }
    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event){
        event.registerServerCommand(new heightaddCommand());
        event.registerServerCommand(new heightmultCommand());
        event.registerServerCommand(new setBaseSizeCommand());
        event.registerServerCommand(new setCurrentHeightCommand());
        event.registerServerCommand(new setmultCommand());
    }
}