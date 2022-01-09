![AFKICON](https://user-images.githubusercontent.com/60233722/106799057-6720f100-6624-11eb-9245-69ee555e9811.PNG)

Minecraft Bukkit plugin to detect when players have gone idle. Allows admins to kick all currenly idle players or configure the plugin to do so automatically after a player has been idle for X amount of time.

# Command menu

![theAfkMenu](https://user-images.githubusercontent.com/60233722/148627881-e7a3ca7a-c6c6-4cbf-85e0-a421d48192e2.PNG)

# Configuration
```
# The amount of time in minutes a player must be away before being marked as AFK
afkTime: 5
# The delay in seconds before the /afk kickall command kicks all players. Set to 0 to disable this.
# If set above 0, the "kickAllPlayersWarning" message will display before players are kicked
kickAllCommandDelay: 2
# The time in seconds before a new "player is now AFK" message will show up. Set this
# higher to prevent this message from filling the chat.
afkCommandBufferTime: 10
# Should players be automatically kicked after a set period of time?
autoKickEnabled: false
# Should we warn the player 1 minute before they're automatically kicked?
shouldWarnPlayersBeforeAutoKick: true
# Should we announce to the server when a player has been auto-kicked?
announceAutoKick: true
# Time in minutes before a player is automatically kicked
timeBeforeAutoKick: 60
# Should the tablist be modified to display AFK players? Set this to false
# if another plugin is modifying your tablist.
shouldDisplayTabListTag: true
# Should the plugin broadcast a message when the /afk kickall command is ran?
announceWhenKickingPlayers: true
# Should the plugin broadcast a message when a player goes idle?
announcePlayerNowAfk: true
# Should the plugin broadcast a message when a player is no longer idle?
announcePlayerNoLongerAfk: true
# Should a player be set idle if they say one of the below messages in chat?
setPlayerAfkViaChatMessage: true
# The messages that will set a player idle (case insensitive). Any message that STARTS WITH
# these messages will set a player idle!
chatMessagesWhichTriggerAfk:
  - 'afk'
  - 'brb'
# Commands that will not remove AFK status from a player. /afk commands do not by default.
ignoredCommands:
    - '/afk'
    - '/example'
messages:
  kickAllCommandMessage: '&c[Notice] &rAll AFK players have been kicked.'
  kickAllPlayersWarning: '&c[Notice] &rAll AFK players will be kicked from the server in <minutes> minutes.'
  messageToKickedPlayers: 'All AFK players have been kicked.'
  youHaveBeenAutoKicked: 'You were idle for too long and have been kicked.'
  autoKickAnnounce: '&c[Notice] &r%playername% was idle for too long and has been kicked.'
  markedYourselfAfk: 'You marked yourself as AFK.'
  isNowAfk: '&8[!] &r%playername% is now AFK.'
  noLongerAfk: '&8[!] &r%playername% is no longer AFK.'
  tabListTag: '%playername% &8AFK'
  noPermission: '&cYou do not have permission to do that.'
  noPlayersAreAfk: 'There are no AFK players at the moment.'
  thesePlayersAreAfk: 'The following players are currently AFK:'
  youAreAboutToBeKicked: '&c[Notice] &rYou are about to be kicked for idling for too long.'
  ```

