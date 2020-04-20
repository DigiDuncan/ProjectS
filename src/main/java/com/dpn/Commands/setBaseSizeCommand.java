package com.dpn.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.dpn.projects.ProjectS;
import com.dpn.capabilities.ISizeCapability;
import com.dpn.capabilities.SizeProvider;
import com.dpn.network.MessageSizeChange;
import com.dpn.network.PacketHandler;
import com.dpn.util.EntitySizeUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class setBaseSizeCommand extends CommandBase {

    @Override
    public String getName() {
        return "setbasesize";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return ProjectS.MODID + ".commands.setbasesize.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1 || args.length > 2) {
            throw new WrongUsageException(getUsage(sender), new Object[0]);
            //Make sure this is used correctly. If not it will show how
        } else {
            int i = 0;
            Entity entity;
            //Defines Entity

            if (args.length == 1) {
                entity = getCommandSenderAsPlayer(sender);
                //Defines who sent the command and who to target
            } else {
                entity = getEntity(server, sender, args[i]);
                //Defines who sent the command and who to target
                i = 1;
            }

            if (entity.hasCapability(SizeProvider.sizeCapability, null)) {
                //Checks if the entity has the capability of SizeProvider.sizeCapability regardless of facing.
                //Checks if people have no capability?
                ISizeCapability cap = entity.getCapability(SizeProvider.sizeCapability, null);

                if (args.length != i + 1) {
                    throw new WrongUsageException(getUsage(sender), new Object[0]);
                    //Informs the user they fucked up.
                }

                float newBaseSize = (float) parseDouble(args[i], EntitySizeUtil.HARD_MIN, EntitySizeUtil.HARD_MAX);
                if (!entity.world.isRemote) {
                    cap.setBaseSize(newBaseSize);
                    cap.setCameraHeight(newBaseSize);
                    //newBaseSize is the variable that defines the new height
                    PacketHandler.INSTANCE.sendToAll(new MessageSizeChange(cap.getBaseSize(), cap.getScale(), entity.getEntityId()));
                    //SHOULD say "Hey, this moron changed size" Only on servers
                }
            } else {
                // notifyCommandListener(sender, this, Elastic.MODID + ".commands.setbasesize.failure.capability");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length != 1 ? Collections.emptyList() : getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        //Obviously allows the user to tab complete
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

}
