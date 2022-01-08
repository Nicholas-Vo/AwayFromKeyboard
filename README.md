![AFKICON](https://user-images.githubusercontent.com/60233722/106799057-6720f100-6624-11eb-9245-69ee555e9811.PNG)

Minecraft Bukkit plugin to detect when players have gone idle.

# Command menu

![theAfkMenu](https://user-images.githubusercontent.com/60233722/148627881-e7a3ca7a-c6c6-4cbf-85e0-a421d48192e2.PNG)

# Configuration
```
# The amount of time a player must be away before being marked as AFK.
afkTime: 5
# Should the console be notified when a player's AFK status changes?
consoleNotifications: true
# Should the tablist be modified to display AFK players? Set this to false
# if another plugin is modifying your tablist.
displayTabListTag: true
# Should the plugin broadcast a message when kicking all AFK players?
announceWhenKickingPlayers: true
# Should the plugin broadcast a message when a player goes idle?
announcePlayerNowAfk: true
# Should the plugin broadcast a message when a player is no longer idle?
announcePlayerNoLongerAfk: true
# Commands that will not remove AFK status from a player
ignoredCommands:
    - '/msg'
    - '/vanish'
messages:
  announcementToServer: '&c[Notice] &7All AFK players have been kicked due to poor server performance.'
  messageToKickedPlayers: 'All AFK players have been kicked due to poor server performance.'
  markedYourselfAfk: 'You marked yourself as AFK.'
  isNowAfk: '&8[!] &r%playername% is now AFK.'
  noLongerAfk: '&8[!] &r%playername% is no longer AFK.'
  tabListTag: '%playername% &8AFK'
  noPermission: '&cYou do not have permission to do that.'
  noPlayersAreAfk: 'There are no AFK players at the moment.'
  thesePlayersAreAfk: 'The following players are currently AFK:'
  ```

