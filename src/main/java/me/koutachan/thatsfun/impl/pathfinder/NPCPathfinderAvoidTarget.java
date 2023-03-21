package me.koutachan.thatsfun.impl.pathfinder;

import net.minecraft.server.v1_16_R3.*;

import java.util.EnumSet;
import java.util.function.Predicate;

public class NPCPathfinderAvoidTarget<T extends EntityLiving> extends PathfinderGoal {
    protected final EntityCreature a;
    private final double i;
    private final double j;
    protected T b;
    protected final float c;
    protected PathEntity d;
    protected final NavigationAbstract e;
    protected final Class<T> f;
    protected final Predicate<EntityLiving> g;
    protected final Predicate<EntityLiving> h;
    private final PathfinderTargetCondition k;

    public NPCPathfinderAvoidTarget(EntityCreature var0, Class<T> var1, float var2, double var3, double var5) {
        this(var0, var1, (var0x) -> true, var2, var3, var5, IEntitySelector.e::test);
    }

    public NPCPathfinderAvoidTarget(EntityCreature var0, Class<T> var1, Predicate<EntityLiving> var2, float var3, double var4, double var6, Predicate<EntityLiving> var8) {
        this.a = var0;
        this.f = var1;
        this.g = var2;
        this.c = var3;
        this.i = var4;
        this.j = var6;
        this.h = var8;
        this.e = var0.getNavigation();
        this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        this.k = (new PathfinderTargetCondition()).a((double)var3).a(var8.and(var2));
    }

    public NPCPathfinderAvoidTarget(EntityCreature var0, Class<T> var1, float var2, double var3, double var5, Predicate<EntityLiving> var7) {
        this(var0, var1, (var0x) -> true, var2, var3, var5, var7);
    }

    public boolean a() {
        this.b = this.a.world.b(this.f, this.k, this.a, this.a.locX(), this.a.locY(), this.a.locZ(), this.a.getBoundingBox().grow((double)this.c, 3.0D, (double)this.c));
        if (this.b == null) {
            return false;
        } else {
            Vec3D var0 = RandomPositionGenerator.c(this.a, 16, 7, this.b.getPositionVector());
            if (var0 == null) {
                return false;
            } else if (this.b.h(var0.x, var0.y, var0.z) < this.b.h(this.a)) {
                return false;
            } else {
                this.d = this.e.a(var0.x, var0.y, var0.z, 0);
                return this.d != null;
            }
        }
    }

    public boolean b() {
        return !this.e.m();
    }

    public void c() {
        this.e.a(this.d, this.i);
    }

    public void d() {
        this.b = null;
    }

    public void e() {
        if (this.a.h(this.b) < 49.0D) {
            this.a.getNavigation().a(this.j);
        } else {
            this.a.getNavigation().a(this.i);
        }

    }
}
