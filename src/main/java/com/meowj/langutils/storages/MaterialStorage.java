package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Locale;

public class MaterialStorage extends Storage<Material> {

    public MaterialStorage(@NotNull String fallbackLocale) {
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

                Material material = Material.matchMaterial(entryName);
                if (material != null) {
                    addEntry(locale, material, localized, remaper);
                }
            }
        }

        return entries;
    }


    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Material material) {
        String result = super.getEntry(locale, material);
        return result == null ? material.name().toLowerCase(Locale.ROOT) : result;
    }

}