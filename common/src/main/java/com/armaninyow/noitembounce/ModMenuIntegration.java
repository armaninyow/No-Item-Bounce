package com.armaninyow.noitembounce;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> YetAnotherConfigLib.createBuilder()
			.title(Component.translatable("noitembounce.config.title"))
			.category(ConfigCategory.createBuilder()
				.name(Component.translatable("noitembounce.config.category.general"))
				.option(Option.<Boolean>createBuilder()
					.name(Component.translatable("noitembounce.config.option.vertical_bounce"))
					.description(OptionDescription.createBuilder()
						.text(Component.translatable("noitembounce.config.option.vertical_bounce.tooltip"))
						.build())
					.binding(
						false,
						() -> ConfigManager.HANDLER.instance().verticalBounce,
						value -> {
							ConfigManager.HANDLER.instance().verticalBounce = value;
							ConfigManager.HANDLER.save();
						}
					)
					.controller(TickBoxControllerBuilder::create)
					.build())
				.build())
			.save(ConfigManager.HANDLER::save)
			.build()
			.generateScreen(parent);
	}
}