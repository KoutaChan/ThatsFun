package me.koutachan.thatsfun.impl.controller;

import com.google.common.collect.Lists;
import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityInsentient;

import java.util.List;

public class NPCEntitySenses {
    private final NPCPlayer a;
    private final List<Entity> b = Lists.newArrayList();
    private final List<Entity> c = Lists.newArrayList();

    public NPCEntitySenses(NPCPlayer var0) {
        this.a = var0;
    }

    public void a() {
        this.b.clear();
        this.c.clear();
    }

    public boolean a(Entity var0) {
        if (this.b.contains(var0)) {
            return true;
        } else if (this.c.contains(var0)) {
            return false;
        } else {
            this.a.world.getMethodProfiler().enter("canSee");
            boolean var1 = this.a.hasLineOfSight(var0);
            this.a.world.getMethodProfiler().exit();
            if (var1) {
                this.b.add(var0);
            } else {
                this.c.add(var0);
            }

            return var1;
        }
    }
}
