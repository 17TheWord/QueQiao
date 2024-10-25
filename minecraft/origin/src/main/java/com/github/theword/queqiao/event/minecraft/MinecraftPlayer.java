package com.github.theword.queqiao.event.minecraft;

import com.github.theword.queqiao.tool.event.base.BasePlayer;
import lombok.Getter;


@Getter
public class MinecraftPlayer extends BasePlayer {
    public MinecraftPlayer(String nickname) {
        super(nickname, null);
    }
}
