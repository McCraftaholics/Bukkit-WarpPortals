package com.mccraftaholics.warpportals.bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.mccraftaholics.warpportals.commands.CmdBackup;
import com.mccraftaholics.warpportals.commands.CmdHelp;
import com.mccraftaholics.warpportals.commands.CmdLoad;
import com.mccraftaholics.warpportals.commands.CmdLocationCreate;
import com.mccraftaholics.warpportals.commands.CmdLocationDelete;
import com.mccraftaholics.warpportals.commands.CmdLocationList;
import com.mccraftaholics.warpportals.commands.CmdLocationTeleport;
import com.mccraftaholics.warpportals.commands.CmdPortalCreate;
import com.mccraftaholics.warpportals.commands.CmdPortalDelTool;
import com.mccraftaholics.warpportals.commands.CmdPortalDelete;
import com.mccraftaholics.warpportals.commands.CmdPortalList;
import com.mccraftaholics.warpportals.commands.CmdPortalTeleport;
import com.mccraftaholics.warpportals.commands.CmdSave;
import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.manager.PortalManager;

public class CommandHandler {
	public PortalPlugin mPortalPlugin;
	public PortalManager mPortalManager;
	public YamlConfiguration mPortalConfig;
	public ChatColor mCC;

	public CommandHandler(PortalPlugin mainPlugin, PortalManager portalManager, YamlConfiguration portalConfig) {
		mPortalPlugin = mainPlugin;
		mPortalManager = portalManager;
		mPortalConfig = portalConfig;

		mCC = ChatColor.getByChar(mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR));
	}

	public boolean handleCommand(CommandSender sender, Command command, String label, String[] args) {
		// Wrap the command argument for easy filtering
		WPCommand cmd = new WPCommand(command);

		Player player = sender instanceof Player ? (Player) sender : null;

		// For commands that require the sender to be a player
		if (cmd.test("wp-portal-create", "wppc", "pcreate")) {
			return requirePlayer(sender, args, this, CmdPortalCreate.class);
		} else if (cmd.test("wp-portal-deltool", "wppdt", "pdeltool")) {
			return requirePlayer(sender, args, this, CmdPortalDelTool.class);
		} else if (cmd.test("wp-portal-teleport", "wp-portal-tp", "wpptp")) {
			return requirePlayer(sender, args, this, CmdPortalTeleport.class);
		} else if (cmd.test("wp-location-create", "wplc")) {
			return requirePlayer(sender, args, this, CmdLocationCreate.class);
		} else if (cmd.test("wp-location-teleport", "wp-location-tp", "wpltp")) {
			return requirePlayer(sender, args, this, CmdLocationTeleport.class);
		}
		// For commands that a server or player can run
		else if (cmd.test("wp", "wp-help", "phelp"))
			return CmdHelp.handle(sender, args, this);
		else if (cmd.test("wp-portal-delete", "wppd", "pdelete"))
			return CmdPortalDelete.handle(sender, args, this);
		else if (cmd.test("wp-portal-list", "wppl", "plist"))
			return CmdPortalList.handle(sender, args, this);
		else if (cmd.test("wp-location-delete", "wpld", "pdestdel"))
			return CmdLocationDelete.handle(sender, args, this);
		else if (cmd.test("wp-location-list", "wpll", "pdestlist"))
			return CmdLocationList.handle(sender, args, this);
		else if (cmd.test("wp-save", "wps", "psave"))
			return CmdSave.handle(sender, args, this);
		else if (cmd.test("wp-load", "wpl", "pload"))
			return CmdLoad.handle(sender, args, this);
		else if (cmd.test("wp-backup", "wpb", "pbackup"))
			return CmdBackup.handle(sender, args, this);

		return false;
	}

	public static class CommandHandlerObject {
		public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
			return false;
		}
	}

	boolean requirePlayer(CommandSender sender, String[] args, CommandHandler main, Class<?> cho) {
		if (sender instanceof Player) {
			sender.sendMessage("This command must be run from an active player");
			return true;
		} else {
			try {
				Method m = cho.getMethod("handle", CommandSender.class, String[].class, CommandHandler.class);
				return (Boolean) m.invoke(null, sender, args, main);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	static class WPCommand {
		String command;

		WPCommand(Command bukkitCommand) {
			command = bukkitCommand.getName().toLowerCase();
		}

		boolean test(String... strings) {
			for (String s : strings) {
				if (command.equals(s))
					return true;
			}
			return false;
		}
	}
}
