package com.dpn.Commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class setCurrentHeightCommand extends CommandBase {
    @Override
    public String getUsage(ICommandSender var1){
        return "/" +this.getName()+" <player> [size] //Adjust the player's multiplier to make the player's current height the input size.";
    }
    @Override
    public int getRequiredPermissionLevel(){
        return 0;
    }
    @Override
    public String getName(){
        return "setcurrentheight";
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)throws CommandException{
        Minecraft.getMinecraft().player.sendChatMessage("Placeholder Text");
    }
}
