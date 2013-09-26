package com.mccraftaholics.warpportals.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 
 * An Event that fires when a player enters into a portal managed by WarpPortals
 * 
 * @see Event
 * 
 */
public class WarpPortalEnterEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	/**
	 * Constructor of a WorldSniperWandSelectionEvent * * Between pos1 and pos2
	 * is the selection *
	 * 
	 * @param p
	 *            - Player that is selecting Something WIth the wand * @param
	 *            pos1 - The First Position
	 * @param pos2
	 *            - The Second Position
	 */
	public WarpPortalEnterEvent(Player p) {
		this.cancelled = false;
	}

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

}
