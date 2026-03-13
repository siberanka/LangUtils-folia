package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Locale;

public class PotionEffectStorage extends Storage<PotionEffectType> {

    public PotionEffectStorage(@NotNull String fallbackLocale) {
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

                PotionEffectType effect = PotionEffectType.getByName(entryName.toUpperCase(Locale.ROOT));
                if (effect != null) {
                    addEntry(locale, effect, localized, remaper);
                }
            }
        }

        return entries;
    }

}