package fr.rudy.homes.commands;

import fr.rudy.homes.HomesPlugin;
import fr.rudy.homes.manager.HomesManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HomeCommand implements CommandExecutor {

    private final HomesPlugin plugin;
    private final HomesManager homesManager;

    public HomeCommand(HomesPlugin plugin) {
        this.plugin = plugin;
        this.homesManager = plugin.getHomesManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("<glyph:error> §cSeuls les joueurs peuvent utiliser cette commande.");
            return true;
        }

        final Player player = (Player) sender;
        final UUID playerUuid = player.getUniqueId();

        if (label.equalsIgnoreCase("home")) {
            Location home = homesManager.getHome(playerUuid);
            if (home == null) {
                player.sendMessage("<glyph:error> §cVous n'avez pas encore défini de home.");
                return true;
            }

            player.teleport(home);
            player.sendMessage("<glyph:info> §bTéléporté à votre home !");
            return true;
        }

        if (label.equalsIgnoreCase("sethome")) {
            boolean success = homesManager.setHome(playerUuid, player.getLocation());
            if (success) {
                player.sendMessage("<glyph:info> §bVotre home a été défini avec succès !");
            } else {
                player.sendMessage("<glyph:error> §cUne erreur est survenue lors de l'enregistrement du home.");
            }
            return true;
        }

        return false;
    }
}
