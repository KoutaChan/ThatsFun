package me.koutachan.thatsfun.impl.controller;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.*;

public class NPCControllerLook {
    protected final NPCPlayer a;
    protected float b;
    protected float c;
    protected boolean d;
    protected double e;
    protected double f;
    protected double g;

    public NPCControllerLook(NPCPlayer var0) {
        this.a = var0;
    }

    public void a(Vec3D var0) {
        this.a(var0.x, var0.y, var0.z);
    }

    public void a(Entity var0, float var1, float var2) {
        this.a(var0.locX(), b(var0), var0.locZ(), var1, var2);
    }

    public void a(double var0, double var2, double var4) {
        this.a(var0, var2, var4, (float)this.a.ea(), (float)this.a.d());
    }

    public void a(double var0, double var2, double var4, float var6, float var7) {
        this.e = var0;
        this.f = var2;
        this.g = var4;
        this.b = var6;
        this.c = var7;
        this.d = true;
    }

    public void a() {
        if (this.b()) {
            this.a.pitch = 0.0F;
        }

        if (this.d) {
            this.d = false;
            this.a.aC = this.a(this.a.aC, this.h(), this.b);
            this.a.pitch = this.a(this.a.pitch, this.g(), this.c);
        } else {
            this.a.aC = this.a(this.a.aC, this.a.aA, 10.0F);
        }

        if (!this.a.getNavigation().m()) {
            this.a.aC = MathHelper.b(this.a.aC, this.a.aA, (float)this.a.g());
        }

    }

    protected boolean b() {
        return true;
    }

    public boolean c() {
        return this.d;
    }

    public double d() {
        return this.e;
    }

    public double e() {
        return this.f;
    }

    public double f() {
        return this.g;
    }

    protected float g() {
        double var0 = this.e - this.a.locX();
        double var2 = this.f - this.a.getHeadY();
        double var4 = this.g - this.a.locZ();
        double var6 = (double)MathHelper.sqrt(var0 * var0 + var4 * var4);
        return (float)(-(MathHelper.d(var2, var6) * 57.2957763671875D));
    }

    protected float h() {
        double var0 = this.e - this.a.locX();
        double var2 = this.g - this.a.locZ();
        return (float)(MathHelper.d(var2, var0) * 57.2957763671875D) - 90.0F;
    }

    protected float a(float var0, float var1, float var2) {
        float var3 = MathHelper.c(var0, var1);
        float var4 = MathHelper.a(var3, -var2, var2);
        return var0 + var4;
    }

    private static double b(Entity var0) {
        return var0 instanceof EntityLiving ? var0.getHeadY() : (var0.getBoundingBox().minY + var0.getBoundingBox().maxY) / 2.0D;
    }
}
