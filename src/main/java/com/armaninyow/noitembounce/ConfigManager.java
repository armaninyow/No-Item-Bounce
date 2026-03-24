package com.armaninyow.noitembounce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("noitembounce.json");

    public static class Config {
        public boolean removeVerticalBounce = true;
    }

    public static Config loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                return GSON.fromJson(json, Config.class);
            } catch (IOException e) {
                NoItemBounce.LOGGER.error("Failed to load config, using defaults", e);
            }
        }
        return new Config();
    }

    public static void saveConfig(Config config) {
        try {
            String json = GSON.toJson(config);
            Files.writeString(CONFIG_PATH, json);
            NoItemBounce.LOGGER.info("Config saved successfully");
        } catch (IOException e) {
            NoItemBounce.LOGGER.error("Failed to save config", e);
        }
    }
}