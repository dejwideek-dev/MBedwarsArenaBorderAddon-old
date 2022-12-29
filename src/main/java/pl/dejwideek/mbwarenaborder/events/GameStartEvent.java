package pl.dejwideek.mbwarenaborder.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.RegenerationType;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.dejwideek.mbwarenaborder.ArenaBorderAddon;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class GameStartEvent implements Listener {

    private static ArenaBorderAddon plugin;

    public GameStartEvent(ArenaBorderAddon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameStart(RoundStartEvent e) {
        Arena arena = e.getArena();
        RegenerationType regenerationType = arena.getRegenerationType();
        ArrayList<String> disabledArenas = new ArrayList<>();

        for(String s : plugin.config.getStringList("Disabled-Arenas")) {
            disabledArenas.add(s);
        }

        for(String s : disabledArenas) {
            if(!arena.getName().equals(s)) {
                if(regenerationType.equals(RegenerationType.WORLD)) {
                    World world = arena.getGameWorld();

                    double centerLocationX = plugin.config
                            .getDouble("Border.Arenas."
                                    + arena.getName() + ".Center-Location.X");
                    double centerLocationZ = plugin.config
                            .getDouble("Border.Arenas."
                                    + arena.getName() + ".Center-Location.Z");
                    double borderSize = plugin.config
                            .getDouble("Border.Arenas."
                                    + arena.getName() + ".Size");
                    double defaultCenterLocationX = plugin.config
                            .getDouble("Border.Default.Center-Location.X");
                    double defaultCenterLocationZ = plugin.config
                            .getDouble("Border.Default.Center-Location.Z");
                    double defaultBorderSize = plugin.config
                            .getDouble("Border.Default.Size");

                    if(plugin.config.getSection("Border.Arenas."
                            + arena.getName()) != null) {
                        world.getWorldBorder().setCenter(centerLocationX, centerLocationZ);
                        world.getWorldBorder().setSize(borderSize);
                    }
                    else {
                        world.getWorldBorder().setCenter(defaultCenterLocationX,
                                defaultCenterLocationZ);
                        world.getWorldBorder().setSize(defaultBorderSize);
                    }
                }
                else return;
            }
            else return;
        }
        return;
    }
}
