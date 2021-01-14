package me.lystx.tagapi.listener;

import me.lystx.tagapi.NametagAPI;
import me.lystx.tagapi.manager.ConfigService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ConfigService cs = NametagAPI.getInstance().getConfigService();
        if (cs.isUpdateOnJoin()) {
            if (cs.isUpdateAllPlayers()) {
                NametagAPI.getInstance().updateNameTags();
            } else {
                NametagAPI.getInstance().updateNameTags(player);
            }
        }
    }
}
