package com.onarandombox.MultiverseCore.utils;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

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


    public CompletableFuture<Boolean> teleport(Entity entity, Location location) {

        if (!isFolia) {
            CompletableFuture<Boolean> result = new CompletableFuture<>();
            result.complete(entity.teleport(location));
            return result;
        }

        try {
            @SuppressWarnings("unchecked")
            CompletableFuture<Boolean> result = (CompletableFuture<Boolean>) teleportAsync.invoke(entity, location);
            return result;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
