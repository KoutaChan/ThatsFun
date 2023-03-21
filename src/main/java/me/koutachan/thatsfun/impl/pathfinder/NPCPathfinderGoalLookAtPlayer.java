package me.koutachan.thatsfun.impl.pathfinder;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;

import java.util.EnumSet;

public class NPCPathfinderGoalLookAtPlayer extends PathfinderGoal {
    protected final NPCPlayer a;
    protected Entity b;
    protected final float c;
    private int g;
    protected final float d;
    protected final Class<? extends EntityLiving> e;
    protected final PathfinderTargetCondition f;

    public NPCPathfinderGoalLookAtPlayer(NPCPlayer var0, Class<? extends EntityLiving> var1, float var2) {
        this(var0, var1, var2, 0.02F);
    }

    public NPCPathfinderGoalLookAtPlayer(NPCPlayer var0, Class<? extends EntityLiving> var1, float var2, float var3) {
        this.a = var0;
        this.e = var1;
        this.c = var2;
        this.d = var3;
        this.a(EnumSet.of(Type.LOOK));
        if (var1 == EntityHuman.class) {
            this.f = (new PathfinderTargetCondition()).a((double)var2).b().a().d().a((var1x) -> {
                return IEntitySelector.b(var0).test(var1x);
            });
        } else {
            this.f = (new PathfinderTargetCondition()).a((double)var2).b().a().d();
        }

    }

    public boolean a() {
        if (this.a.getRandom().nextFloat() >= this.d) {
            return false;
        } else {
            if (this.a.getGoalTarget() != null) {
                this.b = this.a.getGoalTarget();
            }

            if (this.e == EntityHuman.class) {
                this.b = this.a.world.a(this.f, this.a, this.a.locX(), this.a.getHeadY(), this.a.locZ());
            } else {
                this.b = this.a.world.b(this.e, this.f, this.a, this.a.locX(), this.a.getHeadY(), this.a.locZ(), this.a.getBoundingBox().grow((double)this.c, 3.0D, (double)this.c));
            }

            return this.b != null;
        }
    }

    public boolean b() {
        if (!this.b.isAlive()) {
            return false;
        } else if (this.a.h(this.b) > (double)(this.c * this.c)) {
            return false;
        } else {
            return this.g > 0;
        }
    }

    public void c() {
        this.g = 40 + this.a.getRandom().nextInt(40);
    }

    public void d() {
        this.b = null;
    }

    public void e() {
        this.a.getControllerLook().a(this.b.locX(), this.b.getHeadY(), this.b.locZ());
        --this.g;
    }
}
