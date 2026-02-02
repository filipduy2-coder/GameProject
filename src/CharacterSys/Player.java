package CharacterSys;
import InitializationSys.GameState;
import InitializationSys.LocationType;
import InitializationSys.World;
import ItemSys.Item;
import ItemSys.ItemType;
import QuestSys.QuestLog;
import InitializationSys.Location;

import java.util.Set;

public class Player extends Character {
    private Inventory inventory;
    private Companion companion;
    private Location currentLocation;
    private Set<Location> visitedLocations;
    private QuestLog questLog;
    private GameState state = GameState.NORMAL;
    private World world;

    public Player(String name, int hp, int strength, boolean hostile,
                  Inventory inventory, Companion companion, Location currentLocation, QuestLog questLog, World world) {
        super(name, hp, strength, hostile);
        this.inventory = inventory;
        this.companion = companion;
        this.currentLocation = currentLocation;
        this.questLog = questLog;
        this.world = world;
    }

    public void go(String input) {
        LocationType type = currentLocation.getType();
        switch (type) {
            case FOREST -> {
                input = input.toUpperCase();
                if (!input.equals("N") && !input.equals("S") && !input.equals("W") && !input.equals("E")) {
                    System.out.println("Invalid direction. Use N, S, W or E");
                    return;
                }
                Location next = currentLocation.getNeighbours().get(input);
                if (next == null) {
                    System.out.println("You can't go that way");
                    return;
                }
                currentLocation = next;
                System.out.println("You moved" + input + "to" + next.getId());
            }
            case ACADEMY -> {
                input = input.toLowerCase();
                Location next = world.getLocationByName("input");
                if (next == null || next.getType() != LocationType.ACADEMY) {
                    System.out.println("No such room in the academy");
                    return;
                }
                visitedLocations.add(currentLocation);
                if (!visitedLocations.contains(next)) {
                    System.out.println("You haven't visited this room yet.");
                }
                currentLocation = next;
                visitedLocations.add(next);
                System.out.println("You entered" + next.getId());
            }
        }
    }
    public void talkTo(String nameNPC) {
    }
    public void useItem(String nameItem) {
            Item item = inventory.getItem(nameItem);
            if (item == null) {
                System.out.println("You don't have that item.");
                return;
            }
            if (item.getType() != ItemType.USABLE) {
                System.out.println("You can't use this item.");
                return;
            }
            System.out.println("You used: " + item.getName());
            inventory.removeItem(item);
    }
    public void pickUp(String itemName) {
        Item item = currentLocation.getItem(itemName);
        if (item == null) {
            System.out.println("No such item here.");
            return;
        }
        if (inventory.isFull()) {
            System.out.println("Your inventory is full.");
            return;
        }
        inventory.addItem(item);
        currentLocation.removeItem(itemName);
        System.out.println("You picked up: " + item.getName());
    }
    public void dropItem(String itemName) {
        Item item = inventory.getItem(itemName);
        if (item == null) {
            System.out.println("You don't have that item.");
            return;
        }
        inventory.removeItem(item);
        currentLocation.addItem(item);
        System.out.println("You dropped: " + item.getName());
    }
    public void showInventory() {
        System.out.println("Inventory:");
        inventory.printInventory();
        System.out.println("Free slots: " + inventory.getFreeSlots());
    }
    public void showQuestLog() {
        System.out.println("Active Quests:");
        questLog.printActiveQuests();
        System.out.println("Completed Quests:");
        questLog.printCompletedQuests();
    }
    public void craftItem(String nameRecipe) {}
    public void attack(String nameNPC) {}

    public void forceMoveTo(String locationName) {
        Location next = world.getLocationByName(locationName.toLowerCase());
        if (next == null) {
            System.out.println("Error: location does not exist");
            return;
        }
        visitedLocations.add(next);
        currentLocation = next;
        System.out.println("You are in" + next.getId());
    }
    public void enterCombat() {
        this.state = GameState.COMBAT;
        System.out.println("You're combat!");
    }
    public void leaveCombat() {
        this.state = GameState.COMBAT;
        System.out.println("Combat ended!");
    }
    public void enterTrapped() {
        this.state = GameState.TRAPPED;
        System.out.println("You're trapped in the basement!");
    }
    public void leaveTrapped() {
        this.state = GameState.NORMAL;
        System.out.println("You escaped the basement!");
    }
    public Inventory getInventory() {
        return inventory;
    }

    public GameState getState() {
        return state;
    }

    @Override
    public void takeDamage(int amount) {

    }
}
