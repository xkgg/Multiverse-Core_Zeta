package com.onarandombox.MultiverseCore.utils;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
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
                teleportAsync = Entity.class.getMethod("teleportAsync", Location.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void teleport(Entity entity, Location location) {
        if (!isFolia) {
            entity.teleport(location);
            return;
        }

        plugin.getMorePaperLib().scheduling().entitySpecificScheduler(entity).run(() -> {
            try {
                teleportAsync.invoke(entity, location);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }, null);
    }
}
