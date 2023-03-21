package me.koutachan.thatsfun.impl.connection;

import net.minecraft.server.v1_16_R3.*;

public class FakePlayerConnection extends PlayerConnection {
    public FakePlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        super(minecraftserver, networkmanager, entityplayer);
    }

    @Override
    public void tick() {

    }

    @Override
    public void sendPacket(Packet<?> packet) {

    }

    @Override
    public NetworkManager a() {
        return super.a();
    }
}
