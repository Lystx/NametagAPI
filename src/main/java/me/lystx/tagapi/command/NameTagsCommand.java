package me.lystx.tagapi.command;

import java.io.IOException;

import me.lystx.tagapi.NametagAPI;
import me.lystx.tagapi.utils.NametagGroup;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class NameTagsCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (player.hasPermission("nte.command")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("rl")) {
                        NametagAPI.getInstance().getConfigService().load();
                        NametagAPI.getInstance().getConfigService().save();
                        player.sendMessage(NametagAPI.getInstance().getPrefix() + "§7The plugin was §ereloaded§8!");
                    } else if (args[0].equalsIgnoreCase("listGroup")) {
                        if (NametagAPI.getInstance().getConfigService().getNametagGroups().isEmpty()) {
                            player.sendMessage(NametagAPI.getInstance().getPrefix() + "§cThere aren't any groups at the moment!");
                            return false;
                        }
                        player.sendMessage("§eNametagGroups §7Help§8:");
                        player.sendMessage("§8§m------------------");
                        for (NametagGroup nametagGroup : NametagAPI.getInstance().getConfigService().getNametagGroups()) {
                            player.sendMessage("§8» §e" + nametagGroup.getName() + " §8┃ §7Prefix§8: §7" + nametagGroup.getPrefix() + "§8, §7Suffix§8: §7" + nametagGroup.getSuffix() + "§8, §7Priority §8: §7" + nametagGroup.getId());
                        }
                        player.sendMessage("§8§m------------------");
                    } else {
                        this.help(player);
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("removeGroup")) {
                        String group = args[1];
                        NametagGroup nametagGroup = NametagAPI.getInstance().getConfigService().getGroup(group);
                        if (nametagGroup == null) {
                            player.sendMessage(NametagAPI.getInstance().getPrefix() + "§cThe group §e" + group + " §cdoesn't exists!");
                            return false;
                        }
                        NametagAPI.getInstance().getConfigService().getNametagGroups().remove(nametagGroup);
                        NametagAPI.getInstance().getConfigService().save();
                        NametagAPI.getInstance().getConfigService().load();
                        player.sendMessage(NametagAPI.getInstance().getPrefix() + "§7The group §e" + group + " §7was removed§8!");
                    } else {
                        this.help(player);
                    }
                } else if (args.length == 5) {
                    if (args[0].equalsIgnoreCase("addgroup")) {
                        String group = args[1];
                        NametagGroup nametagGroup = NametagAPI.getInstance().getConfigService().getGroup(group);
                        if (nametagGroup != null) {
                            player.sendMessage(NametagAPI.getInstance().getPrefix() + "§cThe group §e" + nametagGroup.getName() + " §calready exists!");
                            return false;
                        }
                        String prefix = args[2];
                        String suffix = args[3];
                        Integer priority = Integer.parseInt(args[4]);
                        NametagGroup newGroup = new NametagGroup(group, prefix, suffix, priority, "nametag." + group);
                        NametagAPI.getInstance().getConfigService().getNametagGroups().add(newGroup);
                        NametagAPI.getInstance().getConfigService().save();
                        NametagAPI.getInstance().getConfigService().load();
                        player.sendMessage(NametagAPI.getInstance().getPrefix() + "§7The group §e" + group + " §7was created§8!");
                    } else {
                        this.help(player);
                    }
                } else {
                    this.help(player);
                }
            } else {
                player.sendMessage(NametagAPI.getInstance().getPrefix() + "§cYou aren't allowed to perform this command!");
            }
        }
        return false;
    }

    public void help(Player player) {
        player.sendMessage("§eNametagsCommand §7Help§8:");
        player.sendMessage("§8§m------------------");
        player.sendMessage("§8» §e/nte rl §8┃ §7Reloads the plugins");
        player.sendMessage("§8» §e/nte addGroup <name> <prefix> <suffix> <priority> §8┃ §7Creates a group");
        player.sendMessage("§8» §e/nte removeGroup <name> §8┃ §7Removes a group");
        player.sendMessage("§8» §e/nte listGroup §8┃ §7Lists all groups");
        player.sendMessage("§8§m------------------");
    }

}
