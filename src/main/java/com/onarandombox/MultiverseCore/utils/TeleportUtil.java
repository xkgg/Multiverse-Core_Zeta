package com.onarandombox.MultiverseCore.utils;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TeleportUtil {

    private final MultiverseCore plugin;
    private final boolean isFolia;
    private Method teleportAsync;

    public TeleportUtil(MultiverseCore plugin){
        this.plugin = plugin;
        this.isFolia = plugin.getMorePaperLib().scheduling().isUsingFolia();
        if (isFolia) {
            try {
                teleportAsync = Player.class.getMethod("teleportAsync", Location.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void teleport(Player player, Location location) {
        if (!isFolia) {
            player.teleport(location);
            return;
        }

        plugin.getMorePaperLib().scheduling().entitySpecificScheduler(player).run(() -> {
            try {
                teleportAsync.invoke(player, location);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }, null);
    }
}
