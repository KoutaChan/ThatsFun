package me.koutachan.thatsfun.impl.controller;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Iterator;

public class NPCPathfinderNormal extends NPCPathfinderAbstract {
    protected float j;
    private final Long2ObjectMap<PathType> k = new Long2ObjectOpenHashMap<>();
    private final Object2BooleanMap<AxisAlignedBB> l = new Object2BooleanOpenHashMap<>();

    public NPCPathfinderNormal() {

    }

    public void a(ChunkCache var0, NPCPlayer var1) {
        super.a(var0, var1);
        this.j = var1.a(PathType.WATER);
    }

    public void a() {
        this.b.a(PathType.WATER, this.j);
        this.k.clear();
        this.l.clear();
        super.a();
    }

    public PathPoint b() {
        BlockPosition.MutableBlockPosition var1 = new BlockPosition.MutableBlockPosition();
        int var0 = MathHelper.floor(this.b.locY());
        IBlockData var2 = this.a.getType(var1.c(this.b.locX(), (double)var0, this.b.locZ()));
        BlockPosition var3;
        if (!this.b.a(var2.getFluid().getType())) {
            if (this.e() && this.b.isInWater()) {
                while(true) {
                    if (var2.getBlock() != Blocks.WATER && var2.getFluid() != FluidTypes.WATER.a(false)) {
                        --var0;
                        break;
                    }

                    ++var0;
                    var2 = this.a.getType(var1.c(this.b.locX(), (double)var0, this.b.locZ()));
                }
            } else if (this.b.isOnGround()) {
                var0 = MathHelper.floor(this.b.locY() + 0.5D);
            } else {
                for(var3 = this.b.getChunkCoordinates(); (this.a.getType(var3).isAir() || this.a.getType(var3).a(this.a, var3, PathMode.LAND)) && var3.getY() > 0; var3 = var3.down()) {
                }

                var0 = var3.up().getY();
            }
        } else {
            while(true) {
                if (!this.b.a(var2.getFluid().getType())) {
                    --var0;
                    break;
                }

                ++var0;
                var2 = this.a.getType(var1.c(this.b.locX(), (double)var0, this.b.locZ()));
            }
        }

        var3 = this.b.getChunkCoordinates();
        PathType var4 = this.a(this.b, var3.getX(), var0, var3.getZ());
        if (this.b.a(var4) < 0.0F) {
            AxisAlignedBB var5 = this.b.getBoundingBox();
            if (this.b(var1.c(var5.minX, (double)var0, var5.minZ)) || this.b(var1.c(var5.minX, (double)var0, var5.maxZ)) || this.b(var1.c(var5.maxX, (double)var0, var5.minZ)) || this.b(var1.c(var5.maxX, (double)var0, var5.maxZ))) {
                PathPoint var6 = this.a((BlockPosition)var1);
                var6.l = this.a(this.b, var6.a());
                var6.k = this.b.a(var6.l);
                return var6;
            }
        }

        PathPoint var5 = this.a(var3.getX(), var0, var3.getZ());
        var5.l = this.a(this.b, var5.a());
        var5.k = this.b.a(var5.l);
        return var5;
    }

    private boolean b(BlockPosition var0) {
        PathType var1 = this.a(this.b, var0);
        return this.b.a(var1) >= 0.0F;
    }

    public PathDestination a(double var0, double var2, double var4) {
        return new PathDestination(this.a(MathHelper.floor(var0), MathHelper.floor(var2), MathHelper.floor(var4)));
    }

