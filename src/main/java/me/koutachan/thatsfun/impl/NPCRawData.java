package me.koutachan.thatsfun.impl;

import me.koutachan.thatsfun.Animation;
import me.koutachan.thatsfun.CustomPathfinder;
import me.koutachan.thatsfun.NPC;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NPCRawData implements NPC<Player> {
    private final NPCPlayer player;

    public NPCRawData(NPCPlayer player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public boolean isVisibleNameTag() {
        return false;
    }

    @Override
    public void setNameTagVisibility(boolean show) {

    }

    @Override
    public void moveTo(int priority, Location location) {
        player.moveTo(priority, new Vec3D(location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public void setTarget(Entity entity, boolean attack) {

    }

    @Override
    public void sleep(BlockPosition position, boolean force) {
        if (force) player.forceSleep(position);
        else player.sleep(position);
    }

    @Override
    public boolean isAware() {
        return player.isAware();
    }

    @Override
    public void setAware(boolean aware) {
        player.setAware(aware);
    }

    @Override
    public boolean isSpawned() {
        return player.isAlive();
    }

    @Override
    public void addPathfinding(int priority, CustomPathfinder pathfinder) {
        switch (pathfinder) {

        }
    }

    @Override
    public Player getEntity() {
        return player.getBukkitEntity();
    }
}
