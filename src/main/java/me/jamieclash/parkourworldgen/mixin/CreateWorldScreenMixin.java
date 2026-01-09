package me.jamieclash.parkourworldgen.mixin;

import me.jamieclash.parkourworldgen.client.gui.ParkourWorldSettingsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {
    protected CreateWorldScreenMixin(Text title){
        super(title);
    }

    @Inject(method="init", at=@At("TAIL"))
    private void addParkourWorldButton(CallbackInfo ci){
        int x = this.width / 2 - 155;
        int y = this.height / 4 + 120;

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Parkour World Settings"),
                button -> {
                    assert this.client != null;
                    this.client.setScreen(
                            new ParkourWorldSettingsScreen(this)
                    );
                }
            ).dimensions(x, y, 150, 20).build()
        );
    }
}
