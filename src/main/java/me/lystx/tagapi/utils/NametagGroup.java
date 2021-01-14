package me.lystx.tagapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NametagGroup {

    private final String name;
    private final String prefix;
    private final String suffix;
    private final Integer id;
    private final String permission;
}
