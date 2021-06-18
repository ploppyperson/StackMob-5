package uk.antiperson.stackmob.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static final String PREFIX = ChatColor.of("#00CED1") + "StackMob " + ChatColor.GRAY + ">> " + ChatColor.RESET;
    public static final String SLIME_METADATA = "deathcount";
    public static final String NO_LEASH_METADATA = "stop-leash";
    public static final String DISCORD = "https://discord.gg/fz9xzuB";
    public static final String GITHUB = "https://github.com/Nathat23/StackMob-5";
    public static final String GITHUB_DEFAULT_CONFIG = GITHUB + "/tree/master/src/main/resources";
    private static final Pattern hexPattern = Pattern.compile("&#([a-zA-Z0-9]){6}");
    private static boolean usingPaper = false;
    static {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            usingPaper = true;
        } catch (ClassNotFoundException ignored) {
        }
    }
    private static final boolean usingNative = ClassLoader.getSystemClassLoader().getDefinedPackage("org.bukkit.craftbukkit.v1_17_R1") != null;
    public static final List<Material> DROWNED_MATERIALS = Arrays.asList(Material.NAUTILUS_SHELL, Material.TRIDENT);
    public static final List<EquipmentSlot> HAND_SLOTS = Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);

    public static String translateColorCodes(String toTranslate) {
        Matcher matcher = hexPattern.matcher(toTranslate);
        while (matcher.find()) {
            net.md_5.bungee.api.ChatColor chatColor = net.md_5.bungee.api.ChatColor.of(matcher.group().substring(1,8));
            String before = toTranslate.substring(0, matcher.start());
            String after = toTranslate.substring(matcher.end());
            toTranslate = before + chatColor + after;
            matcher = hexPattern.matcher(toTranslate);
        }
        return ChatColor.translateAlternateColorCodes('&', toTranslate);
    }

    public static List<Integer> split(int dividend, int divisor) {
        int fullAmount = dividend / divisor;
        int remainder = dividend % divisor;
        List<Integer> numbers = new ArrayList<>(fullAmount + 1);
        for (int i = 0; i < fullAmount; i++) {
            numbers.add(divisor);
        }
        if (remainder > 0) {
            numbers.add(remainder);
        }
        return numbers;
    }

    public static CompletableFuture<DownloadResult> downloadFile(File filePath, String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL webPath = new URL(url);
                try (InputStream in = webPath.openStream()) {
                    Files.createDirectories(filePath.getParentFile().toPath());
                    Files.copy(in, filePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                return DownloadResult.SUCCESSFUL;
            } catch (IOException e) {
                e.printStackTrace();
                return DownloadResult.ERROR;
            }
        });
    }

    public static boolean isPaper() {
        return usingPaper;
    }

    public static boolean isNativeVersion() {
        return usingNative;
    }

    public static boolean isDye(ItemStack material) {
        return material.getType().toString().endsWith("_DYE");
    }

    public static void removeHandItem(Player player, int itemAmount) {
        if (itemAmount == player.getInventory().getItemInMainHand().getAmount()) {
           player.getInventory().setItemInMainHand(null);
           return;
        }
        ItemStack is = player.getInventory().getItemInMainHand();
        is.setAmount(is.getAmount() - itemAmount);
        player.getInventory().setItemInMainHand(is);
    }

    public enum DownloadResult {
        SUCCESSFUL,
        ERROR
    }
}
