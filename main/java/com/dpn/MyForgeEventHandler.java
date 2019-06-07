package com.dpn;

import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.block.BlockCactus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
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
