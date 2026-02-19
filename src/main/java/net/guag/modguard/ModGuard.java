package net.guag.modguard;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModGuard implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "modguard";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() { LOGGER.info("CLIENT INIT COMPLETE"); }

	@Override
	public void onInitializeClient() {}

}