package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Locale;

public class EntityStorage extends Storage<EntityType> {

    public EntityStorage(@NotNull String fallbackLocale) {
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
                EntityType ent = EntityType.valueOf(entryName.toUpperCase(Locale.ROOT));
                addEntry(locale, ent, localized, remaper);
            } catch (IllegalArgumentException ignored) {
            }
        }

        return entries;
    }

}