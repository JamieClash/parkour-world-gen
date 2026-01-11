package me.jamieclash.parkourworldgen;

import me.jamieclash.parkourworldgen.chunk.ParkourNoiseRouterCache;
import me.jamieclash.parkourworldgen.config.ParkourWorldConfig;
import me.jamieclash.parkourworldgen.mixin.ChunkNoiseSamplerMixin;
import me.jamieclash.parkourworldgen.world.ParkourWorldSavedData;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParkourWorldGen implements ModInitializer {
	public static final String MOD_ID = "parkourworldgen";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerWorldEvents.LOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD){
				ParkourWorldSavedData data = ParkourWorldSavedData.get(server);

				if (ParkourWorldConfig.getFrozen() == null){
					data.applyToConfig();
				}

				ParkourWorldConfig.setSavedData(world);
			}
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			ParkourWorldConfig.clearFrozen();
			ParkourNoiseRouterCache.clear();
		});
	}
}