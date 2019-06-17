package com.dpn;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class MyForgeEventHandler {
    @SubscribeEvent
    public void pickupItem(PlayerEvent.ItemPickupEvent event){
        Minecraft.getMinecraft().player.sendChatMessage("I picked up a "+ event.getStack().getDisplayName());
    }
    @SubscribeEvent
    public void killSomething(PlayerEvent.ItemCraftedEvent event){
        Minecraft.getMinecraft().player.sendChatMessage("I crafted a "+ event.crafting.getDisplayName());
    }
}
