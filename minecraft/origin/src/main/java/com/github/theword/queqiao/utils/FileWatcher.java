package com.github.theword.queqiao.utils;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.github.theword.queqiao.tool.utils.Tool.logger;


/**
 * 监控文件变化
 *
 * @author YL
 **/
@RequiredArgsConstructor
public class FileWatcher {
    private final LineProcessor lineProcessor;

    /**
     * 文件监听
     *
     * @param filePath 文件路径
     */
    public static void fileListen(String filePath, LineProcessor lineProcessor) {
        Path path = Paths.get(filePath);
        // 监听
        new Thread(() -> {
            try {
                new FileWatcher(lineProcessor).watcherLog(path);
            } catch (Exception e) {
                logger.error("监听日志时出现异常", e);
            }
        }).start();

        // 刷新日志
        new Thread(() -> {
            try {
                while (true) {
                    FileWriter writer = new FileWriter(path.toFile(), true);
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("写日志时出现异常", e);
            }
        }).start();
    }

    /**
     * 文件监控
     * 同步调用会阻塞
     *
     * @param filePath String
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    public void watcherLog(Path filePath) throws IOException, InterruptedException {
        String fileName = filePath.getFileName().toString();
        Path parentPath = filePath.getParent();
        WatchService watchService = FileSystems.getDefault().newWatchService();

        parentPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        AtomicLong lastPointer = new AtomicLong(0L);
        do {
            WatchKey key = watchService.take();
            List<WatchEvent<?>> watchEvents = key.pollEvents();
            watchEvents.stream().filter(i -> StandardWatchEventKinds.ENTRY_MODIFY == i.kind() && fileName.equals(((Path) i.context()).getFileName().toString())).forEach(i -> {
                if (i.count() > 1) {
                    return;
                }

                File configFile = Paths.get(parentPath + "/" + i.context()).toFile();
                StringBuilder str = new StringBuilder();
                lastPointer.set(getFileContent(configFile, lastPointer.get(), str));

                String line = str.toString();

                if (!line.isEmpty() && line.length() < 320) {
                    lineProcessor.processLine(line);
                }
            });
            key.reset();
        } while (true);
    }

    /**
     * beginPointer > configFile 时会从头读取
     *
     * @param configFile   配置文件
     * @param beginPointer 起点
     * @param str          内容会拼接进去
     * @return 读到了多少字节, -1 读取失败
     */
    private long getFileContent(File configFile, long beginPointer, StringBuilder str) {
        if (beginPointer < 0) {
            beginPointer = 0;
        }
        RandomAccessFile file = null;
        boolean top = true;
        try {
            file = new RandomAccessFile(configFile, "r");
            if (beginPointer > file.length()) {
                return 0;
            }
            file.seek(beginPointer);
            String line;
            while ((line = file.readLine()) != null) {
                if (top) {
                    top = false;
                } else {
                    str.append("\n");
                }
                str.append(new String(line.getBytes(StandardCharsets.ISO_8859_1), "GBK"));
            }
            return file.getFilePointer();
        } catch (IOException e) {
            logger.error("Error reading file content", e);
            return -1;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    logger.error("Error closing file", e);
                }
            }
        }
    }
}