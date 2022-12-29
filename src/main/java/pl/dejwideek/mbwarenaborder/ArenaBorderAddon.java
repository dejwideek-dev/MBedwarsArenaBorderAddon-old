package pl.dejwideek.mbwarenaborder;

import co.aikar.commands.BukkitCommandManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.dejwideek.mbwarenaborder.commands.ReloadCmd;
import pl.dejwideek.mbwarenaborder.events.GameStartEvent;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ALL")
public class ArenaBorderAddon extends JavaPlugin {

    public YamlDocument config;

    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("MBedwars") != null) {
            final int supportedAPIVersion = 9;

            try {
                Class apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
                int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

                if (apiVersion < supportedAPIVersion)
                    throw new IllegalStateException();
            } catch(Exception e) {
                this.getLogger().warning("Your MBedwars version is not supported. Supported version: 5.0.8 or higher!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

            try {
                config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
                        GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(),
                        DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            updateCheck();
            registerEvents();
            registerCommands();
        }
        else {
            this.getLogger().warning("MBedwars is not enabled!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void updateCheck() {
        new UpdateChecker(this, 106051).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                this.getLogger().info("You are using latest version.");
            }
            else {
                this.getLogger().info("There is a new update available. (v" + version + ")");
                this.getLogger().info("https://spigotmc.org/resources/106051/updates");
            }
        });
    }

    public void registerEvents() {
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new GameStartEvent(this), this);
    }

    public void registerCommands() {
        BukkitCommandManager manager = new BukkitCommandManager(this);

        manager.registerCommand(new ReloadCmd(this));
    }
}
