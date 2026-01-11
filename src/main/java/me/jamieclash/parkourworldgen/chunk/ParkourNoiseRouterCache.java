package me.jamieclash.parkourworldgen.chunk;

import me.jamieclash.parkourworldgen.config.FrozenParkourWorldSettings;
import net.minecraft.world.gen.noise.NoiseRouter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class ParkourNoiseRouterCache {
    private static final ConcurrentMap<FrozenParkourWorldSettings, NoiseRouter[]> ROUTERS =
            new ConcurrentHashMap<>();

    private ParkourNoiseRouterCache() {}

    public static NoiseRouter getOrCreate(
            FrozenParkourWorldSettings settings,
            int dim,
            Supplier<NoiseRouter> creator
    ) {
        NoiseRouter[] cache = ROUTERS.computeIfAbsent(settings, s -> new NoiseRouter[3]);
        NoiseRouter router = cache[dim];

        if (router != null) {
            return router;
        }

        synchronized (cache) {
            router = cache[dim];
            if (router == null) {
                router = creator.get();
                cache[dim] = router;
            }
        }

        return router;
    }

    public static void clear() {
        ROUTERS.clear();
    }
}
