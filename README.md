# TeleportGUI
GUI teleport system, for small, private server.

### Supported Version (Spigot)
- 1.19.1

## Features
- [X] GUI-Less teleportation
- [X] Teleport delay
- [X] Teleport sound

## Commands
- /t - <b>Open Teleport GUI</b> (teleportgui.use)
- /treload - <b>Reload plugin</b> (teleportgui.admin)

## Configuration
```
# Inventory Name
inventory-title = "&cChoose a player!"
# Player Skull Name
display-name-color" = "&b"
# Message when player teleport to you
notify-message" = "&a%player% &eteleported to you!"
# Notify when player teleport to you
notify-target = true
# Play sound when teleport
teleport-sound = true
# Delay when teleport (0 for no delay)
teleport-delay = 3
```
