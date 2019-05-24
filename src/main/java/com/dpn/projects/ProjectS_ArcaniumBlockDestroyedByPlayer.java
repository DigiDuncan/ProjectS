package com.dpn.projects;

import net.minecraft.world.World;
import net.minecraft.util.EnumParticleTypes;

public class ProjectS_ArcaniumBlockDestroyedByPlayer extends ProjectS.ModElement {
    public ProjectS_ArcaniumBlockDestroyedByPlayer(ProjectS instance){
        super(instance);
    }
    public static void executeProcedure(java.util.HashMap<String, Object> dependencies){
        if(dependencies.get("x")==null){
            System.err.println("Failed to load depenedency x for procedure ArcaniumBlockDestroyedByPlayer!");
            return;
        }
        if(dependencies.get("y")==null) {
            System.err.println("Failed to load depenedency y for procedure ArcaniumBlockDestroyedByPlayer!");
            return;
        }
        if(dependencies.get("z")==null) {
            System.err.println("Failed to load depenedency z for procedure ArcaniumBlockDestroyedByPlayer!");
            return;
        }
        if(dependencies.get("world")==null){
            System.err.println("Failed to load dependency world for procedure ArcaniumBlockDestroyedByPlayer!");
            return;
        }
        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        World world = (World) dependencies.get("world");
        world.spawnParticle(EnumParticleTypes.WATER_SPLASH, x, y, z, 0, 1, 0);
    }
}
