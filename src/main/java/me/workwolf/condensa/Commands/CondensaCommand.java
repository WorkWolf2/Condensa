package me.workwolf.condensa.Commands;

import me.workwolf.condensa.Utils.Functions.Functions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CondensaCommand implements CommandExecutor {

    private final me.workwolf.condensa.Condensa plugin;

    private Functions functions = new Functions();

    public CondensaCommand(me.workwolf.condensa.Condensa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String itemType = plugin.getConfig().getString("item.type");
        Material material = Material.valueOf(itemType);

        int quantity = plugin.getConfig().getInt("item.quantity") * 64;

        String resultType = plugin.getConfig().getString("result.type");
        Material resultMaterial = Material.valueOf(resultType);

        String resultName = functions.translate(plugin.getConfig().getString("result.name"));

        List<String> lore = new ArrayList<>();
        for (String i : plugin.getConfig().getStringList("result.lore")) {
            lore.add(functions.translate(i));
        }

        if (args.length > 1) return true;

        if (args.length == 1) {
            if (!sender.hasPermission("condensa.condensaother")) return true;

            if (!Objects.requireNonNull(Bukkit.getPlayer(args[0])).isOnline()) {
                sender.sendMessage(functions.translate("&x&f&b&2&0&2&0&lC&x&e&0&1&e&4&0&lO&x&c&6&1&c&5&f&lN&x&a&b&1&a&7&f&lD&x&9&1&1&7&9&e&lE&x&7&6&1&5&b&e&lN&x&5&c&1&3&d&d&lS&x&4&1&1&1&f&d&lA &7|functions.translate(\"&x&f&b&2&0&2&0&lC&x&e&0&1&e&4&0&lO&x&c&6&1&c&5&f&lN&x&a&b&1&a&7&f&lD&x&9&1&1&7&9&e&lE&x&7&6&1&5&b&e&lN&x&5&c&1&3&d&d&lS&x&4&1&1&1&f&d&lA &7 Il giocatore specificato non è online!"));
            }

            Player target = Bukkit.getPlayer(args[0]);

            assert target != null;
            int itemCountOther = functions.itemCount(target, material);

            int amount = itemCountOther / 64 / 9;

            if (itemCountOther >= quantity) {

                final var removeCount = 9 * 64;

                Inventory inventory = target.getInventory();

                while(inventory.contains(material, removeCount)) { // If the inventory has at least 9 * 64 wheat
                    var total = 0;

                    int first = 0;
                    while((first = inventory.first(Material.WHEAT)) != -1 && total < removeCount) {
                        var item = inventory.getItem(first);
                        assert item != null;
                        var toRemove = Math.max(Math.min(item.getAmount(), removeCount - total), 0);
                        var newAmount = item.getAmount() - toRemove;
                        if(newAmount <= 0) {
                            inventory.clear(first);
                        } else {
                            item.setAmount(newAmount);
                        }
                        total += toRemove;
                    }
                }

                ItemStack itemStack = new ItemStack(resultMaterial);

                ItemMeta meta = itemStack.getItemMeta();

                assert meta != null;
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setDisplayName(resultName);

                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                pdc.set(new NamespacedKey(plugin, "value"), PersistentDataType.INTEGER, -1);

                meta.setLore(lore);

                itemStack.setItemMeta(meta);

                if (amount == 2) {
                    for (int i = 1; i<=2; i++) {
                        target.getInventory().addItem(itemStack);
                    }
                } else if (amount == 3) {
                    for (int i = 1; i <= 3; i++) {
                        target.getInventory().addItem(itemStack);
                    }
                } else if (amount == 4){
                    for (int i = 1; i <= 4; i++) {
                        target.getInventory().addItem(itemStack);
                    }
                } else {
                    target.getInventory().addItem(itemStack);
                }

                target.sendMessage(functions.translate("&x&f&b&2&0&2&0&lC&x&e&0&1&e&4&0&lO&x&c&6&1&c&5&f&lN&x&a&b&1&a&7&f&lD&x&9&1&1&7&9&e&lE&x&7&6&1&5&b&e&lN&x&5&c&1&3&d&d&lS&x&4&1&1&1&f&d&lA &7| I tuoi item sono stati condensati!"));

                sender.sendMessage(functions.translate("&x&f&b&2&0&2&0&lC&x&e&0&1&e&4&0&lO&x&c&6&1&c&5&f&lN&x&a&b&1&a&7&f&lD&x&9&1&1&7&9&e&lE&x&7&6&1&5&b&e&lN&x&5&c&1&3&d&d&lS&x&4&1&1&1&f&d&lA &7| Gli item di %target_name% &7sono stati condensati!").replace("%target_name%", target.getDisplayName()));
            } else {
                sender.sendMessage(functions.translate("&x&f&b&2&0&2&0&lC&x&e&0&1&e&4&0&lO&x&c&6&1&c&5&f&lN&x&a&b&1&a&7&f&lD&x&9&1&1&7&9&e&lE&x&7&6&1&5&b&e&lN&x&5&c&1&3&d&d&lS&x&4&1&1&1&f&d&lA &7| %target_name% &7non ha abbastanza item per condensarli!").replace("%target_name%", target.getDisplayName()));
            }
        } else {
            if (!(sender instanceof Player)) return true;

            Player player = (Player) sender;

            if (!player.hasPermission("condensa.condensa")) return true;

            int itemCount = functions.itemCount(player, material);

            final var removeCount = 9 * 64;

            Inventory inventory = player.getInventory();

            if (itemCount >= quantity) {
                while(inventory.contains(material, removeCount)) { // If the inventory has at least 9 * 64 wheat
                    var total = 0;

                    int first = 0;
                    while((first = inventory.first(Material.WHEAT)) != -1 && total < removeCount) {
                        var item = inventory.getItem(first);
                        assert item != null;
                        var toRemove = Math.max(Math.min(item.getAmount(), removeCount - total), 0);
                        var newAmount = item.getAmount() - toRemove;
                        if(newAmount <= 0) {
                            inventory.clear(first);
                        } else {
                            item.setAmount(newAmount);
                        }
                        total += toRemove;
                    }
                }

                ItemStack itemStack = new ItemStack(resultMaterial);

                ItemMeta meta = itemStack.getItemMeta();

                assert meta != null;
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setDisplayName(resultName);

                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                pdc.set(new NamespacedKey(plugin, "value"), PersistentDataType.INTEGER, -1);

                meta.setLore(lore);

                itemStack.setItemMeta(meta);

                int amount = itemCount / 64 / 9;

                if (amount == 2) {
                    for (int i = 1; i<=2; i++) {
                        player.getInventory().addItem(itemStack);
                    }
                } else if (amount == 3) {
                    for (int i = 1; i <= 3; i++) {
                        player.getInventory().addItem(itemStack);
                    }
                } else if (amount == 4){
                    for (int i = 1; i <= 4; i++) {
                        player.getInventory().addItem(itemStack);
                    }
                } else {
                    player.getInventory().addItem(itemStack);
                }

                player.sendMessage(functions.translate("&x&f&b&2&0&2&0&lC&x&e&0&1&e&4&0&lO&x&c&6&1&c&5&f&lN&x&a&b&1&a&7&f&lD&x&9&1&1&7&9&e&lE&x&7&6&1&5&b&e&lN&x&5&c&1&3&d&d&lS&x&4&1&1&1&f&d&lA &7| Hai condensato gli item!"));
            } else {
                player.sendMessage(functions.translate("&x&f&b&2&0&2&0&lC&x&e&0&1&e&4&0&lO&x&c&6&1&c&5&f&lN&x&a&b&1&a&7&f&lD&x&9&1&1&7&9&e&lE&x&7&6&1&5&b&e&lN&x&5&c&1&3&d&d&lS&x&4&1&1&1&f&d&lA &7| Non hai abbastanza item per condensarli!"));
            }
        }




        return true;
    }
}
