package me.koutachan.thatsfun.impl.pathfinder;

import me.koutachan.thatsfun.impl.NPCPlayer;
import me.koutachan.thatsfun.impl.controller.NPCRandomPositionGenerator;
import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import net.minecraft.server.v1_16_R3.RandomPositionGenerator;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class NPCPathfinderGoalRandomStroll extends PathfinderGoal {
    protected final NPCPlayer a;
    protected double b;
    protected double c;
    protected double d;
    protected final double e;
    protected int f;
    protected boolean g;
    private boolean h;

    public NPCPathfinderGoalRandomStroll(NPCPlayer var0, double var1) {
        this(var0, var1, 120);
    }

    public NPCPathfinderGoalRandomStroll(NPCPlayer var0, double var1, int var3) {
        this(var0, var1, var3, true);
    }

    public NPCPathfinderGoalRandomStroll(NPCPlayer var0, double var1, int var3, boolean var4) {
        this.a = var0;
        this.e = var1;
        this.f = var3;
        this.h = var4;
        this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
    }

    public boolean a() {
        if (this.a.isVehicle()) {
            return false;
        } else {
            if (!this.g) {
                if (this.h && this.a.dd() >= 100) {
                    return false;
                }

                if (this.a.getRandom().nextInt(this.f) != 0) {
                    return false;
                }
            }

            Vec3D var0 = this.g();
            if (var0 == null) {
                return false;
            } else {
                this.b = var0.x;
                this.c = var0.y;
                this.d = var0.z;
                this.g = false;
                return true;
            }
        }
    }

    @Nullable
    protected Vec3D g() {
        return NPCRandomPositionGenerator.a(this.a, 10, 7);
    }

    public boolean b() {
        return !this.a.getNavigation().m() && !this.a.isVehicle();
    }

    public void c() {
        this.a.getNavigation().a(this.b, this.c, this.d, this.e);
    }

    public void d() {
        this.a.getNavigation().o();
        super.d();
    }

    public void h() {
        this.g = true;
    }

    public void setTimeBetweenMovement(int var0) {
        this.f = var0;
    }
}
