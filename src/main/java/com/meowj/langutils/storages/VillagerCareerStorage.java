package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Remaper;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Villager Career storage using String keys.
 * Career was removed from the Bukkit API in 1.14+.
 * This storage uses String-based keys for backward compatibility.
 */
public class VillagerCareerStorage extends Storage<String> {

    public VillagerCareerStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {

        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries != null) {
            for (String key : entries.getKeys(false)) {
                String localized = entries.getString(key);
                if (localized != null && !localized.isEmpty()) {
                    addEntry(locale, key.toLowerCase(Locale.ROOT), localized, remaper);
                }
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull String career,
                         @NotNull String localized, Remaper remaper) {
        locale = LangUtils.fixLocale(locale);
        Map<String, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new HashMap<>());
        pairMap.put(career, localized);

        remapping(locale, pairMap, remaper);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull String career) {
        String result = super.getEntry(locale, career);
        return result == null ? career.toLowerCase(Locale.ROOT) : result;
    }

}
