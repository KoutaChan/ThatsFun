package me.koutachan.thatsfun.impl.pathfinder;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;

import java.util.EnumSet;

public class NPCPathfinderMovePosition extends PathfinderGoal {
    private final NPCPlayer player;
    private final Vec3D goal;
    private final NPCPathfinderGoalSelector goalSelector;
    private Vec3D lastDelta;
    private int streak;

    public NPCPathfinderMovePosition(NPCPlayer living, NPCPathfinderGoalSelector selector, Vec3D goal) {
        this.player = living;
        this.goalSelector = selector;
        this.goal = goal;
        this.a(EnumSet.of(Type.MOVE));
    }

    @Override
    public boolean a() {
        Bukkit.broadcastMessage("dist: " + player.getPositionVector().distanceSquared(this.goal));
        if (player.getPositionVector().distanceSquared(this.goal) <= 3 * 3) {
            this.goalSelector.remove(this);
            return false;
        }
        return true;
    }

    @Override
    public void c() {
        this.player.getNavigation().a(this.player.getNavigation().a(goal.getX(), goal.getY(), goal.getZ(), 1), player.b(GenericAttributes.MOVEMENT_SPEED));
    }

    @Override
    public boolean b() {
        return !this.player.getNavigation().m() && !this.player.isVehicle();
    }

    public NPCPlayer getPlayer() {
        return player;
    }

    public Vec3D getGoal() {
        return goal;
    }
}