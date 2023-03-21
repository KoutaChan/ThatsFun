package me.koutachan.thatsfun.impl.pathfinder;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.event.entity.EntityTargetEvent;

import javax.annotation.Nullable;

public abstract class NPCPathfinderGoalTarget extends PathfinderGoal {
    protected final NPCPlayer e;
    protected final boolean f;
    private final boolean a;
    private int b;
    private int c;
    private int d;
    protected EntityLiving g;
    protected int h;

    public NPCPathfinderGoalTarget(NPCPlayer player, boolean flag) {
        this(player, flag, false);
    }

    public NPCPathfinderGoalTarget(NPCPlayer player, boolean flag, boolean flag1) {
        this.h = 60;
        this.e = player;
        this.f = flag;
        this.a = flag1;
    }

    public boolean b() {
        EntityLiving entityliving = this.e.getGoalTarget();
        if (entityliving == null) {
            entityliving = this.g;
        }

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else {
            ScoreboardTeamBase scoreboardteambase = this.e.getScoreboardTeam();
            ScoreboardTeamBase scoreboardteambase1 = entityliving.getScoreboardTeam();
            if (scoreboardteambase != null && scoreboardteambase1 == scoreboardteambase) {
                return false;
            } else {
                double d0 = this.k();
                if (this.e.h(entityliving) > d0 * d0) {
                    return false;
                } else {
                    if (this.f) {
                        if (this.e.getEntitySenses().a(entityliving)) {
                            this.d = 0;
                        } else if (++this.d > this.h) {
                            return false;
                        }
                    }

                    if (entityliving instanceof EntityHuman && ((EntityHuman)entityliving).abilities.isInvulnerable) {
                        return false;
                    } else {
                        this.e.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true);
                        return true;
                    }
                }
            }
        }
    }

    protected double k() {
        return this.e.b(GenericAttributes.FOLLOW_RANGE);
    }

    public void c() {
        this.b = 0;
        this.c = 0;
        this.d = 0;
    }

    public void d() {
        this.e.setGoalTarget((EntityLiving)null, EntityTargetEvent.TargetReason.FORGOT_TARGET, true);
        this.g = null;
    }

    protected boolean a(@Nullable EntityLiving entityliving, PathfinderTargetCondition pathfindertargetcondition) {
        if (entityliving == null) {
            return false;
        } else if (!pathfindertargetcondition.a(this.e, entityliving)) {
            return false;
        } else if (!this.e.a(entityliving.getChunkCoordinates())) {
            return false;
        } else {
            if (this.a) {
                if (--this.c <= 0) {
                    this.b = 0;
                }

                if (this.b == 0) {
                    this.b = this.a(entityliving) ? 1 : 2;
                }

                if (this.b == 2) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean a(EntityLiving entityliving) {
        this.c = 10 + this.e.getRandom().nextInt(5);
        PathEntity pathentity = this.e.getNavigation().a(entityliving, 0);
        if (pathentity == null) {
            return false;
        } else {
            PathPoint pathpoint = pathentity.d();
            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.a - MathHelper.floor(entityliving.locX());
                int j = pathpoint.c - MathHelper.floor(entityliving.locZ());
                return (double)(i * i + j * j) <= 2.25D;
            }
        }
    }

    public NPCPathfinderGoalTarget a(int i) {
        this.h = i;
        return this;
    }
}
