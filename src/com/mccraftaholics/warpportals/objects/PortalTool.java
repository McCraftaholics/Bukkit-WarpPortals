package com.mccraftaholics.warpportals.objects;

import org.bukkit.Material;

public class PortalTool {
	public Material toolType;
	public Action action;
	
	public PortalTool(Material toolType, Action action) {
		this.toolType = toolType;
		this.action = action;
	}
	
	public static enum Action {
		DELETE, IDENTIFY;
	}
}
