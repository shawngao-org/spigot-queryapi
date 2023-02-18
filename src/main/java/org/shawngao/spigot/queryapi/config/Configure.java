package org.shawngao.spigot.queryapi.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.shawngao.spigot.queryapi.QueryApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class Configure {

    private static final String CONFIG_FILE = "config.yml";

    private String channelName;
    private String subServerName;
    private final Plugin plugin = QueryApi.instance;

    public void createConfig() throws IOException {
        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Created config folder: " + plugin.getDataFolder().mkdir());
        }
        File config = new File(plugin.getDataFolder(), CONFIG_FILE);
        if (!config.exists()) {
            FileOutputStream fileOutputStream = new FileOutputStream(config);
            InputStream inputStream = plugin.getResource(CONFIG_FILE);
            inputStream.transferTo(fileOutputStream);
        }
    }

    public void loadConfigure() {
        FileConfiguration fileConfiguration = plugin.getConfig();
        channelName = fileConfiguration.getString("channel");
        subServerName = fileConfiguration.getString("sub-server-name");
    }
}
