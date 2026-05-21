package com.armaninyow.noitembounce;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoItemBounce implements ModInitializer {
	public static final String MOD_ID = "noitembounce";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ConfigManager.HANDLER.load();
		LOGGER.info("No Item Bounce mod initialized! Vertical bounce: {}", ConfigManager.HANDLER.instance().verticalBounce);

		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			BlockDropTracker.markBlockBroken(pos);
			return true;
		});
	}

	public static boolean shouldRemoveVerticalBounce() {
		return !ConfigManager.HANDLER.instance().verticalBounce;
	}

	public static void setRemoveVerticalBounce(boolean value) {
		ConfigManager.HANDLER.instance().verticalBounce = !value;
		ConfigManager.HANDLER.save();
		LOGGER.info("Vertical bounce set to: {}", ConfigManager.HANDLER.instance().verticalBounce);
	}
}