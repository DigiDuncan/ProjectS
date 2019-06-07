package com.dpn.Commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MyCommandHandler extends CommandBase {
    @Override
    public String getUsage(ICommandSender var1){
        return "/" + this.getName() + " Tells the player a beautiful message!";
    }
    @Override
    public int getRequiredPermissionLevel(){
        return 0;
    }
    @Override
    public String getName(){
        return "test";
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        Minecraft.getMinecraft().player.sendChatMessage("Go fuck yourself");
    }
}