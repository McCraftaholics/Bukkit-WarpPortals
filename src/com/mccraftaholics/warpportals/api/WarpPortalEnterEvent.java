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

    // Event data
    private Player player;
    private boolean isWarpPortal;

	/**
	 * Constructor of a WarpPortalEnterEvent. Triggered when a player has entered a WarpPortal managed portal.
	 * 
	 * @param p
	 *            - Player that is entering the portal
	 * @param iWP
	 *            - Is the portal a Normal portal or a WarpPortal?
	 */
	public WarpPortalEnterEvent(Player p, boolean iWP) {
		this.cancelled = false;

        player = p;
        isWarpPortal = iWP;
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

    /* WarpPortal API methods */

    /**
     * Get the player that is entering the portal.
     *
     * @return Player associated with the event
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Is the portal a WarpPortal or a normal Ender/Nether portal?
     *
     * @return boolean - True for WarpPortal, False for Normal Portal
     */
    public boolean isWarpPortal() {
        return isWarpPortal;
    }

}
