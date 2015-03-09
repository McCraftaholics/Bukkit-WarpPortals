package com.mccraftaholics.warpportals.manager;

import java.util.HashMap;
import java.util.UUID;

import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.PortalCreate;
import com.mccraftaholics.warpportals.objects.PortalTool;

public class PortalToolManager {

	PortalManager mPM;

	YamlConfiguration mPortalConfig;
	ChatColor mCC;

	public HashMap<UUID, PortalCreate> mPlayerPortalCreateMap = new HashMap<UUID, PortalCreate>();
	public HashMap<UUID, PortalTool> mPlayerPortalToolMap = new HashMap<UUID, PortalTool>();

	public PortalToolManager(PortalManager pm, YamlConfiguration portalConfig) {
		mPM = pm;

		mPortalConfig = portalConfig;
		mCC = ChatColor.getByChar(mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR));
	}

	public void addCreating(UUID playerUUID, PortalCreate portalCreate) {
		mPlayerPortalCreateMap.put(playerUUID, portalCreate);
	}

	public void removeCreating(UUID playerUUID) {
		mPlayerPortalCreateMap.remove(playerUUID);
	}

	public void addTool(UUID playerUUID, PortalTool tool) {
		mPlayerPortalToolMap.put(playerUUID, tool);
	}

	public void removeTool(UUID playerUUID) {
		mPlayerPortalToolMap.remove(playerUUID);
	}
	
	public PortalTool getTool(UUID playerUUID) {
		return mPlayerPortalToolMap.get(playerUUID);
	}

	public void playerItemRightClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		PortalCreate portalCreate = mPlayerPortalCreateMap.get(player.getUniqueId());
		if (portalCreate != null && portalCreate.toolType == player.getItemInHand().getType()) {
			mPM.mPortalCDManager.possibleCreatePortal(e.getClickedBlock(), player, portalCreate);
		} else {
			PortalTool tool = mPlayerPortalToolMap.get(player.getUniqueId());
			if (tool != null && tool.toolType == player.getItemInHand().getType()) {
				if (tool.action == PortalTool.Action.DELETE) {
					mPM.mPortalCDManager.possibleDeletePortal(e.getClickedBlock(), player);
				} else if (tool.action == PortalTool.Action.IDENTIFY) {
					identifyPortal(e);
				}
			}
		}
	}

	private void identifyPortal(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		PortalInfo portal = mPM.getPortal(new Coords(e.getClickedBlock()));
		if (portal != null) {
			player.sendMessage(mCC + "\"" + portal.name + "\" = " + portal.uuid);
		} else {
			player.sendMessage(mCC + "That is not a WarpPortal.");
		}
	}
}
