WarpPortals API
==================

## API

WarpPortals now has an Event API. Bukkit explains Events at [Event API Reference](http://wiki.bukkit.org/Event_API_Reference). WarpPortals triggers custom events allowing other  plugins to tie into these events. The events provide player & portal information and allow other plugins to cancel the portal teleportation.

An example implementation of the API can be found at [example/WarpPortalsEventListener.java](https://github.com/McCraftaholics/Bukkit-WarpPortals/blob/master/src/com/mccraftaholics/warpportals/api/example/WarpPortalsEventListener.java).

 - This simple Event Listener displays a message to the player upon successful WarpPortal teleportation.

WarpPortals will trigger three events:

  1. WarpPortalsEnterEvent 
  2. WarpPortalsEvent 
  3. WarpPortalsTeleportEvent 

**WarpPortalsEnterEvent** is called when a player enters any portal. The event will contain the player object and a boolean with either True, the portal is a WarpPortal; or False, the portal is a normal Nether/Ender portal.

  * If this event is cancelled, Bukkit handles the portal enter event as default (as if there is no WarpPortals plugin) 

**WarpPortalsEvent** is the main WarpPortal Event. It is called after the WarpPortalsEnterEvent, as long as that event is not cancelled and the portal entered is a WarpPortal.

  * The event contains three pieces of information:
    1. The _player_ triggering the event 
    2. The _portal_ associated with the event. This contains: 
        * Teleportation coordinates 
        * Portal name 
        * Coordinates of blocks making up the portal 
    3. Boolean _hasPermission_
        * True - Player has the "warpportals.enter" permission 
        * False - Player does not have it 
  * This event will _default to cancelled_ if the player does not have the "warpportals.enter" permission. The event can be re-enabled by calling _setCancelled(false)_ on it. 
  * **setTeleportCoordsPY(Location newLoc)**
    * Use this method to change the location that the player will be teleported to. 
  * **getTeleportCoordsPY()**
    * Returns the [CoordsPY](https://github.com/McCraftaholics/Bukkit-WarpPortals/blob/master/src/com/mccraftaholics/warpportals/objects/CoordsPY.java) destination that the player will be teleported to. 
  * An example WarpPortals Economy plugin could use this method to: check if the portal being used costs money; check if the player has the money; charge them OR cancel the event, therefor blocking them from using the portal. 

**WarpPortalsTeleportEvent** is called once the player has been teleported. It has two pieces of information:

  * The _player_ that was teleported 
  * The _previous location_ of the player, AKA the location before they were teleported. 

_Open an Issue on the GitHub page if you have any questions!_

## Changelog

**1.0.0** _(3.0.0)_

  * **Added WarpPortals Event API**
    * Plugins can be written that tie into WarpPortals 
    * Example: An economy plugin can be created that charges people for portal use 
    * **Allows 3rd parties to add missing features**
