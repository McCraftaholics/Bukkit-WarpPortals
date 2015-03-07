package com.mccraftaholics.warpportals.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.PortalTool;

public class CmdPortalDelTool extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-portal-deltool", "wppdt", "pdeltool" };
	private static final String PERMISSION = "warpportals.admin.portal.delete.tool";
	private static final boolean REQUIRES_PLAYER = true;

	@Override
	public String getPermission() {
		return PERMISSION;
	}

	@Override
	public String[] getAliases() {
		return ALIASES;
	}

	@Override
	public boolean doesRequirePlayer() {
		return REQUIRES_PLAYER;
	}

	@Override
	boolean command(Player player, String[] args, CommandHandler main) {
		try {
			ItemStack curItem = player.getItemInHand();
			if (!curItem.getType().isBlock()) {
				PortalTool tool = new PortalTool(curItem.getType(), PortalTool.Action.DELETE);
				main.mPortalManager.addTool(player.getUniqueId(), tool);
				player.sendMessage(main.mCC + "Portal delete tool equipped to \"" + curItem.getType() + "\"");
			} else
				player.sendMessage(main.mCC + "You can't use a block for that! Try using something like the fishing rod.");
		} catch (Exception e) {
			player.sendMessage(main.mCC + "Error deleting Portal");
		}
		return true;
	}
}
