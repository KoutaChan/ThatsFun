package me.koutachan.thatsfun;

import me.koutachan.thatsfun.commands.Fun;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThatsFun extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("fun").setExecutor(new Fun());
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
