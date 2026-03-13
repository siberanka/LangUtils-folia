package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class EnchantStorage extends Storage<Enchantment> {

    public EnchantStorage(@NotNull String fallbackLocale) {
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
                    // Try to get by key first (1.13+)
                    NamespacedKey key = NamespacedKey.minecraft(entryName);
                    Enchantment enchant = Enchantment.getByKey(key);
                    if (enchant == null) {
                        // Fallback to name for older versions/compatibility
                        enchant = Enchantment.getByName(entryName.toUpperCase(Locale.ROOT));
                    }

                    if (enchant != null) {
                        addEntry(locale, enchant, localized, remaper);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        return entries;
    }

}