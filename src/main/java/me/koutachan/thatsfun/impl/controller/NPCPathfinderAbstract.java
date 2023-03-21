package me.koutachan.thatsfun.impl.controller;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public abstract class NPCPathfinderAbstract {
    protected ChunkCache a;
    protected NPCPlayer b;
    protected final Int2ObjectMap<PathPoint> c = new Int2ObjectOpenHashMap();
    protected int d;
    protected int e;
    protected int f;
    protected boolean g;
    protected boolean h;
    protected boolean i;

    public NPCPathfinderAbstract() {
    }

    public void a(ChunkCache var0, NPCPlayer var1) {
        this.a = var0;
        this.b = var1;
        this.c.clear();
        this.d = MathHelper.d(var1.getWidth() + 1.0F);
        this.e = MathHelper.d(var1.getHeight() + 1.0F);
        this.f = MathHelper.d(var1.getWidth() + 1.0F);
    }

    public void a() {
        this.a = null;
        this.b = null;
    }

    protected PathPoint a(BlockPosition var0) {
        return this.a(var0.getX(), var0.getY(), var0.getZ());
    }

    protected PathPoint a(int var0, int var1, int var2) {
        return this.c.computeIfAbsent(PathPoint.b(var0, var1, var2), (var3) -> new PathPoint(var0, var1, var2));
    }

    public abstract PathPoint b();

    public abstract PathDestination a(double var1, double var3, double var5);

    public abstract int a(PathPoint[] var1, PathPoint var2);

    public abstract PathType a(IBlockAccess var1, int var2, int var3, int var4, NPCPlayer var5, int var6, int var7, int var8, boolean var9, boolean var10);

    public abstract PathType a(IBlockAccess var1, int var2, int var3, int var4);

    public void a(boolean var0) {
        this.g = var0;
    }

    public void b(boolean var0) {
        this.h = var0;
    }

    public void c(boolean var0) {
        this.i = var0;
    }

    public boolean c() {
        return this.g;
    }

    public boolean d() {
        return this.h;
    }

    public boolean e() {
        return this.i;
    }
}
