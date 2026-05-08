package com.armaninyow.noitembounce;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoItemBounce implements ModInitializer {
	public static final String MOD_ID = "noitembounce";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static ConfigManager.Config config;

	@Override
	public void onInitialize() {
		config = ConfigManager.loadConfig();
		LOGGER.info("No Item Bounce mod initialized! Vertical bounce removal: {}", config.removeVerticalBounce);

		// Register BEFORE event so the position is marked before drops are scattered
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			StorageBlockTracker.markStorageBlockBroken(pos);
			return true;
		});
	}

	public static boolean isTargetStorageBlock(Block block) {
		if (block instanceof ChestBlock) return true;
		if (block instanceof EnderChestBlock) return true;
		if (block instanceof BarrelBlock) return true;
		if (block instanceof ShulkerBoxBlock) return true;
		if (block instanceof AbstractFurnaceBlock) return true;
		if (block instanceof BrewingStandBlock) return true;
		if (block instanceof HopperBlock) return true;
		if (block instanceof DispenserBlock) return true;
		if (block instanceof DropperBlock) return true;
		if (block instanceof JukeboxBlock) return true;
		if (block instanceof LecternBlock) return true;
		if (block instanceof ChiseledBookshelfBlock) return true;
		if (block instanceof DecoratedPotBlock) return true;
		if (block instanceof FlowerPotBlock) return true;
		if (block instanceof CampfireBlock) return true;
		return false;
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