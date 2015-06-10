package com.mccraftaholics.warpportals.bukkit;

import com.mccraftaholics.warpportals.api.example.WarpPortalsEventListener;
import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.manager.PortalManager;
import com.mccraftaholics.warpportals.remote.RemoteManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PortalPlugin extends JavaPlugin {

    public static final String VERSION = "${project.version}";
    public static final int VERSION_NUMBER = Integer.parseInt("${project.properties.versionNumber}");

    public PortalManager mPortalManager;
    public File mPortalDataFile;
    public YamlConfiguration mPortalConfig;
    public RemoteManager mRemoteManager;
    CommandHandler mCommandHandler;
    File mPortalConfigFile;

    @Override
    public void onEnable() {
        mPortalConfigFile = new File(getDataFolder(), "config.yml");
        mPortalDataFile = new File(getDataFolder(), "portals.yml");
        mPortalConfig = new YamlConfiguration();
        initiateConfigFiles();
        loadConfigs();
        mRemoteManager = new RemoteManager(this, mPortalConfig.getBoolean("portals.reporting.allowAnalytics", true));
        mPortalManager = new PortalManager(getLogger(), mPortalConfig, mPortalDataFile, mRemoteManager.reportManager, this);
        mCommandHandler = new CommandHandler(this, mPortalManager, mPortalConfig);
        getServer().getPluginManager().registerEvents(new BukkitEventListener(this, mPortalManager, mPortalConfig), this);
        initMCStats();

        // Initialize remote manager unless user requested not to
        if (mRemoteManager.isEnabled) {
            mRemoteManager.initialize();
        }

        // Register example WarpPortals Event API Listener
        String tpMessage = mPortalConfig.getString("portals.teleport.message", Defaults.TP_MESSAGE);
        ChatColor tpChatColor = ChatColor.getByChar(mPortalConfig.getString("portals.teleport.messageColor", Defaults.TP_MSG_COLOR));
        getServer().getPluginManager().registerEvents(new WarpPortalsEventListener(tpMessage, tpChatColor), this);
    }

    private void initMCStats() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    private void initiateConfigFiles() {
        // Initiate portal config file
        if (!mPortalConfigFile.exists()) {
            mPortalConfigFile.getParentFile().mkdirs();
            try {
                mPortalConfigFile.createNewFile();
                Utils.copy(getResource("config.yml"), mPortalConfigFile);
            } catch (IOException e) {
                getLogger().severe("Error creating the default Portal config file!");
                e.printStackTrace();
            }
        }
        // Initiate portal data file
        if (!mPortalDataFile.exists()) {
            mPortalDataFile.getParentFile().mkdirs();
            try {
                mPortalDataFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Error creating Portal's save file!");
                e.printStackTrace();
            }
        }
    }

    private void loadConfigs() {
        try {
            mPortalConfig.load(mPortalConfigFile);
        } catch (InvalidConfigurationException e) {
            getLogger().severe("The WarpPortal config file has invalid markup.");
        } catch (FileNotFoundException e) {
            getLogger().severe("No config file found for WarpPortals!");
        } catch (IOException e) {
            getLogger().severe("Can't load Portal's config file!");
            e.printStackTrace();
        }
    }

    private void saveConfigs() {
        // Why save the config? That's for the user to edit.
        /*
         * try { mPortalConfig.save(mPortalConfigFile); } catch (IOException e)
		 * { getLogger().severe("Can't save Portal's config file!"); }
		 */
    }

    @Override
    public void onDisable() {
        mPortalManager.onDisable();
        if (mRemoteManager != null)
            mRemoteManager.shutdown();
        saveConfigs();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return mCommandHandler.handleCommand(sender, command, label, args);
    }
}
