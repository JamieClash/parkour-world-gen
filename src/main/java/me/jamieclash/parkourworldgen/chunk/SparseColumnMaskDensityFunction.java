package me.jamieclash.parkourworldgen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;

import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.spongepowered.asm.mixin.Unique;

public class SparseColumnMaskDensityFunction implements DensityFunction.Base{
    private final DensityFunction delegate;
    private final int gap;
    private final boolean cellBased;

    public SparseColumnMaskDensityFunction(DensityFunction delegate, int gap, boolean cellBased){
        this.delegate = delegate;
        this.gap = gap;
        this.cellBased = cellBased;
    }

    @Override
    public double sample(NoisePos pos) {
        int x = pos.blockX();
        int z = pos.blockZ();

        if (cellBased){
            int cellX = pos.blockX() >> 2;
            int cellZ = pos.blockZ() >> 2;

            if (shouldNotPlace(cellX, cellZ)){
                return 0.0;
            }
        }else {
            if (shouldNotPlace(x, z)) {
                return -1;
            }
        }
        return delegate.sample(pos);
    }

    @Unique
    private boolean shouldNotPlace(int x, int z){
        boolean isPowerOfTwo = (gap & (gap - 1)) == 0;
        if (isPowerOfTwo) {
            return ((x & (gap - 1)) != 0) || ((z & (gap - 1)) != 0);
        } else {
            return (Math.floorMod(x, gap) != 0) || (Math.floorMod(z, gap) != 0);
        }
    }

    @Override
    public double minValue() {
        return -1.0;
    }

    @Override
    public double maxValue() {
        return 1.0;
    }

    public DensityFunction apply(DensityFunctionVisitor visitor){
        return new SparseColumnMaskDensityFunction(
                delegate.apply(visitor),
                gap,
                cellBased
        );
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return delegate.getCodecHolder();
    }

}
