package me.workwolf.condensa.Events;

import me.workwolf.condensa.Condensa;
import me.workwolf.condensa.Utils.Functions.Functions;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlace implements Listener {

    private final Condensa plugin;

    private final Functions functions = new Functions();

    public BlockPlace(Condensa plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        ItemMeta meta = is.getItemMeta();

        assert meta != null;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (pdc.has(new NamespacedKey(plugin, "value"), PersistentDataType.INTEGER)) {
            NamespacedKey key = new NamespacedKey(plugin, "value");

            int value = pdc.get(key, PersistentDataType.INTEGER);

            if (value == -1) {
                event.setCancelled(true);
            }
        }

    }
}
