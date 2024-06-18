package com.onarandombox.MultiverseCore.api;

import com.onarandombox.MultiverseCore.api.MVDestination;
import com.onarandombox.MultiverseCore.enums.TeleportResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface Teleporter {
    CompletableFuture<TeleportResult> teleport(CommandSender teleporter, Player teleportee, MVDestination destination);
}
