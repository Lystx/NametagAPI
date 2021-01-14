package me.lystx.tagapi;

import me.lystx.tagapi.command.NameTagsCommand;
import me.lystx.tagapi.events.PlayerNameTagReceiveEvent;
import me.lystx.tagapi.events.PlayerPrefixReceiveEvent;
import me.lystx.tagapi.events.PlayerSuffixReceiveEvent;
import me.lystx.tagapi.listener.PlayerJoinListener;
import me.lystx.tagapi.listener.PlayerQuitListener;
import me.lystx.tagapi.listener.PlayerWorldChangeListener;
import me.lystx.tagapi.manager.ConfigService;
import me.lystx.tagapi.manager.NametagService;
import me.lystx.tagapi.manager.PlayerService;
import me.lystx.tagapi.utils.NametagGroup;
import me.lystx.tagapi.utils.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

import java.util.List;

@Getter
public class NametagAPI extends JavaPlugin {

    @Getter
    private static NametagAPI instance;

    private Reflections reflections;
    private ConfigService configService;
    private PlayerService playerService;
    private NametagService nametagService;
    private String prefix;

    @Override
    public void onEnable() {
        instance = this;

        this.prefix = "§8» §eNametags §8┃ §7";
        this.reflections = new Reflections();
        this.configService = new ConfigService();
        this.playerService = new PlayerService(this.configService);
        this.nametagService = new NametagService(this.configService, this.playerService);

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerWorldChangeListener(), this);

        this.getCommand("nte").setExecutor(new NameTagsCommand());
    }


    public void updateNameTags() {
        for (Player player : Bukkit.getOnlinePlayers()) { 
            this.updateNameTags(player);
        }
    }

    public void updateNameTags(Player player) {
        for (NametagGroup nametagGroup : this.configService.getNametagGroups()) {
            if (nametagGroup.getPermission() == null ||player.hasPermission(nametagGroup.getPermission())) {
                this.setTag(player, nametagGroup.getPrefix(), nametagGroup.getSuffix(), nametagGroup.getId());
            }
        }
        
    }

    public void setPrefix(Player player, String prefix) {
        if (this.nametagService.getLoaded().contains(player)) {
            setTag(player, prefix, this.nametagService.getSuffixcache().get(player), this.nametagService.getPrioritycache().get(player));
            Bukkit.getPluginManager().callEvent(new PlayerPrefixReceiveEvent(player, prefix));
        }
    }

    public void setPrefix(Player player, String prefix, List<Player> players) {
        if (this.nametagService.getLoaded().contains(player)) {
            setTag(player, prefix, this.nametagService.getSuffixcache().get(player), this.nametagService.getPrioritycache().get(player), players);
            Bukkit.getPluginManager().callEvent((Event)new PlayerPrefixReceiveEvent(player, prefix));
        }
    }

    public void setSuffix(Player player, String suffix) {
        if (this.nametagService.getLoaded().contains(player)) {
            setTag(player, this.nametagService.getPrefixcache().get(player), suffix, this.nametagService.getPrioritycache().get(player));
            Bukkit.getPluginManager().callEvent(new PlayerSuffixReceiveEvent(player, suffix));
        }
    }

    public void setSuffix(Player player, String suffix, List<Player> players) {
        if (this.nametagService.getLoaded().contains(player)) {
            setTag(player, this.nametagService.getPrefixcache().get(player), suffix, this.nametagService.getPrioritycache().get(player), null);
            Bukkit.getPluginManager().callEvent(new PlayerSuffixReceiveEvent(player, suffix));
        }
    }

    public void setTag(Player player, String prefix, String suffix, Integer priority) {
        this.nametagService.setTabStyle(prefix, suffix, priority, player, null);
        Bukkit.getPluginManager().callEvent(new PlayerNameTagReceiveEvent(player, suffix, prefix, priority));
    }

    public void setTag(Player player, String prefix, String suffix, Integer priority, List<Player> players) {
        this.nametagService.setTabStyle(prefix, suffix, priority, player, players);
        Bukkit.getPluginManager().callEvent(new PlayerNameTagReceiveEvent(player, suffix, prefix, priority));
    }

    public void clearCache() {
        this.nametagService.getSuffixcache().clear();
        this.nametagService.getPrefixcache().clear();
        this.nametagService.getPrioritycache().clear();
        this.nametagService.getLoaded().clear();
    }

    public String getSuffix(Player player) {
        String suffix = this.nametagService.getSuffixcache().get(player);
        if (suffix != null)
            return suffix;
        throw new NullPointerException("§cNo suffix cached for player " + player.getName());
    }

    public Integer getPriority(Player player) {
        Integer suffix = this.nametagService.getPrioritycache().get(player);
        if (suffix != null)
            return suffix;
        throw new NullPointerException("§cNo priority cached for player " + player.getName());
    }

    public String getPrefix(Player player) {
        String prefix = this.nametagService.getPrefixcache().get(player);
        if (prefix != null)
            return prefix;
        throw new NullPointerException("§cNo prefix cached for player " + player.getName());
    }

}
