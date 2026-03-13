package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Locale;

public class BiomeStorage extends Storage<Biome> {

    public BiomeStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {

        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries != null) {
            for (String entryName : entries.getKeys(false)) {
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    continue;
                }

                try {
                    Biome biome = Biome.valueOf(entryName.toUpperCase(Locale.ROOT));
                    addEntry(locale, biome, localized, remaper);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        return entries;
    }


    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Biome biome) {
        String result = super.getEntry(locale, biome);
        return result == null ? biome.name().toLowerCase(Locale.ROOT) : result;
    }

}