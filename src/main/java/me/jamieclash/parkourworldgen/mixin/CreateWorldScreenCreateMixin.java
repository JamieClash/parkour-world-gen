package me.jamieclash.parkourworldgen.mixin;

import me.jamieclash.parkourworldgen.config.FrozenParkourWorldSettings;
import me.jamieclash.parkourworldgen.config.ParkourWorldConfig;
import me.jamieclash.parkourworldgen.world.ParkourWorldSavedData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenCreateMixin {

    @Inject(method = "createLevel", at = @At("HEAD"))
    private void beforeCreateLevel(CallbackInfo ci) {
        ParkourWorldConfig.freeze();
    }

    @Inject(method = "createLevel", at = @At("TAIL")
    )
    private void afterCreateLevel(CallbackInfo ci) {
        MinecraftServer server = MinecraftClient.getInstance().getServer();
        if (server==null){
            return;
        }

        ParkourWorldSavedData data = ParkourWorldSavedData.get(server);
        FrozenParkourWorldSettings settings = ParkourWorldConfig.getFrozen();

        data.enableOverworld = settings.enableOverworld();
        data.enableNether = settings.enableNether();
        data.enableEnd = settings.enableEnd();
        data.overworldGap = settings.overworldGap();
        data.netherGap = settings.netherGap();
        data.endGap = settings.endGap();

        data.markDirty();
    }
}