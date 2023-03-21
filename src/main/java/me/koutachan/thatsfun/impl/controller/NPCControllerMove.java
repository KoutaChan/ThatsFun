package me.koutachan.thatsfun.impl.controller;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;

public class NPCControllerMove {
    protected final NPCPlayer a;
    protected double b;
    protected double c;
    protected double d;
    protected double e;
    protected float f;
    protected float g;
    protected Operation h;

    public NPCControllerMove(NPCPlayer player) {
        this.h = Operation.WAIT;
        this.a  = player;
    }

    public boolean b() {
        return this.h == Operation.MOVE_TO;
    }

    public double c() {
        return this.e;
    }

    public void a(double var0, double var2, double var4, double var6) {
        this.b = var0;
        this.c = var2;
        this.d = var4;
        this.e = var6;
        if (this.h != Operation.JUMPING) {
            this.h = Operation.MOVE_TO;
        }

    }

    public void a(float var0, float var1) {
        this.h = Operation.STRAFE;
        this.f = var0;
        this.g = var1;
        this.e = 0.25D;
    }

    public void a() {
        float var8;
        Bukkit.broadcastMessage(this.h.name());
        if (this.h == Operation.STRAFE) {
            float var0 = (float)this.a.b(GenericAttributes.MOVEMENT_SPEED);
            float var1 = (float)this.e * var0;
            float var2 = this.f;
            float var3 = this.g;
            float var4 = MathHelper.c(var2 * var2 + var3 * var3);
            if (var4 < 1.0F) {
                var4 = 1.0F;
            }

            var4 = var1 / var4;
            var2 *= var4;
            var3 *= var4;
            float var5 = MathHelper.sin(this.a.yaw * 0.017453292F);
            float var6 = MathHelper.cos(this.a.yaw * 0.017453292F);
            float var7 = var2 * var6 - var3 * var5;
            var8 = var3 * var6 + var2 * var5;
            if (!this.b(var7, var8)) {
                this.f = 1.0F;
                this.g = 0.0F;
            }

            this.a.q(var1);
            this.a.t(this.f);
            this.a.v(this.g);
            this.h = Operation.WAIT;
        } else if (this.h == Operation.MOVE_TO) {
            this.h = Operation.WAIT;
            double var0 = this.b - this.a.locX();
            double var2 = this.d - this.a.locZ();
            double var4 = this.c - this.a.locY();
            double var6 = var0 * var0 + var4 * var4 + var2 * var2;
            if (var6 < 2.500000277905201E-7D) {
                this.a.t(0.0F);
                return;
            }
            var8 = (float)(MathHelper.d(var2, var0) * 57.2957763671875D) - 90.0F;
            this.a.yaw = this.a(this.a.yaw, var8, 90.0F);
            this.a.q((float)(this.e * this.a.b(GenericAttributes.MOVEMENT_SPEED)));
            BlockPosition var9 = this.a.getChunkCoordinates();
            IBlockData var10 = this.a.world.getType(var9);
            Block var11 = var10.getBlock();
            VoxelShape var12 = var10.getCollisionShape(this.a.world, var9);
            if (var4 > (double)this.a.G && var0 * var0 + var2 * var2 < (double)Math.max(1.0F, this.a.getWidth()) || !var12.isEmpty() && this.a.locY() < var12.c(EnumDirection.EnumAxis.Y) + (double)var9.getY() && !var11.a(TagsBlock.DOORS) && !var11.a(TagsBlock.FENCES)) {
                this.a.getControllerJump().jump();
                this.h = Operation.JUMPING;
            }
        } else if (this.h == Operation.JUMPING) {
            this.a.q((float)(this.e * this.a.b(GenericAttributes.MOVEMENT_SPEED)));
            if (this.a.isOnGround()) {
                this.h = Operation.WAIT;
            }
        } else {
            this.a.t(0.0F);
        }

    }

    private boolean b(float var0, float var1) {
        NPCNavigationAbstract var2 = this.a.getNavigation();
        if (var2 != null) {
            NPCPathfinderAbstract var3 = var2.q();
            return var3 == null || var3.a(this.a.world, MathHelper.floor(this.a.locX() + (double) var0), MathHelper.floor(this.a.locY()), MathHelper.floor(this.a.locZ() + (double) var1)) == PathType.WALKABLE;
        }

        return true;
    }

    protected float a(float var0, float var1, float var2) {
        float var3 = MathHelper.g(var1 - var0);
        if (var3 > var2) {
            var3 = var2;
        }

        if (var3 < -var2) {
            var3 = -var2;
        }

        float var4 = var0 + var3;
        if (var4 < 0.0F) {
            var4 += 360.0F;
        } else if (var4 > 360.0F) {
            var4 -= 360.0F;
        }

        return var4;
    }

    public double d() {
        return this.b;
    }

    public double e() {
        return this.c;
    }

    public double f() {
        return this.d;
    }

    public enum Operation {
        WAIT,
        MOVE_TO,
        STRAFE,
        JUMPING;

        Operation() {
        }
    }
}
