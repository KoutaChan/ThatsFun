package me.koutachan.thatsfun.impl.controller;

import com.google.common.collect.ImmutableSet;
import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class NPCNavigationAbstract {
    protected final NPCPlayer a;
    protected final World b;
    @Nullable
    protected PathEntity c;
    protected double d;
    protected int e;
    protected int f;
    protected Vec3D g;
    protected BaseBlockPosition h;
    protected long i;
    protected long j;
    protected double k;
    protected float l;
    protected boolean m;
    protected long n;
    protected NPCPathfinderAbstract o;
    private BlockPosition p;
    private int q;
    private float r;
    private final NPCPathfinder s;
    private boolean t;

    public NPCNavigationAbstract(NPCPlayer var0, World var1) {
        this.g = Vec3D.ORIGIN;
        this.h = BaseBlockPosition.ZERO;
        this.l = 0.5F;
        this.r = 1.0F;
        this.a = var0;
        this.b = var1;

        int var2 = MathHelper.floor(var0.b(GenericAttributes.FOLLOW_RANGE) * 16.0D);
        this.s = this.a(var2);
    }

    public void g() {
        this.r = 1.0F;
    }

    public void a(float var0) {
        this.r = var0;
    }

    public BlockPosition h() {
        return this.p;
    }

    protected abstract NPCPathfinder a(int var1);

    public void a(double var0) {
        this.d = var0;
    }

    public boolean i() {
        return this.m;
    }

    public void j() {
        if (this.b.getTime() - this.n > 20L) {
            if (this.p != null) {
                this.c = null;
                this.c = this.a(this.p, this.q);
                this.n = this.b.getTime();
                this.m = false;
            }
        } else {
            this.m = true;
        }

    }

    @Nullable
    public final PathEntity a(double var0, double var2, double var4, int var6) {
        return this.a(new BlockPosition(var0, var2, var4), var6);
    }

    @Nullable
    public PathEntity a(Stream<BlockPosition> var0, int var1) {
        return this.a((Set)var0.collect(Collectors.toSet()), 8, false, var1);
    }

    @Nullable
    public PathEntity a(Set<BlockPosition> var0, int var1) {
        return this.a(var0, 8, false, var1);
    }

    @Nullable
    public PathEntity a(BlockPosition var0, int var1) {
        return this.a(ImmutableSet.of(var0), 8, false, var1);
    }

    @Nullable
    public PathEntity a(Entity var0, int var1) {
        return this.a(ImmutableSet.of(var0.getChunkCoordinates()), 16, true, var1);
    }

    @Nullable
    protected PathEntity a(Set<BlockPosition> var0, int var1, boolean var2, int var3) {
        if (var0.isEmpty()) {
            return null;
        } else if (this.a.locY() < 0.0D) {
            return null;
        } else if (!this.a()) {
            return null;
        } else if (this.c != null && !this.c.c() && var0.contains(this.p)) {
            return this.c;
        } else {
            this.b.getMethodProfiler().enter("pathfind");
            float var4 = (float)this.a.b(GenericAttributes.FOLLOW_RANGE);
            BlockPosition var5 = var2 ? this.a.getChunkCoordinates().up() : this.a.getChunkCoordinates();
            int var6 = (int)(var4 + (float)var1);
            ChunkCache var7 = new ChunkCache(this.b, var5.b(-var6, -var6, -var6), var5.b(var6, var6, var6));
            PathEntity var8 = this.s.a(var7, this.a, var0, var4, var3, this.r);
            this.b.getMethodProfiler().exit();
            if (var8 != null && var8.m() != null) {
                this.p = var8.m();
                this.q = var3;
                this.f();
            }

            return var8;
        }
    }

    public boolean a(double var0, double var2, double var4, double var6) {
        return this.a(this.a(var0, var2, var4, 1), var6);
    }

    public boolean a(Entity var0, double var1) {
        PathEntity var3 = this.a(var0, 1);
        return var3 != null && this.a(var3, var1);
    }

    public boolean a(@Nullable PathEntity var0, double var1) {
        if (var0 == null) {
            this.c = null;
            return false;
        } else {
            if (!var0.a(this.c)) {
                this.c = var0;
            }

            if (this.m()) {
                return false;
            } else {
                this.D_();
                if (this.c.e() <= 0) {
                    return false;
                } else {
                    this.d = var1;
                    Vec3D var3 = this.b();
                    this.f = this.e;
                    this.g = var3;
                    return true;
                }
            }
        }
    }

    @Nullable
    public PathEntity k() {
        return this.c;
    }

    public void c() {
        ++this.e;
        if (this.m) {
            this.j();
        }

        if (!this.m()) {
            Vec3D var0;
            if (this.a()) {
                this.l();
            } else if (this.c != null && !this.c.c()) {
                var0 = this.b();
                Vec3D var1 = this.c.a(this.a);
                if (var0.y > var1.y && !this.a.isOnGround() && MathHelper.floor(var0.x) == MathHelper.floor(var1.x) && MathHelper.floor(var0.z) == MathHelper.floor(var1.z)) {
                    this.c.a();
                }
            }

            //TODO: What is This?
            //PacketDebug.a(this.b, this.a, this.c, this.l);
            if (!this.m()) {
                var0 = this.c.a(this.a);
                BlockPosition var1 = new BlockPosition(var0);
                this.a.getControllerMove().a(var0.x, this.b.getType(var1.down()).isAir() ? var0.y : PathfinderNormal.a(this.b, var1), var0.z, this.d);
            }
        }
    }

    protected void l() {
        Vec3D var0 = this.b();
        this.l = this.a.getWidth() > 0.75F ? this.a.getWidth() / 2.0F : 0.75F - this.a.getWidth() / 2.0F;
        BaseBlockPosition var1 = this.c.g();
        double var2 = Math.abs(this.a.locX() - ((double)var1.getX() + 0.5D));
        double var4 = Math.abs(this.a.locY() - (double)var1.getY());
        double var6 = Math.abs(this.a.locZ() - ((double)var1.getZ() + 0.5D));
        boolean var8 = var2 < (double)this.l && var6 < (double)this.l && var4 < 1.0D;
        if (var8 || this.a.b(this.c.h().l) && this.b(var0)) {
            this.c.a();
        }

        this.a(var0);
    }

    private boolean b(Vec3D var0) {
        if (this.c.f() + 1 >= this.c.e()) {
            return false;
        } else {
            Vec3D var1 = Vec3D.c(this.c.g());
            if (!var0.a(var1, 2.0D)) {
                return false;
            } else {
                Vec3D var2 = Vec3D.c(this.c.d(this.c.f() + 1));
                Vec3D var3 = var2.d(var1);
                Vec3D var4 = var0.d(var1);
                return var3.b(var4) > 0.0D;
            }
        }
    }

    protected void a(Vec3D var0) {
        if (this.e - this.f > 100) {
            if (var0.distanceSquared(this.g) < 2.25D) {
                this.t = true;
                this.o();
            } else {
                this.t = false;
            }

            this.f = this.e;
            this.g = var0;
        }

        if (this.c != null && !this.c.c()) {
            BaseBlockPosition var1 = this.c.g();
            if (var1.equals(this.h)) {
                this.i += SystemUtils.getMonotonicMillis() - this.j;
            } else {
                this.h = var1;
                double var2 = var0.f(Vec3D.c(this.h));
                this.k = this.a.dN() > 0.0F ? var2 / (double)this.a.dN() * 1000.0D : 0.0D;
            }

            if (this.k > 0.0D && (double)this.i > this.k * 3.0D) {
                this.e();
            }

            this.j = SystemUtils.getMonotonicMillis();
        }

    }

    private void e() {
        this.f();
        this.o();
    }

    private void f() {
        this.h = BaseBlockPosition.ZERO;
        this.i = 0L;
        this.k = 0.0D;
        this.t = false;
    }

    public boolean m() {
        return this.c == null || this.c.c();
    }

    public boolean n() {
        return !this.m();
    }

    public void o() {
        this.c = null;
    }

    protected abstract Vec3D b();

    protected abstract boolean a();

    protected boolean p() {
        return this.a.aH() || this.a.aQ();
    }

    protected void D_() {
        if (this.c != null) {
            for(int var0 = 0; var0 < this.c.e(); ++var0) {
                PathPoint var1 = this.c.a(var0);
                PathPoint var2 = var0 + 1 < this.c.e() ? this.c.a(var0 + 1) : null;
                IBlockData var3 = this.b.getType(new BlockPosition(var1.a, var1.b, var1.c));
                if (var3.a(Blocks.CAULDRON)) {
                    this.c.a(var0, var1.a(var1.a, var1.b + 1, var1.c));
                    if (var2 != null && var1.b >= var2.b) {
                        this.c.a(var0 + 1, var1.a(var2.a, var1.b + 1, var2.c));
                    }
                }
            }

        }
    }

    protected abstract boolean a(Vec3D var1, Vec3D var2, int var3, int var4, int var5);

    public boolean a(BlockPosition var0) {
        BlockPosition var1 = var0.down();
        return this.b.getType(var1).i(this.b, var1);
    }

    public NPCPathfinderAbstract q() {
        return this.o;
    }

    public void d(boolean var0) {
        this.o.c(var0);
    }

    public boolean r() {
        return this.o.e();
    }

    public void b(BlockPosition var0) {
        if (this.c != null && !this.c.c() && this.c.e() != 0) {
            PathPoint var1 = this.c.d();
            Vec3D var2 = new Vec3D(((double)var1.a + this.a.locX()) / 2.0D, ((double)var1.b + this.a.locY()) / 2.0D, ((double)var1.c + this.a.locZ()) / 2.0D);
            if (var0.a(var2, (double)(this.c.e() - this.c.f()))) {
                this.j();
            }

        }
    }

    public boolean t() {
        return this.t;
    }
}
