package uk.antiperson.stackmob.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UtilitiesTest {
    private static Server server;

    @BeforeClass
    public static void installServer() throws Exception {
        server = mock(Server.class);
        when(server.getLogger()).thenReturn(Logger.getLogger("StackMobTest"));
        when(server.getName()).thenReturn("Paper");
        when(server.getVersion()).thenReturn("test");
        when(server.spigot()).thenReturn(mock(Server.Spigot.class));
        Field currentServer = Bukkit.class.getDeclaredField("server");
        currentServer.setAccessible(true);
        currentServer.set(null, server);
    }

    private void release(String minecraftVersion, String bukkitVersion,
                         Utilities.MinecraftVersion expected) throws Exception {
        when(server.getMinecraftVersion()).thenReturn(minecraftVersion);
        when(server.getBukkitVersion()).thenReturn(bukkitVersion);
        Field cached = Utilities.class.getDeclaredField("minecraftVersion");
        cached.setAccessible(true);
        cached.set(null, null);
        assertEquals(expected, Utilities.getMinecraftVersion());
    }

    @Test
    public void recognizesPaper263BuildString() throws Exception {
        release("26.3", "26.3.build.253-stable", Utilities.MinecraftVersion.MC26_3);
    }

    @Test
    public void doesNotRequireHyphenInBukkitVersion() throws Exception {
        release("26.3", "26.3", Utilities.MinecraftVersion.MC26_3);
    }

    @Test
    public void handlesPatchReleases() throws Exception {
        release("26.3.1", "26.3.1.build.1-stable", Utilities.MinecraftVersion.MC26_3);
        release("1.21.11", "1.21.11-R0.1-SNAPSHOT", Utilities.MinecraftVersion.V1_21);
    }

    @Test
    public void doesNotConfuseSimilarReleaseNumbers() throws Exception {
        release("26.30", "26.30-R0.1-SNAPSHOT", Utilities.MinecraftVersion.V1_20_6);
    }
}
