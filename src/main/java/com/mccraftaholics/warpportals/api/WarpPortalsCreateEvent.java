package com.mccraftaholics.warpportals.api;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalInfo;

/**
 * 
 * An Event that fires when a WarpPortal is being created.
 * 
 * @see Event
 * 
 */
public class WarpPortalsCreateEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	// Event data
	private CommandSender sender;
	private PortalInfo portal;

	/**
	 * Constructor of a WarpPortalsCreateEvent, triggered when a WarpPortal is
	 * being created.
	 * 
	 * @param s
	 *            - CommandSender that is creating the WarpPortal. Most likely a
	 *            player.
	 * @param po
	 *            - PortalInfo for the to-be-created WarpPortal. Contains the
	 *            WarpPortal's name, blocks, destination coords.
	 */
	public WarpPortalsCreateEvent(CommandSender s, PortalInfo po) {
		this.cancelled = false;

		sender = s;
		portal = po.clone();
	}

	/* Bukkit Event API methods */

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	/* WarpPortal Event API methods */

	/* Get event data */
	/**
	 * Get the CommandSender that is creating the WarpPortal.
	 * 
	 * @return CommandSender associated with the event
	 */
	public CommandSender getSender() {
		return sender;
	}

	/**
	 * Get a copy of the WarpPortal that is being created. PortalInfo contains
	 * portal name, location data and teleportation data.
	 * 
	 * @return PortalInfo - WarpPortal associated with the event
	 */
	public PortalInfo getPortal() {
		return portal;
	}

	/* Set event data */

	/**
	 * Modify the to-be-created WarpPortal's name.
	 * 
	 * @param name
	 *            - String of the new name.
	 */
	public void setName(String name) {
		portal.name = name;
	}

	/**
	 * Modify the to-be-created WarpPortal's destination.
	 * 
	 * @param newTPC
	 *            - CoordsPY of the new destination
	 */
	public void setTeleportCoordsPY(CoordsPY newTPC) {
		portal.tpCoords = newTPC;
	}

	/**
	 * Modify the to-be-created WarpPortal's destination, use the supplied
	 * Location as the new destination. <b>Use of
	 * {@link #setTeleportCoordsPY(CoordsPY)} is preferable!
	 * 
	 * @param newLoc
	 *            - CoordsPY of the new destination
	 */
	public void setTeleportCoordsPY(Location newLoc) {
		setTeleportCoordsPY(new CoordsPY(newLoc));
	}

	/**
	 * Modify the location that the new WarpPortal will be created at.
	 * WarpPortal's uses an ArrayList of {@link Coords} to signify where it
	 * exists. Each {@link Coords} item represents an active block of the
	 * WarpPortal.
	 * 
	 * @param newBlocks
	 *            - ArrayList<Coords> of the new block coords.
	 */
	public void setPortalBlocks(ArrayList<Coords> newBlocks) {
		portal.blockCoordArray = newBlocks;
	}
}
