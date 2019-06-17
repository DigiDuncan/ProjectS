package com.dpn;

import com.dpn.projects.ProjectS;
import com.dpn.potions.PotionProjectS;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = ProjectS.MODID)
@ObjectHolder("lilliputian")
public class RegistryManager {

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {

	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {

	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(PotionProjectS.SHRINKING_POTION);
		event.getRegistry().register(PotionProjectS.GROWING_POTION);
	}

	@SubscribeEvent
	public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
		event.getRegistry().register(PotionProjectS.SHRINKING);
		event.getRegistry().register(PotionProjectS.LONG_SHRINKING);
		event.getRegistry().register(PotionProjectS.STRONG_SHRINKING);
		event.getRegistry().register(PotionProjectS.GROWING);
		event.getRegistry().register(PotionProjectS.LONG_GROWING);
		event.getRegistry().register(PotionProjectS.STRONG_GROWING);
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD),
					new ItemStack(Items.MUSHROOM_STEW),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.SHRINKING),
					new ItemStack(Items.REDSTONE),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.LONG_SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.SHRINKING),
					new ItemStack(Items.GLOWSTONE_DUST),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.STRONG_SHRINKING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD),
					new ItemStack(Items.CAKE),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.GROWING),
					new ItemStack(Items.REDSTONE),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.LONG_GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.GROWING),
					new ItemStack(Items.GLOWSTONE_DUST),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.STRONG_GROWING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.AWKWARD),
					new ItemStack(Items.MUSHROOM_STEW),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.SHRINKING),
					new ItemStack(Items.REDSTONE),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.LONG_SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.SHRINKING),
					new ItemStack(Items.GLOWSTONE_DUST), PotionUtils
							.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.STRONG_SHRINKING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.AWKWARD),
					new ItemStack(Items.CAKE),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.GROWING),
					new ItemStack(Items.REDSTONE),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.LONG_GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.GROWING),
					new ItemStack(Items.GLOWSTONE_DUST),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.STRONG_GROWING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.AWKWARD),
					new ItemStack(Items.MUSHROOM_STEW),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.SHRINKING),
					new ItemStack(Items.REDSTONE), PotionUtils
							.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.LONG_SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.SHRINKING),
					new ItemStack(Items.GLOWSTONE_DUST), PotionUtils.addPotionToItemStack(
							new ItemStack(Items.LINGERING_POTION), PotionProjectS.STRONG_SHRINKING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.AWKWARD),
					new ItemStack(Items.CAKE),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.GROWING),
					new ItemStack(Items.REDSTONE), PotionUtils
							.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.LONG_GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.GROWING),
					new ItemStack(Items.GLOWSTONE_DUST), PotionUtils
							.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.STRONG_GROWING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.SHRINKING),
					new ItemStack(Items.GUNPOWDER),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.LONG_SHRINKING),
					new ItemStack(Items.GUNPOWDER),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.LONG_SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.STRONG_SHRINKING),
					new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION),
							PotionProjectS.STRONG_SHRINKING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.GROWING),
					new ItemStack(Items.GUNPOWDER),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.LONG_GROWING),
					new ItemStack(Items.GUNPOWDER),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.LONG_GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionProjectS.STRONG_GROWING),
					new ItemStack(Items.GUNPOWDER),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.STRONG_GROWING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.SHRINKING),
					new ItemStack(Items.DRAGON_BREATH),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.LONG_SHRINKING),
					new ItemStack(Items.DRAGON_BREATH), PotionUtils
							.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.LONG_SHRINKING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION),
							PotionProjectS.STRONG_SHRINKING),
					new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(
							new ItemStack(Items.LINGERING_POTION), PotionProjectS.STRONG_SHRINKING));

			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.GROWING),
					new ItemStack(Items.DRAGON_BREATH),
					PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.LONG_GROWING),
					new ItemStack(Items.DRAGON_BREATH), PotionUtils
							.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.LONG_GROWING));
			BrewingRecipeRegistry.addRecipe(
					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionProjectS.STRONG_GROWING),
					new ItemStack(Items.DRAGON_BREATH), PotionUtils
							.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionProjectS.STRONG_GROWING));
	}

	@SubscribeEvent
	public static void setupModels(ModelRegistryEvent event) {

	}

	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	private static void registerBlockModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation(block.getRegistryName().toString(), "inventory"));
	}

	private static void registerItemModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
	}

}
