package com.mccraftaholics.warpportals.api.example;

import com.mccraftaholics.warpportals.api.WarpPortalsTeleportEvent;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WarpPortalsEventListener implements Listener {

    // Array of messages that denote 'No message to be sent'
    static final String[] NO_MESSAGE = new String[]{"&none", "none", ""};
    /* Class variables */
    // Message to send to player upon teleport
    String mDefaultTPMessage;
    // ChatColor of message
    ChatColor mTPC;

    /**
     * Create an Event Listener for the WarpPortals Event API. This listener listens for the WarpPortalsTeleportEvent
     * and sends a colored chat message to the player when the event is triggered.
     *
     * @param tpMessage   - Message to send
     * @param tpCharColor - ChatColor of the message
     */
    public WarpPortalsEventListener(String tpMessage, ChatColor tpCharColor) {
        mDefaultTPMessage = tpMessage;
        mTPC = tpCharColor;

        if (Utils.arrayContains(NO_MESSAGE, mDefaultTPMessage))
            mDefaultTPMessage = null;
    }

    @EventHandler
    public void onTeleport(WarpPortalsTeleportEvent event) {
        Player player = event.getPlayer();
        PortalInfo portal = event.getPortal();
        if (portal.message.equals("$default")) {
            if (mDefaultTPMessage != null)
                player.sendMessage(mTPC + mDefaultTPMessage);
        } else {
            player.sendMessage(mTPC + portal.message.replace("$user", player.getDisplayName()));
        }
    }
}
