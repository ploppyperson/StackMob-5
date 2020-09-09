package uk.antiperson.stackmob.utils;

import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static final String PREFIX = ChatColor.DARK_GREEN + "StackMob " + ChatColor.GRAY + ">> " + ChatColor.RESET;
    public static final String SLIME_METADATA = "deathcount";
    public static final String DISCORD = "https://discord.gg/fz9xzuB";
    public static final String GITHUB = "https://github.com/Nathat23/StackMob-5";
    public static final String GITHUB_DEFAULT_CONFIG = GITHUB + "/tree/master/src/main/resources";
    private static final Pattern hexPattern = Pattern.compile("&#([a-zA-Z0-9]){6}");

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
        return Package.getPackage("com.destroystokyo.paper") != null;
    }

    public static boolean isNewBukkit() {
        return Package.getPackage("net.minecraft.server.v1_15_R1") == null;
    }

    public static boolean isNativeVersion() {
        return Package.getPackage("net.minecraft.server.v1_16_R2") != null;
    }

    public enum DownloadResult {
        SUCCESSFUL,
        ERROR
    }
}
