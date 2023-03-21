package me.koutachan.thatsfun;

import net.minecraft.server.v1_16_R3.BlockPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public interface NPC<T> {
    public String getName();
    public void setName(String name);

    public boolean isVisibleNameTag();
    public void setNameTagVisibility(boolean show);

    public void moveTo(int priority, Location location);
    public void setTarget(Entity entity, boolean attack);
    public void sleep(BlockPosition position, boolean force);

    public boolean isSpawned();

    public void addPathfinding(int priority, CustomPathfinder pathfinder);
    public boolean isAware();
    public void setAware(boolean aware);

    public T getEntity();
}