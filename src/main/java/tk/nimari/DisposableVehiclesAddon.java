package tk.nimari;

import es.pollitoyeye.vehicles.enums.VehicleType;
import es.pollitoyeye.vehicles.events.VehicleExitEvent;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public final class DisposableVehiclesAddon extends JavaPlugin {

    public FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        long currentTime = System.currentTimeMillis();

        new Metrics(this, 17796);

        for (VehicleType vehicleType : VehicleType.values())
            config.addDefault("dispose" + vehicleType.getConfigName(), false);

        config.options().copyDefaults(true);
        saveConfig();
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        getLogger().log(Level.INFO, "Successful startup! Took " + (System.currentTimeMillis() - currentTime) + "ms.");

    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Leaving so soon? Drive safe!");
    }

    public class Listeners implements Listener {

        @EventHandler
        public void noVehiclePickup(VehicleExitEvent e) {
            if (config.getBoolean("dispose" + e.getVehicle().getType().getConfigName()))
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getVehicle().remove();
                    }
                }.runTaskLater(DisposableVehiclesAddon.this, 10);
        }
    }
}
