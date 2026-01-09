package me.jamieclash.parkourworldgen.mixin;

import me.jamieclash.parkourworldgen.config.ParkourWorldConfig;
import me.jamieclash.parkourworldgen.world.ParkourWorldState;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenCreateMixin {

    @Inject(method = "createLevel", at = @At("HEAD")
    )
    private void onCreateLevel(CallbackInfo ci) {
        ParkourWorldState.set(ParkourWorldConfig.overworld,
                ParkourWorldConfig.nether,
                ParkourWorldConfig.end,
                ParkourWorldConfig.overworldGap,
                ParkourWorldConfig.netherGap,
                ParkourWorldConfig.endGap
        );
    }
}