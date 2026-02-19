package net.guag.modguard.mixin;

import net.guag.modguard.modrinth.InstallerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class OpenGUIMixin extends Screen {
    protected OpenGUIMixin(Text title) {
        super(title);
    }

    @Inject(method = "addNormalWidgets", at = @At("RETURN"))
    private void addOpenGUIButton(int y, int spacingY, CallbackInfoReturnable<Void> cir){
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("key.ModGuard.opengui"), button -> MinecraftClient.getInstance().setScreen(new InstallerScreen(Text.literal("ModGuard Installer"))))
                        .dimensions(this.width / 2 - 100 + 205, y, 20, 20)
                        .build()
        );
    }
}
