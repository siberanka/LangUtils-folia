package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Locale;

public class DyeColorStorage extends Storage<DyeColor> {

    public DyeColorStorage(@NotNull String fallbackLocale) {
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
                    DyeColor color = DyeColor.valueOf(entryName.toUpperCase(Locale.ROOT));
                    addEntry(locale, color, localized, remaper);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        return entries;
    }

}