package me.koutachan.thatsfun.impl.controller;

import me.koutachan.thatsfun.impl.NPCPlayer;
import net.minecraft.server.v1_16_R3.EntityInsentient;

public class NPCControllerJump {
    private final NPCPlayer player;
    protected boolean a;

    public NPCControllerJump(NPCPlayer var0) {
        this.player = var0;
    }

    public void jump() {
        this.a = true;
    }

    public void b() {
        this.player.setJumping(this.a);
        this.a = false;
    }
}
