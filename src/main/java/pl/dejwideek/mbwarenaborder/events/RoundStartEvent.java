package pl.dejwideek.mbwarenaborder.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.RegenerationType;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.dejwideek.mbwarenaborder.ArenaBorderAddon;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RoundStartEvent implements Listener {

    private final ArenaBorderAddon plugin;

    public RoundStartEvent(ArenaBorderAddon plg) {
        plugin = plg;
    }

    @EventHandler
    public void onGameStart(de.marcely.bedwars.api.event.arena.RoundStartEvent e) {
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

                    if(plugin.config.getSection("Border.Arenas."
                            + arena.getName()) != null) {
                        world.getWorldBorder().setCenter(
                                plugin.config.getDouble("Border.Arenas."
                                        + arena.getName() + ".Center-Location.X"),
                                plugin.config.getDouble("Border.Arenas."
                                        + arena.getName() + ".Center-Location.Z"));
                        world.getWorldBorder().setSize(
                                plugin.config.getDouble("Border.Arenas."
                                        + arena.getName() + ".Size"));
                    }
                    else {
                        world.getWorldBorder().setCenter(
                                plugin.config.getDouble("Border.Default.Center-Location.X"),
                                plugin.config.getDouble("Border.Default.Center-Location.Z"));
                        world.getWorldBorder().setSize(
                                plugin.config.getDouble("Border.Default.Size"));
                    }
                }
                else return;
            }
            else return;
        }
        return;
    }
}
