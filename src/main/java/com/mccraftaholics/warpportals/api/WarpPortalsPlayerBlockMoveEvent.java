package com.mccraftaholics.warpportals.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * An event that fires every time a player moves from one block to another. This
 * is a light-weight version of the PlayerMoveEvent that fires every coordinate
 * change and when players look around.
 * 
 * @see PlayerEvent, @see PlayerMoveEvent
 * 
 * @author http://pastebin.com/SbzHPZBa
 */
public class WarpPortalsPlayerBlockMoveEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private Location from;
	private Location to;

	private boolean cancel;

	public WarpPortalsPlayerBlockMoveEvent(Player who, Location from, Location to) {
		super(who);

		this.from = from;
		this.to = to;
	}

	/**
	 * Get the block-course location that the player was previously at.
	 * 
	 * @return Location
	 */
	public Location getFrom() {
		return from;
	}

	/**
	 * Get the block-course location that the player is now on.
	 * 
	 * @return Location
	 */
	public Location getTo() {
		return to;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
