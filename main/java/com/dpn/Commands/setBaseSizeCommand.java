package com.dpn.Commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class setBaseSizeCommand extends CommandBase {
    @Override
    public String getUsage(ICommandSender var1){
        return "/" + this.getName() + " <player> [size] //Set's a users basesize (what other math is based on)";
    }
    @Override
    public int getRequiredPermissionLevel(){
        return 0;
    }
    @Override
    public String getName(){
        return "basesize";
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        Minecraft.getMinecraft().player.sendChatMessage("Placeholder Text");
    }
}