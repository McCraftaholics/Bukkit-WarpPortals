package com.mccraftaholics.warpportals.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.PortalTool;

public class CmdPortalIDTool extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-portal-idtool", "wppid", "wppidt" };
	private static final String PERMISSION = "warpportals.admin.portal.list.tool";
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
				if (main.mPortalManager.getTool(player.getName()) != null) {
					main.mPortalManager.removeTool(player.getName());
					player.sendMessage(main.mCC + "Portal identify tool dequipped from \"" + curItem.getType() + "\"");
				} else {
					PortalTool tool = new PortalTool(curItem.getType(), PortalTool.Action.IDENTIFY);
					main.mPortalManager.addTool(player.getName(), tool);
					player.sendMessage(main.mCC + "Portal identify tool equipped to \"" + curItem.getType() + "\"");
				}
			} else
				player.sendMessage(main.mCC + "You can't use a block for that! Try using something like the fishing rod.");
		} catch (Exception e) {
			player.sendMessage(main.mCC + "Error identifying Portal");
		}
		return true;
	}
}
