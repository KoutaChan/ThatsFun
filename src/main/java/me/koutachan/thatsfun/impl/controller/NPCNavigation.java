package me.koutachan.thatsfun.impl.controller;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;

import java.util.Iterator;

public class NPCNavigation extends NPCNavigationAbstract {
    private boolean p;

    public NPCNavigation(NPCPlayer var0, World var1) {
        super(var0, var1);
    }

    protected NPCPathfinder a(int var0) {
        this.o = new NPCPathfinderNormal();
        this.o.a(true);
        return new NPCPathfinder(this.o, var0);
    }

    protected boolean a() {
        return this.a.isOnGround() || this.p() || this.a.isPassenger();
    }

    protected Vec3D b() {
        return new Vec3D(this.a.locX(), (double)this.u(), this.a.locZ());
    }

    public PathEntity a(BlockPosition var0, int var1) {
        BlockPosition var2;
        if (this.b.getType(var0).isAir()) {
            for(var2 = var0.down(); var2.getY() > 0 && this.b.getType(var2).isAir(); var2 = var2.down()) {
            }

            if (var2.getY() > 0) {
                return super.a(var2.up(), var1);
            }

            while(var2.getY() < this.b.getBuildHeight() && this.b.getType(var2).isAir()) {
                var2 = var2.up();
            }

            var0 = var2;
        }

        if (!this.b.getType(var0).getMaterial().isBuildable()) {
            return super.a(var0, var1);
        } else {
            for(var2 = var0.up(); var2.getY() < this.b.getBuildHeight() && this.b.getType(var2).getMaterial().isBuildable(); var2 = var2.up()) {
            }

            return super.a(var2, var1);
        }
    }

    public PathEntity a(Entity var0, int var1) {
        return this.a(var0.getChunkCoordinates(), var1);
    }

    private int u() {
        if (this.a.isInWater() && this.r()) {
            int var0 = MathHelper.floor(this.a.locY());
            Block var1 = this.b.getType(new BlockPosition(this.a.locX(), (double)var0, this.a.locZ())).getBlock();
            int var2 = 0;

            do {
                if (var1 != Blocks.WATER) {
                    return var0;
                }

                ++var0;
                var1 = this.b.getType(new BlockPosition(this.a.locX(), (double)var0, this.a.locZ())).getBlock();
                ++var2;
            } while(var2 <= 16);

            return MathHelper.floor(this.a.locY());
        } else {
            return MathHelper.floor(this.a.locY() + 0.5D);
        }
    }

    protected void D_() {
        super.D_();
        if (this.p) {
            if (this.b.e(new BlockPosition(this.a.locX(), this.a.locY() + 0.5D, this.a.locZ()))) {
                return;
            }

            for(int var0 = 0; var0 < this.c.e(); ++var0) {
                PathPoint var1 = this.c.a(var0);
                if (this.b.e(new BlockPosition(var1.a, var1.b, var1.c))) {
                    this.c.b(var0);
                    return;
                }
            }
        }

    }

    protected boolean a(Vec3D var0, Vec3D var1, int var2, int var3, int var4) {
        int var5 = MathHelper.floor(var0.x);
        int var6 = MathHelper.floor(var0.z);
        double var7 = var1.x - var0.x;
        double var9 = var1.z - var0.z;
        double var11 = var7 * var7 + var9 * var9;
        if (var11 < 1.0E-8D) {
            return false;
        } else {
            double var13 = 1.0D / Math.sqrt(var11);
            var7 *= var13;
            var9 *= var13;
            var2 += 2;
            var4 += 2;
            if (!this.a(var5, MathHelper.floor(var0.y), var6, var2, var3, var4, var0, var7, var9)) {
                return false;
            } else {
                var2 -= 2;
                var4 -= 2;
                double var15 = 1.0D / Math.abs(var7);
                double var17 = 1.0D / Math.abs(var9);
                double var19 = (double)var5 - var0.x;
                double var21 = (double)var6 - var0.z;
                if (var7 >= 0.0D) {
                    ++var19;
                }

                if (var9 >= 0.0D) {
                    ++var21;
                }

                var19 /= var7;
                var21 /= var9;
                int var23 = var7 < 0.0D ? -1 : 1;
                int var24 = var9 < 0.0D ? -1 : 1;
                int var25 = MathHelper.floor(var1.x);
                int var26 = MathHelper.floor(var1.z);
                int var27 = var25 - var5;
                int var28 = var26 - var6;

                do {
                    if (var27 * var23 <= 0 && var28 * var24 <= 0) {
                        return true;
                    }

                    if (var19 < var21) {
                        var19 += var15;
                        var5 += var23;
                        var27 = var25 - var5;
                    } else {
                        var21 += var17;
                        var6 += var24;
                        var28 = var26 - var6;
                    }
                } while(this.a(var5, MathHelper.floor(var0.y), var6, var2, var3, var4, var0, var7, var9));

                return false;
            }
        }
    }

    private boolean a(int var0, int var1, int var2, int var3, int var4, int var5, Vec3D var6, double var7, double var9) {
        int var11 = var0 - var3 / 2;
        int var12 = var2 - var5 / 2;
        if (!this.b(var11, var1, var12, var3, var4, var5, var6, var7, var9)) {
            return false;
        } else {
            for(int var13 = var11; var13 < var11 + var3; ++var13) {
                for(int var14 = var12; var14 < var12 + var5; ++var14) {
                    double var15 = (double)var13 + 0.5D - var6.x;
                    double var17 = (double)var14 + 0.5D - var6.z;
                    if (!(var15 * var7 + var17 * var9 < 0.0D)) {
                        PathType var19 = this.o.a(this.b, var13, var1 - 1, var14, (NPCPlayer) this.a, var3, var4, var5, true, true);
                        if (!this.a(var19)) {
                            return false;
                        }

                        var19 = this.o.a(this.b, var13, var1, var14, (NPCPlayer) this.a, var3, var4, var5, true, true);
                        float var20 = ((NPCPlayer) this.a).a(var19);
                        if (var20 < 0.0F || var20 >= 8.0F) {
                            return false;
                        }

                        if (var19 == PathType.DAMAGE_FIRE || var19 == PathType.DANGER_FIRE || var19 == PathType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    protected boolean a(PathType var0) {
        if (var0 == PathType.WATER) {
            return false;
        } else if (var0 == PathType.LAVA) {
            return false;
        } else {
            return var0 != PathType.OPEN;
        }
    }

    private boolean b(int var0, int var1, int var2, int var3, int var4, int var5, Vec3D var6, double var7, double var9) {
        Iterator var12 = BlockPosition.a(new BlockPosition(var0, var1, var2), new BlockPosition(var0 + var3 - 1, var1 + var4 - 1, var2 + var5 - 1)).iterator();

        BlockPosition last;
        double var13;
        double var15;
        do {
            if (!var12.hasNext()) {
                return true;
            }

            last = (BlockPosition)var12.next();
            var13 = (double)last.getX() + 0.5D - var6.x;
            var15 = (double)last.getZ() + 0.5D - var6.z;
        } while(var13 * var7 + var15 * var9 < 0.0D || this.b.getType(last).a(this.b, last, PathMode.LAND));

        return false;
    }

    public void a(boolean var0) {
        this.o.b(var0);
    }

    public boolean f() {
        return this.o.c();
    }

    public void c(boolean var0) {
        this.p = var0;
    }
}
