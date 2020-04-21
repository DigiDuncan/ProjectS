package com.dpn.Commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class heightmultCommand extends CommandBase {
    @Override
    public String getUsage(ICommandSender var1) {
        return "/" + this.getName() + " <player> [float] // Multiply the user's mult by X.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getName() {
        return "heightmult";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().player.sendChatMessage("Placeholder Text");
    }
}
