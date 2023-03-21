package me.koutachan.thatsfun.impl.pathfinder;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import org.bukkit.Bukkit;

import java.util.EnumSet;

public class NPCPathfindingRandomLookaround extends PathfinderGoal {
    private final NPCPlayer a;
    private double b;
    private double c;
    private int d;

    public NPCPathfindingRandomLookaround(NPCPlayer var0) {
        this.a = var0;
        this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
    }

    public boolean a() {
        return this.a.getRandom().nextFloat() < 0.02F;
    }

    public boolean b() {
        return this.d >= 0;
    }

    public void c() {
        double var0 = 6.283185307179586D * this.a.getRandom().nextDouble();
        this.b = Math.cos(var0);
        this.c = Math.sin(var0);
        this.d = 20 + this.a.getRandom().nextInt(20);
    }

    public void e() {
        --this.d;
        this.a.getControllerLook().a(this.a.locX() + this.b, this.a.getHeadY(), this.a.locZ() + this.c);
    }
}
