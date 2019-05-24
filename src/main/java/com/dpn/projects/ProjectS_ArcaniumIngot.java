package com.dpn.projects;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectS_ArcaniumIngot extends ProjectS.ModElement {
    @GameRegistry.ObjectHolder("ProjectS:ArcaniumIngot")
    public static final Item block = null;
    public ProjectS_ArcaniumIngot(ProjectS instance){
        super(instance);
        instance.items.add(() -> new ItemCustom());
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event){
        ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("ProjectS:ArcaniumIngot", "inventory"));
    }
    public static class ItemCustom extends Item{
        public ItemCustom(){
            setMaxDamage(0);
            maxStackSize = 64;
            setUnlocalizedName("ArcaniumIngot");
            setRegistryName("ArcaniumIngot");
            setCreativeTab(CreativeTabs.MATERIALS);
        }
        @Override
        public int getItemEnchantability(){
            return 0;
        }
        @Override
        public int getMaxItemUseDuration(ItemStack par1ItemStack){
            return 0;
        }
        @Override
        public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block){
            return 1F;
        }
        @Override
        public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entity, EnumHand hand){
            ActionResult<ItemStack> ar = super.onItemRightClick(world, entity, hand);
            ItemStack itemstack = ar.getResult();
            int x = (int)entity.posX;
            int y = (int)entity.posY;
            int z = (int)entity.posZ;
            {
                java.util.HashMap<String, Object> $_dependencies = new java.util.HashMap<>();
                ProjectS_ArcaniumIngotRightClickedInAir.executeProcedure($_dependencies);
            }
            return ar;
        }
    }
}
