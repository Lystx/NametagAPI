package me.lystx.tagapi.listener;

import me.lystx.tagapi.NametagAPI;
import me.lystx.tagapi.manager.ConfigService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChangeListener implements Listener {


    @EventHandler
    public void onJoin(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        ConfigService cs = NametagAPI.getInstance().getConfigService();
        if (cs.isUpdateOnWorldChange()) {
            if (cs.isUpdateAllPlayers()) {
                NametagAPI.getInstance().updateNameTags();
            } else {
                NametagAPI.getInstance().updateNameTags(player);
            }
        }
    }
}
