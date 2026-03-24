package com.armaninyow.noitembounce;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoItemBounce implements ModInitializer {
	public static final String MOD_ID = "noitembounce";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static ConfigManager.Config config;

	@Override
	public void onInitialize() {
		// Load config on initialization
		config = ConfigManager.loadConfig();
		LOGGER.info("No Item Bounce mod initialized! Vertical bounce removal: {}", config.removeVerticalBounce);
	}

	public static boolean shouldRemoveVerticalBounce() {
		return config.removeVerticalBounce;
	}

	public static void setRemoveVerticalBounce(boolean value) {
		config.removeVerticalBounce = value;
		ConfigManager.saveConfig(config);
		LOGGER.info("Vertical bounce removal set to: {}", value);
	}
}