    public int a(PathPoint[] var0, PathPoint var1) {
        int var2 = 0;
        int var3 = 0;
        PathType var4 = this.a(this.b, var1.a, var1.b + 1, var1.c);
        PathType var5 = this.a(this.b, var1.a, var1.b, var1.c);
        if (this.b.a(var4) >= 0.0F && var5 != PathType.STICKY_HONEY) {
            var3 = MathHelper.d(Math.max(1.0F, this.b.G));
        }

        double var6 = a((IBlockAccess)this.a, (BlockPosition)(new BlockPosition(var1.a, var1.b, var1.c)));
        PathPoint var8 = this.a(var1.a, var1.b, var1.c + 1, var3, var6, EnumDirection.SOUTH, var5);
        if (this.a(var8, var1)) {
            var0[var2++] = var8;
        }

        PathPoint var9 = this.a(var1.a - 1, var1.b, var1.c, var3, var6, EnumDirection.WEST, var5);
        if (this.a(var9, var1)) {
            var0[var2++] = var9;
        }

        PathPoint var10 = this.a(var1.a + 1, var1.b, var1.c, var3, var6, EnumDirection.EAST, var5);
        if (this.a(var10, var1)) {
            var0[var2++] = var10;
        }

        PathPoint var11 = this.a(var1.a, var1.b, var1.c - 1, var3, var6, EnumDirection.NORTH, var5);
        if (this.a(var11, var1)) {
            var0[var2++] = var11;
        }

        PathPoint var12 = this.a(var1.a - 1, var1.b, var1.c - 1, var3, var6, EnumDirection.NORTH, var5);
        if (this.a(var1, var9, var11, var12)) {
            var0[var2++] = var12;
        }

        PathPoint var13 = this.a(var1.a + 1, var1.b, var1.c - 1, var3, var6, EnumDirection.NORTH, var5);
        if (this.a(var1, var10, var11, var13)) {
            var0[var2++] = var13;
        }

        PathPoint var14 = this.a(var1.a - 1, var1.b, var1.c + 1, var3, var6, EnumDirection.SOUTH, var5);
        if (this.a(var1, var9, var8, var14)) {
            var0[var2++] = var14;
        }

        PathPoint var15 = this.a(var1.a + 1, var1.b, var1.c + 1, var3, var6, EnumDirection.SOUTH, var5);
        if (this.a(var1, var10, var8, var15)) {
            var0[var2++] = var15;
        }

        return var2;
    }

    private boolean a(PathPoint var0, PathPoint var1) {
        return var0 != null && !var0.i && (var0.k >= 0.0F || var1.k < 0.0F);
    }

