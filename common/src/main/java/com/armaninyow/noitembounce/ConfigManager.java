package com.armaninyow.noitembounce;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

public class ConfigManager {

    public static final ConfigClassHandler<ConfigManager> HANDLER = ConfigClassHandler.createBuilder(ConfigManager.class)
        .id(Identifier.fromNamespaceAndPath("noitembounce", "config"))
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("noitembounce.json"))
            .build())
        .build();

    @SerialEntry
    public boolean verticalBounce = false;
}