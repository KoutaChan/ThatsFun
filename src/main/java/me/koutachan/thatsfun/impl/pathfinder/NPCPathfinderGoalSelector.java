package me.koutachan.thatsfun.impl.pathfinder;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.v1_16_R3.GameProfilerFiller;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import net.minecraft.server.v1_16_R3.PathfinderGoalWrapped;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NPCPathfinderGoalSelector {
    private static final PathfinderGoalWrapped b = new PathfinderGoalWrapped(2147483647, new PathfinderGoal() {
        public boolean a() {
            return false;
        }
    }) {
        public boolean g() {
            return false;
        }
    };
    private final Map<PathfinderGoal.Type, PathfinderGoalWrapped> c = new EnumMap(PathfinderGoal.Type.class);
    private final Set<PathfinderGoalWrapped> d = new ConcurrentSet<>();
    private final Supplier<GameProfilerFiller> e;
    private final EnumSet<PathfinderGoal.Type> f = EnumSet.noneOf(PathfinderGoal.Type.class);
    private int g = 3;

    public NPCPathfinderGoalSelector(Supplier<GameProfilerFiller> var0) {
        this.e = var0;
    }

    public void a(int var0, PathfinderGoal var1) {
        this.d.add(new PathfinderGoalWrapped(var0, var1));
    }

    public void plus(PathfinderGoal var0) {
        this.d.stream().filter((var1) -> var1.j() == var0).filter(PathfinderGoalWrapped::g).forEach(PathfinderGoalWrapped::d);
        this.d.removeIf((var1) -> var1.j() == var0);
    }

    public void doTick() {
        GameProfilerFiller var0 = this.e.get();
        var0.enter("goalCleanup");
        //A->C
        //this.d.stream().filter(PathfinderGoalWrapped::a).forEach(d -> d.j().c());

        this.d().filter((var0x) -> {
            if (var0x.g()) {
                Stream<PathfinderGoal.Type> var10000 = var0x.i().stream();
                return var10000.anyMatch(this.f::contains) || !var0x.b();
            }
            return true;
        }).forEach(PathfinderGoal::d);
        this.c.forEach((var0x, var1) -> {
            if (!var1.g()) {
                this.c.remove(var0x);
            }
        });
        var0.exit();
        var0.enter("goalUpdate");
        this.d.stream().filter((var0x) -> !var0x.g()).filter((var0x) -> {
            Stream<PathfinderGoal.Type> var10000 = var0x.i().stream();
            return var10000.noneMatch(this.f::contains);
        }).filter((var0x) -> var0x.i().stream().allMatch((var1) -> this.c.getOrDefault(var1, b).a(var0x))).filter(PathfinderGoalWrapped::a).forEach((var0x) -> {
            var0x.i().forEach((var1) -> {
                PathfinderGoalWrapped var2 = this.c.getOrDefault(var1, b);
                var2.d();
                this.c.put(var1, var0x);
            });
            var0x.c();
        });
        var0.exit();
        var0.enter("goalTick");
        this.d().forEach(PathfinderGoalWrapped::e);
        var0.exit();
    }

    public Stream<PathfinderGoalWrapped> d() {
        return this.d.stream().filter(PathfinderGoalWrapped::g);
    }

    public void remove(PathfinderGoal goal) {
        d.removeIf(d -> d.j() == goal);
    }

    public void a(PathfinderGoal.Type var0) {
        this.f.add(var0);
    }

    public void b(PathfinderGoal.Type var0) {
        this.f.remove(var0);
    }

    public void plus(PathfinderGoal.Type var0, boolean var1) {
        if (var1) {
            this.b(var0);
        } else {
            this.a(var0);
        }

    }
}
