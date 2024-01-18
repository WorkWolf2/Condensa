package me.workwolf.condensa.Utils.Functions;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {



    public int itemCount(Player player, Material material) {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack is : inv.all(material).values()) {
            if (is != null && is.getType() == material) {
                count = count + is.getAmount();
            }
        }
        return count;
    }

    public String translate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }


}
