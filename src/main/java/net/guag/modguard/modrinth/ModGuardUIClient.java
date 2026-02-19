package net.guag.modguard.modrinth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.guag.modguard.modrinth.InstallerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModGuardUIClient implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "modguard";
    private static KeyBinding openUIBinding;

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {}

    @Override
    public void onInitializeClient() {
        openUIBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ModGuard.open_ui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KeyBinding.Category.create(Identifier.of("ModGuard:keybinds"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openUIBinding.wasPressed()) {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.currentScreen == null) {
                    // Pass the client and the hardcoded mod toggles (loaded from file folders)
                    mc.setScreen( new InstallerScreen(Text.literal("ModGuard Installer")) );
                }
            }
        });

        LOGGER.info("KEYBIND INIT COMPLETE");
    }

//	@Override
//	public void render(DrawContext context){
//		context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("modguard", "textures/gui/modrinthfavicon.png"), this.width / 2 - 100 + 205, y, 0, 0, 20, 20, 20, 20);
//	}
}