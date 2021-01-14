package me.lystx.tagapi.manager;

import lombok.Getter;

@Getter
public class PlayerService {

    private final ConfigService configService;

    public PlayerService(ConfigService configService) {
        this.configService = configService;
    }
}
