package com.mccraftaholics.warpportals.api.example;

import com.mccraftaholics.warpportals.api.WarpPortalsTeleportEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WarpPortalsEventListener implements Listener {

    /* Class variables */
    // Message to send to player upon teleport
    String mTPMessage;
    // ChatColor of message
    ChatColor mTPC;

    /**
     * Create an Event Listener for the WarpPortals Event API. This listener listens for the WarpPortalsTeleportEvent
     * and sends a colored chat message to the player when the event is triggered.
     *
     * @param tpMessage - Message to send
     * @param tpCharColor - ChatColor of the message
     */
    public WarpPortalsEventListener(String tpMessage, ChatColor tpCharColor) {
        mTPMessage = tpMessage;
        mTPC = tpCharColor;
    }

    @EventHandler
    public void onTeleport(WarpPortalsTeleportEvent event) {
        Player player = event.getPlayer();
        if (mTPMessage != null && !mTPMessage.equals("&none"))
            player.sendMessage(mTPC + mTPMessage);
    }
}
