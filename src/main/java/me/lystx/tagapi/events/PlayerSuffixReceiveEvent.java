package me.lystx.tagapi.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class PlayerSuffixReceiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    private final String suffix;


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
