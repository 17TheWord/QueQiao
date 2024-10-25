package com.github.theword.queqiao.config;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class RegexConfig {
    Pattern pattern;
    int playerGroup;
    Integer messageGroup;

    public RegexConfig(String regex, int playerGroup, Integer messageGroup) {
        this.pattern = Pattern.compile(regex);
        this.playerGroup = playerGroup;
        this.messageGroup = messageGroup;
    }
}
