package me.koutachan.thatsfun.impl.controller;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NPCPathfinder {
    private final PathPoint[] a = new PathPoint[32];
    private final int b;
    private final NPCPathfinderAbstract c;
    private final Path d = new Path();

    public NPCPathfinder(NPCPathfinderAbstract var0, int var1) {
        this.c = var0;
        this.b = var1;
    }

    @Nullable
    public PathEntity a(ChunkCache var0, NPCPlayer var1, Set<BlockPosition> var2, float var3, int var4, float var5) {
        this.d.a();
        this.c.a(var0, var1);
        PathPoint var6 = this.c.b();
        Map<PathDestination, BlockPosition> var7 = var2.stream().collect(Collectors.toMap((var0x) -> this.c.a(var0x.getX(), (double)var0x.getY(), (double)var0x.getZ()), Function.identity()));
        PathEntity var8 = this.a(var6, var7, var3, var4, var5);
        this.c.a();
        return var8;
    }

    @Nullable
    private PathEntity a(PathPoint var0, Map<PathDestination, BlockPosition> var1, float var2, int var3, float var4) {
        Set<PathDestination> var5 = var1.keySet();
        var0.e = 0.0F;
        var0.f = this.a(var0, var5);
        var0.g = var0.f;
        this.d.a();
        this.d.a(var0);
        Set<PathPoint> var6 = ImmutableSet.of();
        int var7 = 0;
        Set<PathDestination> var8 = Sets.newHashSetWithExpectedSize(var5.size());
        int var9 = (int)((float)this.b * var4);

        while(!this.d.e()) {
            ++var7;
            if (var7 >= var9) {
                break;
            }

            PathPoint var10 = this.d.c();
            var10.i = true;

            for (PathDestination destination : var5) {
                if (var10.c(destination) <= (float) var3) {
                    destination.e();
                    var8.add(destination);
                }
            }

            if (!var8.isEmpty()) {
                break;
            }

            if (!(var10.a(var0) >= var2)) {
                int var11 = this.c.a(this.a, var10);

                for(int var12 = 0; var12 < var11; ++var12) {
                    PathPoint var13 = this.a[var12];
                    float var14 = var10.a(var13);
                    var13.j = var10.j + var14;
                    float var15 = var10.e + var14 + var13.k;
                    if (var13.j < var2 && (!var13.c() || var15 < var13.e)) {
                        var13.h = var10;
                        var13.e = var15;
                        var13.f = this.a(var13, var5) * 1.5F;
                        if (var13.c()) {
                            this.d.a(var13, var13.e + var13.f);
                        } else {
                            var13.g = var13.e + var13.f;
                            this.d.a(var13);
                        }
                    }
                }
            }
        }

        Optional<PathEntity> var10 = !var8.isEmpty() ? var8.stream().map((var1x) -> this.a(var1x.d(), var1.get(var1x), true)).min(Comparator.comparingInt(PathEntity::e)) : var5.stream().map((var1x) -> this.a(var1x.d(), (BlockPosition)var1.get(var1x), false)).min(Comparator.comparingDouble(PathEntity::n).thenComparingInt(PathEntity::e));
        return var10.orElse(null);
    }

    private float a(PathPoint var0, Set<PathDestination> var1) {
        float var2 = 3.4028235E38F;

        float var5;
        for(Iterator<PathDestination> var4 = var1.iterator(); var4.hasNext(); var2 = Math.min(var5, var2)) {
            PathDestination destination = var4.next();
            var5 = var0.a(destination);
            destination.a(var5, var0);
        }

        return var2;
    }

    private PathEntity a(PathPoint var0, BlockPosition var1, boolean var2) {
        List<PathPoint> var3 = Lists.newArrayList();
        PathPoint var4 = var0;
        var3.add(0, var0);

        while(var4.h != null) {
            var4 = var4.h;
            var3.add(0, var4);
        }

        return new PathEntity(var3, var1, var2);
    }
}
