package com.dpn.projects;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.Logger;

@Mod(modid = ProjectS.MODID, name = ProjectS.NAME, version = ProjectS.VERSION)

public class ProjectS {
        public static final String MODID = "projects";
        public static final String NAME = "Project S";
        public static final String VERSION = "0.0.0.1";

        private static Logger logger;

        @EventHandler
        public void preInit(FMLPreInitializationEvent event) {
                logger = event.getModLog();
        }

        @EventHandler
        public void init(FMLInitializationEvent event) {
                logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        }
        public static Logger getLogger(){
            System.out.println("Eat my ass");
            return logger;
        }
}