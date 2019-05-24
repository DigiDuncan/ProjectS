package com.dpn.projects;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ProjectS_ArcaniumIngotRightClickedInAir extends ProjectS.ModElement {
    public ProjectS_ArcaniumIngotRightClickedInAir(ProjectS instance){
        super(instance);
    }
    public static void executeProcedure(java.util.HashMap<String, Object> dependencies){
        {
            MinecraftServer mcserver = FMLCommonHandler.instance().getMinecraftServerInstance();
            if(mcserver!=null){
                mcserver.getPlayerList().sendMessage(new TextComponentString("I want to fucking die!"));
            }
        }
    }
}
