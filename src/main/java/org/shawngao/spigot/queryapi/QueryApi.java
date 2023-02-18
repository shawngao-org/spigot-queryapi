package org.shawngao.spigot.queryapi;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.shawngao.spigot.queryapi.config.Configure;

import java.io.IOException;

public final class QueryApi extends JavaPlugin implements PluginMessageListener {

    public static Plugin instance;
    private Configure configure;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        initPlugin();
        if (!this.getServer().spigot().getConfig().getBoolean("settings.bungeecord")) {
            getLogger().warning("This server is not a BungeeCord server.");
            getLogger().warning("Plugin was disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    public void initPlugin() {
        configure = processConfigure();
        getLogger().info("Enabling plugin channels ...");
        this.getServer().getMessenger()
                .registerIncomingPluginChannel(
                        this, configure.getChannelName(), this);
        this.getServer().getMessenger()
                .registerOutgoingPluginChannel(this, configure.getChannelName());
        getLogger().info("Channel " + configure.getChannelName() + " was registered.");
        getLogger().info("Done.");
    }

    public Configure processConfigure() {
        Configure configure1 = new Configure();
        try {
            getLogger().info("Checking configure file ...");
            configure1.createConfig();
            getLogger().info("Done.");
            getLogger().info("Loading configure file ...");
            configure1.loadConfigure();
            getLogger().info("Loaded.");
        } catch (IOException e) {
            getLogger().warning(e.getMessage());
            throw new RuntimeException(e);
        }
        return configure1;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabling this plugin ...");
        getLogger().info("Unregistering plugin channel ...");
        this.getServer().getMessenger()
                        .unregisterIncomingPluginChannel(this);
        this.getServer().getMessenger()
                        .unregisterOutgoingPluginChannel(this);
        getLogger().info("Channel " + configure.getChannelName() + " was unregistered.");
        getLogger().info("Unregistered plugin channel.");
        getLogger().info("Disabled this plugin.");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(configure.getChannelName())) {
            return;
        }
        ByteArrayDataInput arrayDataInput = ByteStreams.newDataInput(message);
        String subChannel = arrayDataInput.readUTF();
        if (subChannel.equals(configure.getSubServerName())) {
            String data1 = arrayDataInput.readUTF();
            int data2 = arrayDataInput.readInt();
            getLogger().info(data1);
            getLogger().info(String.valueOf(data2));
        }
    }
}
