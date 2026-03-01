# 🕹️About game
This game is about text-based adventure game where the player starts in a dark forest and must find their way into a royal academy. Inside the academy, the player interacts with NPCs, complete quests, collects necessary items, crafts, fight enemies, and uncovers the story. The game features a dynamic quest system, scripted NPC movement, a forest grid map, and a simple combat system.

# 🧩Commands
The game is controlled entirely through text commands. Depending on the player's state, the player can use: 
>In normal state:
```
go <direction / room>
talk <npc>
attack <npc>
pick <item>
remove <item>
show questlog
show inventory
```
>In combat state:
```
talk <npc>
attack <npc>
use <item>
show questlog
show inventory
```
>In trapped state:
```
use <item>
craft <item>
pick <item>
remove <item>
show questlog
show inventory
```
All state can use system commands `Exit` `Hint` `Help`.
# 🤖Game mechanics
### Quest system
Each quest has activation triggers and complletion conditions. Multiple quests can be active at once
### Scripted events 
Some rooms trigger atutomatic story events or player's movements. 
### Inventory & Items
Items may have automatic effects or usable effects or also may have no effect at all. Also there are item can affect the storyline.
### Crafting system
Player can craft new items using recipes including ingredients.
### Combat mode, Dialog
Certain actions may occur after you speak with an NPC. The player's state will switch into **Combat** mode, where only specific commands and items are allowed.
### Movement system
Depending on the current location where the player at, the movement command may varyt. For example player is in the forest, they must use direction to move to another location; otherwise they should use the location's name to move.You can move to the next location of ​​the script if you don't type anything(Enter only).
# 🗝️How to Run the Game
**1.** CLone the repository:
```
git clone <repository-url>
``` 
**2.** Open the project in any Java-compatible IDE.\
**3.** Make sure you have Java 17+ installed.\
**4.** Run the `main()` method in `Main` class.\
**5.** The game will start in the console.
# 🎭Technology used
**Java 17+**\
**Maven**(pom.xml)\
**Json**(world.json)
# ✨Additional information:
If player is currently in the beginning phase, then do nothing else except follow the map to point X by using command `go <N(north) / S(south) / W(west) / E(east)>`\

In the game is exist recipe for crafting `explosive` by using ingredients `Sand` `Gravel` `Gunpowder`.
