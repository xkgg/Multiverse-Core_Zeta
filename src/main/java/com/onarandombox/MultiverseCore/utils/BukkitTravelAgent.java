package com.onarandombox.MultiverseCore.utils;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import com.dumptruckman.minecraft.util.Logging;
import com.onarandombox.MultiverseCore.api.SafeTTeleporter;
import com.onarandombox.MultiverseCore.destination.CannonDestination;
import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.event.player.PlayerPortalEvent;

public class BukkitTravelAgent implements TravelAgent {
    private final MVTravelAgent agent;

    public BukkitTravelAgent(MVTravelAgent agent) {
        this.agent = agent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BukkitTravelAgent setSearchRadius(int radius) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSearchRadius() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BukkitTravelAgent setCreationRadius(int radius) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCreationRadius() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getCanCreatePortal() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCanCreatePortal(boolean create) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location findOrCreate(Location location) {
        return location;
        // return this.getSafeLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location findPortal(Location location) {
        return location;
        // return this.getSafeLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createPortal(Location location) {
        return false;
    }

    private CompletableFuture<Location> getSafeLocation() {

        CompletableFuture<Location> futureLocation = new CompletableFuture<>();

        // At this time, these can never use the velocity.
        if (agent.destination instanceof CannonDestination) {
            Logging.fine("Using Stock TP method. This cannon will have 0 velocity");
        }
        SafeTTeleporter teleporter = agent.core.getSafeTTeleporter();
        Location newLoc = agent.destination.getLocation(agent.player);

        if (!agent.destination.useSafeTeleporter()) {
            futureLocation.complete(newLoc);
            return futureLocation;
        }

        teleporter.getSafeLocation(agent.player, agent.destination).whenComplete((location, throwable) -> {
            if (location == null) {
                futureLocation.complete(agent.player.getLocation());
                return;
            }

            futureLocation.complete(location);
        });

        return futureLocation;

    }

    // public void setPortalEventTravelAgent(PlayerPortalEvent event) {
    //     event.setPortalTravelAgent(this);
    //     event.useTravelAgent(true);
    // }
}
