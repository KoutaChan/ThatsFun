package me.koutachan.thatsfun.impl.pathfinder;

import me.koutachan.thatsfun.impl.NPCPlayer;
import me.koutachan.thatsfun.impl.controller.NPCNavigation;
import net.minecraft.server.v1_16_R3.*;

public class NPCPathfinderGoalDoorInteract extends PathfinderGoal {
    protected NPCPlayer entity;
    protected BlockPosition door;
    protected boolean f;
    private boolean a;
    private float b;
    private float c;

    public NPCPathfinderGoalDoorInteract(NPCPlayer var0) {
        this.door = BlockPosition.ZERO;
        this.entity = var0;
    }

    protected boolean g() {
        if (!this.f) {
            return false;
        } else {
            IBlockData var0 = this.entity.world.getType(this.door);
            if (!(var0.getBlock() instanceof BlockDoor)) {
                this.f = false;
                return false;
            } else {
                return (Boolean) var0.get(BlockDoor.OPEN);
            }
        }
    }

    protected void a(boolean var0) {
        if (this.f) {
            IBlockData var1 = this.entity.world.getType(this.door);
            if (var1.getBlock() instanceof BlockDoor) {
                ((BlockDoor) var1.getBlock()).setDoor(this.entity.world, var1, this.door, var0);
            }
        }

    }

    public boolean a() {
        if (!this.entity.positionChanged) {
            return false;
        } else {
            NPCNavigation var0 = (NPCNavigation) this.entity.getNavigation();
            PathEntity var1 = var0.k();
            if (var1 != null && !var1.c() && var0.f()) {
                for (int var2 = 0; var2 < Math.min(var1.f() + 2, var1.e()); ++var2) {
                    PathPoint var3 = var1.a(var2);
                    this.door = new BlockPosition(var3.a, var3.b + 1, var3.c);
                    if (!(this.entity.h((double) this.door.getX(), this.entity.locY(), (double) this.door.getZ()) > 2.25D)) {
                        this.f = BlockDoor.a(this.entity.world, this.door);
                        if (this.f) {
                            return true;
                        }
                    }
                }

                this.door = this.entity.getChunkCoordinates().up();
                this.f = BlockDoor.a(this.entity.world, this.door);
                return this.f;
            } else {
                return false;
            }
        }
    }

    public boolean b() {
        return !this.a;
    }

    public void c() {
        this.a = false;
        this.b = (float) ((double) this.door.getX() + 0.5D - this.entity.locX());
        this.c = (float) ((double) this.door.getZ() + 0.5D - this.entity.locZ());
    }

    public void e() {
        float var0 = (float) ((double) this.door.getX() + 0.5D - this.entity.locX());
        float var1 = (float) ((double) this.door.getZ() + 0.5D - this.entity.locZ());
        float var2 = this.b * var0 + this.c * var1;
        if (var2 < 0.0F) {
            this.a = true;
        }
    }
}