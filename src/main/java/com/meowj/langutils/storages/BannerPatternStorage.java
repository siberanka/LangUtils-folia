package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BannerPatternStorage extends Storage<Integer> {

    public BannerPatternStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {

        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries == null) {
            return null;
        }

        for (String entryName : entries.getKeys(false)) {
            String localized = entries.getString(entryName);

            if (localized == null || localized.isEmpty()) {
                continue;
            }

            try {
                Integer mixedCode = Integer.parseInt(entryName);
                addEntry(locale, mixedCode, localized, remaper);
            } catch (NumberFormatException ignored) {
            }
        }

        return entries;
    }

}
