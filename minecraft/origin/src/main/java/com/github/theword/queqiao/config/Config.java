package com.github.theword.queqiao.config;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.github.theword.queqiao.tool.utils.Tool.logger;

@Data
public class Config {
    private String logPath;
    private Set<RegexConfig> chatRegexSet;
    private Set<RegexConfig> joinRegexSet;
    private Set<RegexConfig> quitRegexSet;

    public Config() {
        readConfigFile("./config", "regex.yml");
    }


    private void readConfigFile(String configFolder, String fileName) {
        Path configPath = Paths.get("./" + configFolder, BaseConstant.MODULE_NAME, fileName);
        checkFileExists(configPath, fileName);
        readConfigValues(configPath, fileName);
    }

    private void checkFileExists(Path path, String fileName) {
        logger.info("正在寻找置文件 {}...", fileName);
        logger.info("配置文件 {} 路径为：{}。", fileName, path.toAbsolutePath());
        if (Files.exists(path)) {
            logger.info("配置文件 {} 已存在，将读取配置项。", fileName);
        } else {
            logger.warn("配置文件 {} 不存在，将生成默认配置文件。", fileName);
            try {
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
                assert inputStream != null;
                FileUtils.copyInputStreamToFile(inputStream, path.toFile());
                logger.info("已生成默认配置文件 {}。", fileName);
            } catch (IOException var9) {
                logger.warn("生成配置文件 {} 失败。", fileName);
            }
        }
    }

    private void readConfigValues(Path path, String fileName) {
        logger.info("正在读取配置文件 {}...", fileName);
        try {
            Yaml yaml = new Yaml();
            Reader reader = Files.newBufferedReader(path);
            Map<String, Object> configMap = yaml.load(reader);
            loadConfigValues(configMap);
            logger.info("读取配置文件 {} 成功。", fileName);
        } catch (Exception exception) {
            logger.warn("读取配置文件 {} 失败。", fileName);
            logger.warn(exception.getMessage());
            logger.warn("将直接使用默认配置项。");
        }
    }

    private void loadConfigValues(Map<String, Object> configMap) {
        logPath = (String) configMap.get("log_path");
        chatRegexSet = convertToRegexConfigSet(configMap, "chat_regex");
        joinRegexSet = convertToRegexConfigSet(configMap, "join_regex");
        quitRegexSet = convertToRegexConfigSet(configMap, "quit_regex");
    }

    private Set<RegexConfig> convertToRegexConfigSet(Map<String, Object> configMap, String key) {
        Object value = configMap.get(key);
        if (!(value instanceof List<?>)) {
            logger.warn("配置项 {} 不是列表类型，将使用空集合。", key);
            return new HashSet<>();
        }

        Set<RegexConfig> regexConfigs = new HashSet<>();
        for (Object item : (List<?>) value) {
            if (item instanceof Map<?, ?>) {
                Map<?, ?> itemMap = (Map<?, ?>) item;
                String regex = (String) itemMap.get("regex");
                Integer playerGroup = (Integer) itemMap.get("player_group");
                Integer messageGroup = (Integer) itemMap.getOrDefault("message_group", null);
                regexConfigs.add(new RegexConfig(regex, playerGroup, messageGroup));
            } else {
                logger.warn("配置项 {} 中包含非Map类型，将跳过该值。", key);
            }
        }
        return regexConfigs;
    }
}
