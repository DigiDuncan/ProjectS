package com.dpn.projects;

import com.dpn.projects.ProjectS;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block;

import java.lang.ref.Reference;
import java.util.Random;
import java.util.HashMap;

public class ProjectS_Arcanium extends ProjectS.ModElement {

    @GameRegistry.ObjectHolder("projects:arcanium")
    public static Block block = null;

    public ProjectS_Arcanium(ProjectS instance) {
        super(instance);
        instance.blocks.add(new BlockCustom("Arcanium", Material.SPONGE));
        instance.items.add(() -> new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation("projects:arcanium", "inventory"));
    }

    @Override
    public void generateWorld(Random random, int chunkX, int chunkZ, World world, int dimID, IChunkGenerator cg, IChunkProvider cp) {
        boolean dimensionCriteria = false;
        if (dimID == 0)
            dimensionCriteria = true;
        if (!dimensionCriteria)
            return;
        for (int i = 0; i < 10; i++) {
            int x = chunkX + random.nextInt(16);
            int y = random.nextInt(63) + 1;
            int z = chunkZ + random.nextInt(16);
            (new WorldGenMinable(block.getDefaultState(), 16, new com.google.common.base.Predicate<IBlockState>() {

                public boolean apply(IBlockState blockAt) {
                    boolean blockCriteria = false;
                    IBlockState require;
                    if (blockAt.getBlock() == Blocks.STONE.getDefaultState().getBlock())
                        blockCriteria = true;
                    return blockCriteria;
                }
            })).generate(world, random, new BlockPos(x, y, z));
        }
    }

    public static class BlockCustom extends Block {

        public BlockCustom(String name, Material material) {
            super(material);
            setRegistryName(name);
            setUnlocalizedName(name);
        }
        @SideOnly(Side.CLIENT)
        @Override
        public BlockRenderLayer getBlockLayer() {
            return BlockRenderLayer.SOLID;
        }

        @Override
        public Item getItemDropped(IBlockState state, Random random, int l) {
            return new ItemStack(ProjectS_ArcaniumIngot.block, (int) (1)).getItem();
        }

        @Override
        public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer entity, boolean willHarvest) {
            boolean retval = super.removedByPlayer(state, world, pos, entity, willHarvest);
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            Block block = this;
            {
                java.util.HashMap<String, Object> $_dependencies = new java.util.HashMap<>();
                $_dependencies.put("x", x);
                $_dependencies.put("y", y);
                $_dependencies.put("z", z);
                $_dependencies.put("world", world);
                ProjectS_ArcaniumBlockDestroyedByPlayer.executeProcedure($_dependencies);
            }
            return retval;
        }
    }
}
