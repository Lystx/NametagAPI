package me.lystx.tagapi.manager;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.lystx.tagapi.NametagAPI;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.*;

@Getter
public class NametagService {

    private final ConfigService configService;
    private final PlayerService playerService;
    private final Map<Player, String> prefixcache;
    private final Map<Player, Integer> prioritycache;
    private final Map<Player, String> suffixcache;
    private final List<Player> loaded;

    public NametagService(ConfigService configService, PlayerService playerService) {
        this.configService = configService;
        this.playerService = playerService;

        this.prefixcache = new HashMap<>();
        this.prioritycache = new HashMap<>();
        this.suffixcache = new HashMap<>();
        this.loaded = new LinkedList<>();
    }




    public void setTabStyle(String prefix, String suffix, Integer priority, Player player, List<Player> players) {
        this.clearTabStyle(player, priority, players);
        this.loaded.add(player);
        String team_name = priority + player.getName();
        if (team_name.length() > 16)
            team_name = team_name.substring(0, 16);
        if (suffix.length() > 16)
            suffix = suffix.substring(0, 16);
        if (prefix.length() > 16)
            prefix = prefix.substring(0, 16);
        try {
            this.setPlayerListName(player, prefix + player.getName() + suffix, players);
            this.prefixcache.put(player, prefix);
            this.suffixcache.put(player, suffix);
            this.prioritycache.put(player, priority);
            Constructor<?> constructor = NametagAPI.getInstance().getReflections().getNMSClass("PacketPlayOutScoreboardTeam").getConstructor();
            Object packet = constructor.newInstance();
            List<String> contents = new ArrayList<>();
            contents.add(player.getName());
            try {
                NametagAPI.getInstance().getReflections().setField(packet, "a", team_name);
                NametagAPI.getInstance().getReflections().setField(packet, "b", team_name);
                NametagAPI.getInstance().getReflections().setField(packet, "c", prefix);
                NametagAPI.getInstance().getReflections().setField(packet, "d", suffix);
                NametagAPI.getInstance().getReflections().setField(packet, "e", "ALWAYS");
                NametagAPI.getInstance().getReflections().setField(packet, "h", 0);
                NametagAPI.getInstance().getReflections().setField(packet, "g", contents);
            } catch (Exception ex) {
                NametagAPI.getInstance().getReflections().setField(packet, "a", team_name);
                NametagAPI.getInstance().getReflections().setField(packet, "b", team_name);
                NametagAPI.getInstance().getReflections().setField(packet, "c", prefix);
                NametagAPI.getInstance().getReflections().setField(packet, "d", suffix);
                NametagAPI.getInstance().getReflections().setField(packet, "e", "ALWAYS");
                NametagAPI.getInstance().getReflections().setField(packet, "i", 0);
                NametagAPI.getInstance().getReflections().setField(packet, "h", contents);
            }
            if (players == null) {
                for (Player t : Bukkit.getOnlinePlayers())
                    NametagAPI.getInstance().getReflections().sendPacket(t, packet);
            } else {
                for (Player p : players)
                    NametagAPI.getInstance().getReflections().sendPacket(p, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayerListName(Player player, String name, List<Player> ps) {
        if (ps == null) {
            ps = Lists.newLinkedList();
            ps.addAll(Bukkit.getOnlinePlayers());
        }
        CraftPlayer cp = (CraftPlayer)player;
        (cp.getHandle()).listName = name.equals(player.getName()) ? null : CraftChatMessage.fromString(name)[0];
        for (Player p : ps) {
            EntityPlayer ep = ((CraftPlayer)p).getHandle();
            if (ep.getBukkitEntity().canSee(player))
                ep.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, cp.getHandle()));
        }
    }

    private void clearTabStyle(Player player, Integer priority, List<Player> players) {
        try {
            String team_name = priority + player.getName();
            if (team_name.length() > 16)
                team_name = team_name.substring(0, 16);
            Constructor<?> constructor = NametagAPI.getInstance().getReflections().getNMSClass("PacketPlayOutScoreboardTeam").getConstructor();
            Object packet = constructor.newInstance();
            List<String> contents = new ArrayList<>();
            contents.add(priority + player.getName());
            try {
                NametagAPI.getInstance().getReflections().setField(packet, "a", team_name);
                NametagAPI.getInstance().getReflections().setField(packet, "b", team_name);
                NametagAPI.getInstance().getReflections().setField(packet, "e", "ALWAYS");
                NametagAPI.getInstance().getReflections().setField(packet, "h", 1);
                NametagAPI.getInstance().getReflections().setField(packet, "g", contents);
            } catch (Exception ex) {
                NametagAPI.getInstance().getReflections().setField(packet, "a", team_name);
                NametagAPI.getInstance().getReflections().setField(packet, "b", team_name);
                NametagAPI.getInstance().getReflections().setField(packet, "e", "ALWAYS");
                NametagAPI.getInstance().getReflections().setField(packet, "i", 1);
                NametagAPI.getInstance().getReflections().setField(packet, "h", contents);
            }
            if (players == null) {
                for (Player t : Bukkit.getOnlinePlayers())
                    NametagAPI.getInstance().getReflections().sendPacket(t, packet);
            } else {
                for (Player p : players)
                    NametagAPI.getInstance().getReflections().sendPacket(p, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
