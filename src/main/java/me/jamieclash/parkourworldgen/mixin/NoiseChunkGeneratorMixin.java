package me.jamieclash.parkourworldgen.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.jamieclash.parkourworldgen.chunk.SparseColumnMaskDensityFunction;
import me.jamieclash.parkourworldgen.world.ParkourWorldState;
import me.jamieclash.parkourworldgen.world.WorldGenContext;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {
    @Unique
    private static final int OVERWORLD = 0;
    @Unique
    private static final int NETHER = 1;
    @Unique
    private static final int END = 2;

    @Inject(method = "createChunkNoiseSampler", at = @At("HEAD"))
    private void setDim(
            Chunk chunk, StructureAccessor world, Blender blender,
            NoiseConfig noiseConfig, CallbackInfoReturnable<ChunkNoiseSampler> cir
    ) {
        WorldGenContext.set(getDimension((NoiseChunkGenerator)(Object)this));
    }

    @Inject(method = "createChunkNoiseSampler", at = @At("RETURN"))
    private void clearDim(
            Chunk chunk, StructureAccessor world, Blender blender,
            NoiseConfig noiseConfig, CallbackInfoReturnable<ChunkNoiseSampler> cir
    ) {
        WorldGenContext.clear();
    }

    @Unique
    private static int getDimension(NoiseChunkGenerator self){
        BiomeSource source = self.getBiomeSource();
        if(source instanceof TheEndBiomeSource){
            return END;
        }else if (source instanceof MultiNoiseBiomeSource){
            if (!self.getSettings().value().hasAquifers()){
                return NETHER;
            }
            return OVERWORLD;
        }
        return OVERWORLD;
    }
}
