WarpPortals - A Bukkit plugin
==================

A Bukkit Plugin that allows users to create any shape/sized portals for use in the Minecraft game

Here is **WarpPortals**! This plugin was created due to an inability to find a **robust**, **functional**, and **endlessly customizable** portals plugin.

*You can test out the plugin in action at* **play.mc-craftaholics.com:25662**

## Features

* Portals of *any* shape or size!
* Portals without frames or with frames of any material
* Portals that work between any worlds
* Easy to create Portals
* Easy to set destinations
* Custom teleportation message
* Precise teleportations

## Commands ##
```
* /phelp: List all Portal related commands
* /pcreate [portalname] [destName|(World,x,y,z)]: Equip the current item as a Portal Creation tool
* /pdelete [portalName]: Delete a Portal by name
* /pdeltool: Bind the Portal Deletion tool to the current item
* /pdest [destName]: Save your current location as a WarpPortal Destination
* /pdestdel [destName]: Remove the Portal Destination
* /plist: List all Portals
* /pdestlist: List all saved Portal Destinations
* /pgoto [p|d] [destName|portalName]: Teleport to a Portal or Destination
* /psave: Force save all Portal data
* /pload: Force load Portal data from portals.yml
* /pbackup: Backup the current Portal data to "portals.yml_yyyy-MM-ddTkk-mm-ss.bac"
```
## FAQ

**How do you create a Portal?**

First choose the Portal destination and set it using "/pdest [name]". *This command will set the destination to your current location, world AND angle of view.*
Second, build a portal of any shape out of Gold Blocks. These will make up the Portal's body.
Third, hold a non-block item and run the command "/pcreate [portalName] [destName]". This will make it so the next Gold Block you click will turn them into a Portal.
Fourth, right-click any one of the Gold Blocks you built. The plugin will find all adjacent Gold Blocks and turn them all into a Portal!
Fifth, profit.

**Who can use Portals?**

Any one can use a Portal by default. You can revoke someone's Portal rights by setting their permission "warpportal.enter" to false.

**How do you use Portals?**

Portals are always active and simply walking though one will teleport you to its destination.

**Who can create/admin Portals?**

Only players who are ops or have the "warpportal.*" permission.

**What settings are there?**

1. You can change the general Text Color used by the plugin. By default Yellow.
2. You can adjust the max portal size. This limit exists so that a Portal so large that it would eternally crash the server can't be created. By default 1000.
3. You can change the teleportation message. By default it is "Wooooooooosh!"
4. You can change the teleportation message Text Color. By default Purple.

**Is there Economy support?**

No. Portals are always active and currently don't support any form of Economy plugins.

**What bugs are there?**

Ghost Portals:
*Currently, anyone can break the Portal Blocks that make up the Portal and that will cause the PORTAL_ENTERED event to never fire. This means that to the plugin the Portal still exists but realistically it isn't there anymore. To keep this from happening something like WorldGuard should be used to protect the Portals (though this protection may be a built-in feature in the future). To deal with the possibility of all the Portal blocks getting broken in a Portal, you can use the "/pdelete [portalName]" command to return the Portal to its original Gold Block form. If only a few blocks get broken, simply replacing them with new Portal Blocks should work.*

Bad Text Colors:
*Also, the Text Color settings don't appear to be cross OS Compatible. By default they are setup for Windows but if they don't work for you (if you get white text from the plugin) all Text Color Codes are editable in the Plugin's settings file.*

## Install
1. Download the latest version right here!
2. Drop it into the Plugins folder in your CraftBukkit install.
3. Go create some spiffy, funky, wild, seriously fun Portals!

## Source Code and Downloads

The latest version to install an be found attached to this page!

Source code can be found at https://github.com/McCraftaholics/Bukkit-WarpPortals
* Please feel free to make changes!
* Pull requests are awesome.

Issues can be posted at https://github.com/McCraftaholics/Bukkit-WarpPortals/issues

The bukkit-dev page is located at http://dev.bukkit.org/bukkit-plugins/warp-portals/

Enjoy :)