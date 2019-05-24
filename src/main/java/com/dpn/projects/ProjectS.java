package com.dpn.projects;import jdk.nashorn.internal.ir.Block;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import java.util.function.Supplier;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

@Mod(modid = ProjectS.MODID, name = ProjectS.NAME, version = ProjectS.VERSION, dependencies = "required-after:forge@[14.23.5.2768]")
public class ProjectS implements IFuelHandler, IWorldGenerator {
    public static final String MODID = "projects";
    public static final String NAME = "Project S";
    public static final String VERSION = "0.0.0.1";
    @SidedProxy(clientSide = "com.dpn.projects.ClientProxy", serverSide = "com.dpn.projects.CommonProxy")
    public static CommonProxy proxy;
    @Mod.Instance(MODID)
    public static ProjectS instance;
    public final List<ModElement> elements = new ArrayList<>();
    public final List<Supplier<Block>> blocks = new ArrayList<>();
    public final List<Supplier<Item>> items = new ArrayList<>();
    public final List<Supplier<Biome>> biomes = new ArrayList<>();
    public final List<Supplier<EntityEntry>> entities = new ArrayList<>();
    public ProjectS(){
        FluidRegistry.enableUniversalBucket();
        elements.add(new ProjectS_Arcanium(this));
        elements.add(new ProjectS_ArcaniumBlockDestroyedByPlayer(this));
        elements.add(new ProjectS_ArcaniumIngot(this));
        elements.add(new ProjectS_ArcaniumIngotRightClickedInAir(this));

    }
    @Override
    public int getBurnTime(ItemStack fuel){
        for(ModElement element: elements){
            int ret = element.addFuel(fuel);
            if(ret!=0)
                return ret;
        }
        return 0;
    }
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator cg, IChunkProvider cp){
        elements.forEach(element -> element.generateWorld(random, chunkX*16, chunkZ*16, world, world.provider.getDimension(), cg, cp));
    }
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<net.minecraft.block.Block> event) {
        event.getRegistry().registerAll(blocks.stream().map(Supplier::get).toArray(net.minecraft.block.Block[]::new));
    }
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(items.stream().map(Supplier::get).toArray(Item[]::new));
    }
    @SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event){
        event.getRegistry().registerAll(biomes.stream().map(Supplier::get).toArray(Biome[]::new));
    }
    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event){
        event.getRegistry().registerAll(entities.stream().map(Supplier::get).toArray(EntityEntry[]::new));
    }
    @SubscribeEvent
    public void RegisterSounds(RegistryEvent.Register<net.minecraft.util.SoundEvent> event){

    }
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event){
        elements.forEach(element -> element.registerModels(event));
    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
        GameRegistry.registerFuelHandler(this);
        GameRegistry.registerWorldGenerator(this, 5);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        elements.forEach(element -> element.preInit(event));
        proxy.preInit(event);
    }
    @EventHandler
    public void init(FMLInitializationEvent event){
        elements.forEach(element -> element.init(event));
        proxy.init(event);
    }
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        elements.forEach(element -> element.serverLoad(event));
    }
    public static class GuiHandler implements IGuiHandler{
        @Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){
            return null;
        }
        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){
            return null;
        }
    }
    public static class ModElement{
        public static ProjectS instance;
        public ModElement(ProjectS instance){
            this.instance = instance;
        }
        public void init(FMLInitializationEvent event){

        }
        public void preInit(FMLPreInitializationEvent event){

        }
        public void generateWorld(Random random, int posX, int posZ, World world, int dimID, IChunkGenerator cg, IChunkProvider cp){

        }
        public void serverLoad(FMLServerStartingEvent event){

        }
        public void registerModels(ModelRegistryEvent event){

        }
        public int addFuel(ItemStack fuel){
            return 0;
        }
    }
}