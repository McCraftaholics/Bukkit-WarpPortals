package com.mccraftaholics.warpportals.manager;

import java.util.HashMap;

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

	public HashMap<String, PortalCreate> mPlayerPortalCreateMap = new HashMap<String, PortalCreate>();
	public HashMap<String, PortalTool> mPlayerPortalToolMap = new HashMap<String, PortalTool>();

	public PortalToolManager(PortalManager pm, YamlConfiguration portalConfig) {
		mPM = pm;

		mPortalConfig = portalConfig;
		mCC = ChatColor.getByChar(mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR));
	}

	public void addCreating(String playerName, PortalCreate portalCreate) {
		mPlayerPortalCreateMap.put(playerName, portalCreate);
	}
	
	public void removeCreating(String playerName) {
		mPlayerPortalCreateMap.remove(playerName);
	}

	public void addTool(String playerName, PortalTool tool) {
		mPlayerPortalToolMap.put(playerName, tool);
	}
	
	public void removeTool(String playerName) {
		mPlayerPortalToolMap.remove(playerName);
	}

	public void playerItemRightClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		PortalCreate portalCreate = mPlayerPortalCreateMap.get(player.getName());
		if (portalCreate != null && portalCreate.toolType == player.getItemInHand().getType()) {
			mPM.mPortalCDManager.possibleCreatePortal(e.getClickedBlock(), player, portalCreate);
		} else {
			PortalTool tool = mPlayerPortalToolMap.get(player.getName());
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
		String portalName = mPM.getPortalName(new Coords(e.getClickedBlock()));
		if (portalName != null) {
			Player player = e.getPlayer();
			player.sendMessage(mCC + portalName);
			
			// Remove tool from player
			mPlayerPortalToolMap.remove(player.getName());
		}
	}
}
