package com.armaninyow.noitembounce;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoItemBounce implements ModInitializer {
	public static final String MOD_ID = "noitembounce";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static boolean removeVerticalBounce = true;

	@Override
	public void onInitialize() {
		LOGGER.info("No Item Bounce mod initialized!");
	}

	public static boolean shouldRemoveVerticalBounce() {
		return removeVerticalBounce;
	}

	public static void setRemoveVerticalBounce(boolean value) {
		removeVerticalBounce = value;
		LOGGER.info("Vertical bounce removal set to: {}", value);
	}
}