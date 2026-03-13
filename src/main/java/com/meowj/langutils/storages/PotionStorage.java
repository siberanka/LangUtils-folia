package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Locale;

public class PotionStorage extends Storage<PotionType> {

    public PotionStorage(@NotNull String fallbackLocale) {
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
                    PotionType potionType = PotionType.valueOf(entryName.toUpperCase(Locale.ROOT));
                    addEntry(locale, potionType, localized, remaper);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        return entries;
    }


    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull PotionType potionType) {
        String result = super.getEntry(locale, potionType);
        return result == null ? potionType.name().toLowerCase(Locale.ROOT) : result;
    }

}