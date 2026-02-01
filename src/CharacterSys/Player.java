package CharacterSys;
import QuestSys.QuestLog;
import ItemSys.Item;
import InitializationSys.Location;
import ItemSys.Recipe;

import java.util.Set;

public class Player extends Character {
    private Inventory inventory;
    private Companion companion;
    private Location currentLocation;
    private Set<Location> visitedLocations;
    private QuestLog questLog;

    public Player(String name, int hp, int strength, boolean hostile,
                  Inventory inventory, Companion companion, Location currentLocation, QuestLog questLog) {
        super(name, hp, strength, hostile);
        this.inventory = inventory;
        this.companion = companion;
        this.currentLocation = currentLocation;
        this.questLog = questLog;
    }

    public void go(Location target) {}
    public void talkTo(NPC npc) {}
    public void useItem(Item item) {}
    public void pickUp(Item item) {}
    public void dropItem(Item item) {}
    public void showInventory() {}
    public void showQuestLog() {}
    public void craftItem(Recipe recipe) {}

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void attack(Character target) {

    }

    @Override
    public void takeDamage(int amount) {

    }
}
