package com.meowj.langutils.nms;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;


public class NMS {

    private NMS() {
    }

    private static String version;
    private static boolean folia;

    // All supported version directories in order
    private static final String[] VERSION_DIRS = {
        "1.13", "1.14", "1.15", "1.16", "1.17", "1.18", "1.19", "1.20", "1.21"
    };

    public static boolean init() {
        try {
            // Parse version from Bukkit.getBukkitVersion() e.g. "1.21-R0.1-SNAPSHOT" -> "1.21"
            String bukkitVer = Bukkit.getBukkitVersion();
            String mcVer = bukkitVer.split("-")[0]; // "1.21.1" or "1.21"

            // Extract major.minor (e.g. "1.21" from "1.21.1")
            String[] parts = mcVer.split("\\.");
            if (parts.length >= 2) {
                version = parts[0] + "." + parts[1];
            } else {
                version = mcVer;
            }

            // Detect Folia
            try {
                Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
                folia = true;
            } catch (ClassNotFoundException e) {
                folia = false;
            }

            Bukkit.getLogger().log(Level.INFO, "[LangUtils] Detected Minecraft version: {0} (Folia: {1})", 
                new Object[]{version, folia});

            return true;

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "[LangUtils] Failed to detect server version.", e);
            return false;
        }
    }

    @Nullable
    public static String getVersion() {
        return version;
    }

    public static boolean isFolia() {
        return folia;
    }

    /**
     * Get all version directories that should be loaded for the current server version.
     * This returns all version dirs up to and including the server's version.
     * For example, on a 1.20 server, this returns: [1.13, 1.14, 1.15, 1.16, 1.17, 1.18, 1.19, 1.20]
     */
    @NotNull
    public static List<String> getVersionDirs() {
        if (version == null) {
            return Collections.singletonList("1.13");
        }

        List<String> result = new ArrayList<>();
        for (String dir : VERSION_DIRS) {
            result.add(dir);
            if (version.equals(dir) || compareVersions(version, dir) < 0) {
                break;
            }
        }

        // If server version is newer than all known dirs, load them all
        if (result.isEmpty()) {
            result.addAll(Arrays.asList(VERSION_DIRS));
        }

        return result;
    }

    /**
     * Compare two version strings like "1.20" and "1.21".
     * Returns negative if v1 < v2, 0 if equal, positive if v1 > v2.
     */
    private static int compareVersions(String v1, String v2) {
        String[] p1 = v1.split("\\.");
        String[] p2 = v2.split("\\.");
        int len = Math.max(p1.length, p2.length);
        for (int i = 0; i < len; i++) {
            int n1 = i < p1.length ? Integer.parseInt(p1[i]) : 0;
            int n2 = i < p2.length ? Integer.parseInt(p2[i]) : 0;
            if (n1 != n2) return n1 - n2;
        }
        return 0;
    }

    /**
     * Get the base color of a shield using pure Bukkit API.
     * Uses BlockStateMeta to access Banner state without NMS.
     */
    @Nullable
    public static DyeColor getShieldBaseColor(@NotNull ItemStack bukkitItem) {
        if (bukkitItem.getType() != Material.SHIELD) {
            return null;
        }

        try {
            if (bukkitItem.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta bsm = (BlockStateMeta) bukkitItem.getItemMeta();
                if (bsm.hasBlockState() && bsm.getBlockState() instanceof Banner) {
                    Banner banner = (Banner) bsm.getBlockState();
                    DyeColor color = banner.getBaseColor();
                    // Only return if it's a non-default color (white is default for plain shields)
                    if (color != null && !banner.getPatterns().isEmpty()) {
                        return color;
                    }
                    // Check if there's actual color data (banner without patterns but with base color)
                    if (color != null && color != DyeColor.WHITE) {
                        return color;
                    }
                }
            }
        } catch (Exception e) {
            // Gracefully handle any version-specific issues
        }

        return null;
    }

    /**
     * Get the patterns of a shield using pure Bukkit API.
     */
    @NotNull
    public static List<Pattern> getShiedPatterns(@NotNull ItemStack bukkitItem) {
        if (bukkitItem.getType() != Material.SHIELD) {
            return Collections.emptyList();
        }

        try {
            if (bukkitItem.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta bsm = (BlockStateMeta) bukkitItem.getItemMeta();
                if (bsm.hasBlockState() && bsm.getBlockState() instanceof Banner) {
                    Banner banner = (Banner) bsm.getBlockState();
                    return banner.getPatterns();
                }
            }
        } catch (Exception e) {
            // Gracefully handle any version-specific issues
        }

        return Collections.emptyList();
    }

}
