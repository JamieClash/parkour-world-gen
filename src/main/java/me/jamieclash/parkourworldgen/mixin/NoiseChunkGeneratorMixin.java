package me.jamieclash.parkourworldgen.mixin;

import me.jamieclash.parkourworldgen.config.FrozenParkourWorldSettings;
import me.jamieclash.parkourworldgen.config.ParkourWorldConfig;
import me.jamieclash.parkourworldgen.world.WorldGenContext;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {
    @Unique
    private static final int OVERWORLD = 0;
    @Unique
    private static final int NETHER = 1;
    @Unique
    private static final int END = 2;

    @Inject(method = "createChunkNoiseSampler", at = @At("HEAD"))
    private void setContext(
            Chunk chunk, StructureAccessor structureAccessor, Blender blender,
            NoiseConfig noiseConfig, CallbackInfoReturnable<ChunkNoiseSampler> cir
    ) {
        int dim = getDimension((NoiseChunkGenerator)(Object)this);
        WorldGenContext.set(dim);
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

        FrozenParkourWorldSettings settings = ParkourWorldConfig.getFrozen();
        if(settings == null){
            return -1;
        }

        if(source instanceof TheEndBiomeSource && settings.enableEnd()){
            return END;
        }else if (source instanceof MultiNoiseBiomeSource){
            if (!self.getSettings().value().hasAquifers() && settings.enableNether()){
                return NETHER;
            }
            else if(settings.enableOverworld()) {
                return OVERWORLD;
            }
        }
        return -1;
    }
}
