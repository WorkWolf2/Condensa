package me.workwolf.condensa;

import me.workwolf.condensa.Commands.CondensaCommand;
import me.workwolf.condensa.Events.BlockPlace;
import me.workwolf.condensa.Utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class Condensa extends JavaPlugin {


    @Override
    public void onEnable() {
        Logger.log(Logger.LogLevel.OUTLINE, "*********************");
        Logger.log(Logger.LogLevel.INFO, "Caricando i file di config!");
        saveDefaultConfig();
        Logger.log(Logger.LogLevel.INFO, "File di config caricati con successo!");
        Logger.log(Logger.LogLevel.INFO, "Caricando i comandi!");
        getCommand("condensa").setExecutor(new CondensaCommand(this));
        getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
        Logger.log(Logger.LogLevel.INFO, "Comandi caricati con successo!");
        Logger.log(Logger.LogLevel.OUTLINE, "*********************");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
