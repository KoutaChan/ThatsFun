package me.koutachan.thatsfun.commands;

import me.koutachan.thatsfun.NPC;
import me.koutachan.thatsfun.impl.NPCPlayer;
import me.koutachan.thatsfun.impl.NPCRawData;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

public class Fun implements CommandExecutor {
    private NPC<Player> npc;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (this.npc == null) {
                WorldServer server = ((CraftWorld) player.getWorld()).getHandle();
                this.npc = new NPCRawData(new NPCPlayer(player.getLocation(), server.getMinecraftServer(), server));
                this.npc.getEntity().setInvulnerable(true);
            } else {
                npc.moveTo(10, player.getLocation());
                Bukkit.broadcastMessage("added!");
            }
        }
        return true;
    }
}
