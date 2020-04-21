package com.dpn;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MyForgeEventHandler {
    @SubscribeEvent
    public void pickupItem(EntityItemPickupEvent event) {
        Minecraft.getMinecraft().player.sendChatMessage("I picked up a " + event.getItem().getName());
    }

    @SubscribeEvent
    public void placedBlock() {

    }
}