    private boolean a(PathPoint var0, @Nullable PathPoint var1, @Nullable PathPoint var2, @Nullable PathPoint var3) {
        if (var3 != null && var2 != null && var1 != null) {
            if (var3.i) {
                return false;
            } else if (var2.b <= var0.b && var1.b <= var0.b) {
                if (var1.l != PathType.WALKABLE_DOOR && var2.l != PathType.WALKABLE_DOOR && var3.l != PathType.WALKABLE_DOOR) {
                    boolean var4 = var2.l == PathType.FENCE && var1.l == PathType.FENCE && (double)this.b.getWidth() < 0.5D;
                    return var3.k >= 0.0F && (var2.b < var0.b || var2.k >= 0.0F || var4) && (var1.b < var0.b || var1.k >= 0.0F || var4);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean a(PathPoint var0) {
        Vec3D var1 = new Vec3D((double)var0.a - this.b.locX(), (double)var0.b - this.b.locY(), (double)var0.c - this.b.locZ());
        AxisAlignedBB var2 = this.b.getBoundingBox();
        int var3 = MathHelper.f(var1.f() / var2.a());
        var1 = var1.a((double)(1.0F / (float)var3));

        for(int var4 = 1; var4 <= var3; ++var4) {
            var2 = var2.c(var1);
            if (this.a(var2)) {
                return false;
            }
        }

        return true;
    }

    public static double a(IBlockAccess var0, BlockPosition var1) {
        BlockPosition var2 = var1.down();
        VoxelShape var3 = var0.getType(var2).getCollisionShape(var0, var2);
        return (double)var2.getY() + (var3.isEmpty() ? 0.0D : var3.c(EnumDirection.EnumAxis.Y));
    }

    @Nullable
    private PathPoint a(int var0, int var1, int var2, int var3, double var4, EnumDirection var6, PathType var7) {
        PathPoint var8 = null;
        BlockPosition.MutableBlockPosition var9 = new BlockPosition.MutableBlockPosition();
        double var10 = a((IBlockAccess)this.a, (BlockPosition)var9.d(var0, var1, var2));
        if (var10 - var4 > 1.125D) {
            return null;
        } else {
            PathType var12 = this.a(this.b, var0, var1, var2);
            float var13 = this.b.a(var12);
            double var14 = (double)this.b.getWidth() / 2.0D;
            if (var13 >= 0.0F) {
                var8 = this.a(var0, var1, var2);
                var8.l = var12;
                var8.k = Math.max(var8.k, var13);
            }

            if (var7 == PathType.FENCE && var8 != null && var8.k >= 0.0F && !this.a(var8)) {
                var8 = null;
            }

            if (var12 == PathType.WALKABLE) {
                return var8;
            } else {
                if ((var8 == null || var8.k < 0.0F) && var3 > 0 && var12 != PathType.FENCE && var12 != PathType.UNPASSABLE_RAIL && var12 != PathType.TRAPDOOR) {
                    var8 = this.a(var0, var1 + 1, var2, var3 - 1, var4, var6, var7);
                    if (var8 != null && (var8.l == PathType.OPEN || var8.l == PathType.WALKABLE) && this.b.getWidth() < 1.0F) {
                        double var16 = (double)(var0 - var6.getAdjacentX()) + 0.5D;
                        double var18 = (double)(var2 - var6.getAdjacentZ()) + 0.5D;
                        AxisAlignedBB var20 = new AxisAlignedBB(var16 - var14, a((IBlockAccess)this.a, (BlockPosition)var9.c(var16, (double)(var1 + 1), var18)) + 0.001D, var18 - var14, var16 + var14, (double)this.b.getHeight() + a((IBlockAccess)this.a, (BlockPosition)var9.c((double)var8.a, (double)var8.b, (double)var8.c)) - 0.002D, var18 + var14);
                        if (this.a(var20)) {
                            var8 = null;
                        }
                    }
                }

                if (var12 == PathType.WATER && !this.e()) {
                    if (this.a(this.b, var0, var1 - 1, var2) != PathType.WATER) {
                        return var8;
                    }

                    while(var1 > 0) {
                        --var1;
                        var12 = this.a(this.b, var0, var1, var2);
                        if (var12 != PathType.WATER) {
                            return var8;
                        }

                        var8 = this.a(var0, var1, var2);
                        var8.l = var12;
                        var8.k = Math.max(var8.k, this.b.a(var12));
                    }
                }

                if (var12 == PathType.OPEN) {
                    int var16 = 0;
                    int var17 = var1;

                    while(var12 == PathType.OPEN) {
                        --var1;
                        PathPoint var18;
                        if (var1 < 0) {
                            var18 = this.a(var0, var17, var2);
                            var18.l = PathType.BLOCKED;
                            var18.k = -1.0F;
                            return var18;
                        }

                        if (var16++ >= this.b.bP()) {
                            var18 = this.a(var0, var1, var2);
                            var18.l = PathType.BLOCKED;
                            var18.k = -1.0F;
                            return var18;
                        }

                        var12 = this.a(this.b, var0, var1, var2);
                        var13 = this.b.a(var12);
                        if (var12 != PathType.OPEN && var13 >= 0.0F) {
                            var8 = this.a(var0, var1, var2);
                            var8.l = var12;
                            var8.k = Math.max(var8.k, var13);
                            break;
                        }

                        if (var13 < 0.0F) {
                            var18 = this.a(var0, var1, var2);
                            var18.l = PathType.BLOCKED;
                            var18.k = -1.0F;
                            return var18;
                        }
                    }
                }

                if (var12 == PathType.FENCE) {
                    var8 = this.a(var0, var1, var2);
                    var8.i = true;
                    var8.l = var12;
                    var8.k = var12.a();
                }

                return var8;
            }
        }
    }

    private boolean a(AxisAlignedBB var0) {
        return this.l.computeIfAbsent(var0, (var1) -> {
            return !this.a.getCubes(this.b, var0);
        });
    }

    public PathType a(IBlockAccess var0, int var1, int var2, int var3, NPCPlayer var4, int var5, int var6, int var7, boolean var8, boolean var9) {
        EnumSet<PathType> var10 = EnumSet.noneOf(PathType.class);
        PathType var11 = PathType.BLOCKED;
        BlockPosition var12 = var4.getChunkCoordinates();
        var11 = this.a(var0, var1, var2, var3, var5, var6, var7, var8, var9, var10, var11, var12);
        if (var10.contains(PathType.FENCE)) {
            return PathType.FENCE;
        } else if (var10.contains(PathType.UNPASSABLE_RAIL)) {
            return PathType.UNPASSABLE_RAIL;
        } else {
            PathType var13 = PathType.BLOCKED;
            Iterator var15 = var10.iterator();

            while(var15.hasNext()) {
                PathType var16 = (PathType)var15.next();
                if (var4.a(var16) < 0.0F) {
                    return var16;
                }
                if (var4.a(var16) >= var4.a(var13)) {
                    var13 = var16;
                }
            }

            if (var11 == PathType.OPEN && var4.a(var13) == 0.0F && var5 <= 1) {
                return PathType.OPEN;
            } else {
                return var13;
            }
        }
    }

    public PathType a(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, EnumSet<PathType> var9, PathType var10, BlockPosition var11) {
        for(int var12 = 0; var12 < var4; ++var12) {
            for(int var13 = 0; var13 < var5; ++var13) {
                for(int var14 = 0; var14 < var6; ++var14) {
                    int var15 = var12 + var1;
                    int var16 = var13 + var2;
                    int var17 = var14 + var3;
                    PathType var18 = this.a(var0, var15, var16, var17);
                    var18 = this.a(var0, var7, var8, var11, var18);
                    if (var12 == 0 && var13 == 0 && var14 == 0) {
                        var10 = var18;
                    }

                    var9.add(var18);
                }
            }
        }

        return var10;
    }

    protected PathType a(IBlockAccess var0, boolean var1, boolean var2, BlockPosition var3, PathType var4) {
        if (var4 == PathType.DOOR_WOOD_CLOSED && var1 && var2) {
            var4 = PathType.WALKABLE_DOOR;
        }

        if (var4 == PathType.DOOR_OPEN && !var2) {
            var4 = PathType.BLOCKED;
        }

        if (var4 == PathType.RAIL && !(var0.getType(var3).getBlock() instanceof BlockMinecartTrackAbstract) && !(var0.getType(var3.down()).getBlock() instanceof BlockMinecartTrackAbstract)) {
            var4 = PathType.UNPASSABLE_RAIL;
        }

        if (var4 == PathType.LEAVES) {
            var4 = PathType.BLOCKED;
        }

        return var4;
    }

    private PathType a(NPCPlayer var0, BlockPosition var1) {
        return this.a(var0, var1.getX(), var1.getY(), var1.getZ());
    }

    private PathType a(NPCPlayer var0, int var1, int var2, int var3) {
        return (PathType)this.k.computeIfAbsent(BlockPosition.a(var1, var2, var3), (var4) -> {
            return this.a(this.a, var1, var2, var3, var0, this.d, this.e, this.f, this.d(), this.c());
        });
    }

    public PathType a(IBlockAccess var0, int var1, int var2, int var3) {
        return a(var0, new BlockPosition.MutableBlockPosition(var1, var2, var3));
    }

    public static PathType a(IBlockAccess var0, BlockPosition.MutableBlockPosition var1) {
        int var2 = var1.getX();
        int var3 = var1.getY();
        int var4 = var1.getZ();
        PathType var5 = b(var0, var1);
        if (var5 == PathType.OPEN && var3 >= 1) {
            PathType var6 = b(var0, var1.d(var2, var3 - 1, var4));
            var5 = var6 != PathType.WALKABLE && var6 != PathType.OPEN && var6 != PathType.WATER && var6 != PathType.LAVA ? PathType.WALKABLE : PathType.OPEN;
            if (var6 == PathType.DAMAGE_FIRE) {
                var5 = PathType.DAMAGE_FIRE;
            }

            if (var6 == PathType.DAMAGE_CACTUS) {
                var5 = PathType.DAMAGE_CACTUS;
            }

            if (var6 == PathType.DAMAGE_OTHER) {
                var5 = PathType.DAMAGE_OTHER;
            }

            if (var6 == PathType.STICKY_HONEY) {
                var5 = PathType.STICKY_HONEY;
            }
        }

        if (var5 == PathType.WALKABLE) {
            var5 = a(var0, var1.d(var2, var3, var4), var5);
        }

        return var5;
    }

    public static PathType a(IBlockAccess var0, BlockPosition.MutableBlockPosition var1, PathType var2) {
        int var3 = var1.getX();
        int var4 = var1.getY();
        int var5 = var1.getZ();

        for(int var6 = -1; var6 <= 1; ++var6) {
            for(int var7 = -1; var7 <= 1; ++var7) {
                for(int var8 = -1; var8 <= 1; ++var8) {
                    if (var6 != 0 || var8 != 0) {
                        var1.d(var3 + var6, var4 + var7, var5 + var8);
                        IBlockData var9 = var0.getType(var1);
                        if (var9.a(Blocks.CACTUS)) {
                            return PathType.DANGER_CACTUS;
                        }

                        if (var9.a(Blocks.SWEET_BERRY_BUSH)) {
                            return PathType.DANGER_OTHER;
                        }

                        if (a(var9)) {
                            return PathType.DANGER_FIRE;
                        }

                        if (var0.getFluid(var1).a(TagsFluid.WATER)) {
                            return PathType.WATER_BORDER;
                        }
                    }
                }
            }
        }

        return var2;
    }

    protected static PathType b(IBlockAccess var0, BlockPosition var1) {
        IBlockData var2 = var0.getType(var1);
        Block var3 = var2.getBlock();
        Material var4 = var2.getMaterial();
        if (var2.isAir()) {
            return PathType.OPEN;
        } else if (!var2.a(TagsBlock.TRAPDOORS) && !var2.a(Blocks.LILY_PAD)) {
            if (var2.a(Blocks.CACTUS)) {
                return PathType.DAMAGE_CACTUS;
            } else if (var2.a(Blocks.SWEET_BERRY_BUSH)) {
                return PathType.DAMAGE_OTHER;
            } else if (var2.a(Blocks.HONEY_BLOCK)) {
                return PathType.STICKY_HONEY;
            } else if (var2.a(Blocks.COCOA)) {
                return PathType.COCOA;
            } else {
                Fluid var5 = var0.getFluid(var1);
                if (var5.a(TagsFluid.WATER)) {
                    return PathType.WATER;
                } else if (var5.a(TagsFluid.LAVA)) {
                    return PathType.LAVA;
                } else if (a(var2)) {
                    return PathType.DAMAGE_FIRE;
                } else if (BlockDoor.l(var2) && !(Boolean)var2.get(BlockDoor.OPEN)) {
                    return PathType.DOOR_WOOD_CLOSED;
                } else if (var3 instanceof BlockDoor && var4 == Material.ORE && !(Boolean)var2.get(BlockDoor.OPEN)) {
                    return PathType.DOOR_IRON_CLOSED;
                } else if (var3 instanceof BlockDoor && (Boolean)var2.get(BlockDoor.OPEN)) {
                    return PathType.DOOR_OPEN;
                } else if (var3 instanceof BlockMinecartTrackAbstract) {
                    return PathType.RAIL;
                } else if (var3 instanceof BlockLeaves) {
                    return PathType.LEAVES;
                } else if (!var3.a(TagsBlock.FENCES) && !var3.a(TagsBlock.WALLS) && (!(var3 instanceof BlockFenceGate) || (Boolean)var2.get(BlockFenceGate.OPEN))) {
                    return !var2.a(var0, var1, PathMode.LAND) ? PathType.BLOCKED : PathType.OPEN;
                } else {
                    return PathType.FENCE;
                }
            }
        } else {
            return PathType.TRAPDOOR;
        }
    }

    private static boolean a(IBlockData var0) {
        return var0.a(TagsBlock.FIRE) || var0.a(Blocks.LAVA) || var0.a(Blocks.MAGMA_BLOCK) || BlockCampfire.g(var0);
    }
}
