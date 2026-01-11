package me.jamieclash.parkourworldgen.client.gui;

import me.jamieclash.parkourworldgen.config.ParkourWorldConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ParkourWorldSettingsScreen extends Screen {
    private final Screen parent;

    public ParkourWorldSettingsScreen(Screen parent){
        super(Text.literal("Parkour World Settings"));
        this.parent = parent;
    }

    @Override
    protected void init(){
        int centerX = this.width / 2;
        int y = this.height / 4;
        int currY = y;

        // overworld
        this.addDrawableChild(
            CyclingButtonWidget.onOffBuilder(ParkourWorldConfig.enableOverworld)
                .build(centerX - 100, currY, 200, 20,
                        Text.literal("Enable Overworld Gaps"),
                        (btn, value) -> ParkourWorldConfig.enableOverworld = value
                )
        );

        currY += 24;
        TextFieldWidget overworldGapField = new TextFieldWidget(
                this.textRenderer,
                centerX - 50,
                currY,
                100,
                20,
                Text.literal("Overworld Gap Size")
        );
        overworldGapField.setText(String.valueOf(ParkourWorldConfig.overworldGap - 1));

        overworldGapField.setChangedListener(str -> {
            try {
                int val = Integer.parseInt(str);
                val += 1;
                ParkourWorldConfig.overworldGap = MathHelper.clamp(val, 1, 15);
            } catch (NumberFormatException ignored) {}
        });

        this.addDrawableChild(overworldGapField);

        // nether
        currY += 24;
        this.addDrawableChild(
            CyclingButtonWidget.onOffBuilder(ParkourWorldConfig.enableNether)
                .build(centerX - 100, currY, 200, 20,
                        Text.literal("Enable Nether Gaps"),
                        (btn, value) -> ParkourWorldConfig.enableNether = value
                )
        );

        currY += 24;
        TextFieldWidget netherGapField = new TextFieldWidget(
                this.textRenderer,
                centerX - 50,
                currY,
                100,
                20,
                Text.literal("Nether Gap Size")
        );
        netherGapField.setText(String.valueOf(ParkourWorldConfig.netherGap - 1));

        netherGapField.setChangedListener(str -> {
            try {
                int val = Integer.parseInt(str);
                val += 1;
                ParkourWorldConfig.netherGap = MathHelper.clamp(val, 1, 15);
            } catch (NumberFormatException ignored) {}
        });

        this.addDrawableChild(netherGapField);

        // end
        currY += 24;
        this.addDrawableChild(
            CyclingButtonWidget.onOffBuilder(ParkourWorldConfig.enableEnd)
                .build(centerX - 100, currY, 200, 20,
                        Text.literal("Enable End Gaps"),
                        (btn, value) -> ParkourWorldConfig.enableEnd = value
                )
        );

        currY += 24;
        TextFieldWidget endGapField = new TextFieldWidget(
                this.textRenderer,
                centerX - 50,
                currY,
                100,
                20,
                Text.literal("End Gap Size")
        );
        endGapField.setText(String.valueOf(ParkourWorldConfig.endGap - 1));

        endGapField.setChangedListener(str -> {
            try {
                int val = Integer.parseInt(str);
                val += 1;
                ParkourWorldConfig.endGap = MathHelper.clamp(val, 1, 15);
            } catch (NumberFormatException ignored) {}
        });

        this.addDrawableChild(endGapField);

        // done button
        currY += 28;
        this.addDrawableChild(
            ButtonWidget.builder(Text.literal("Done"),
                    btn -> {
                        assert this.client != null;
                        this.client.setScreen(parent);
                    }
            ).dimensions(centerX - 100, currY, 200, 20).build()
        );
    }

}
