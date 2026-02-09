package CharacterSys;

import InitializationSys.GameState;
import InitializationSys.LocationType;
import InitializationSys.World;
import ItemSys.Item;
import ItemSys.ItemType;
import ItemSys.Recipe;
import QuestSys.QuestLog;
import InitializationSys.Location;
import QuestSys.QuestManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player extends Character {
    private int baseHealth;
    private int baseStrength;
    private Inventory inventory;
    private Location currentLocation;
    private Set<Location> visitedLocations;
    private QuestLog questLog;
    private QuestManager questManager;
    private GameState state = GameState.NORMAL;
    private World world;
    private boolean escapedBasement;

    public Player(String name, int hp, int strength, World world, QuestManager questManager) {
        super(name, hp, strength);
        this.baseHealth = hp;
        this.baseStrength = strength;
        this.inventory = new Inventory(new ArrayList<>(), 3);
        this.visitedLocations = new HashSet<>();
        this.questLog = new QuestLog();
        this.escapedBasement = false;
        this.world = world;
        this.questManager = questManager;
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
                System.out.println("You moved to " + next.getId());
            }
            case ACADEMY -> {
                input = input.toLowerCase();
                Location next = world.getLocationByName(input);
                if (next == null || next.getType() != LocationType.ACADEMY) {
                    System.out.println("No such room in the academy");
                    return;
                }
                if (!visitedLocations.contains(next)) {
                    System.out.println("You haven't visited this room yet.");
                    return;
                }
                if (next.getId().equalsIgnoreCase("Abandoned Basement") && escapedBasement) {
                    System.out.println("You can't return to the basement. The entrance collapsed");
                    return;
                }
                if (next.getId().equals("Abandoned Basement") && !escapedBasement) {
                    enterTrapped();
                }
                currentLocation = next;
                System.out.println("You entered" + next.getId());
            }
        }
    }

    public void talkTo(String nameNPC) {
        NPC npc = currentLocation.getNPCByName(nameNPC);
        if (npc == null) {
            System.out.println("There is no one named " + nameNPC + " here.");
            return;
        }
        if (npc.getName().equals("Lynne") && npc.hasPreActionDialogue()) {
            for (String s : npc.getPreActionDialogue()) {
                System.out.println(npc.getName() + ':' + s);
            }
            forceMoveTo("Abandoned Basement");
            for (String s : npc.getPostActionDialogue()) {
                System.out.println(npc.getName() +  ':' + s);
            }
            npc.clearPreActionDialogue();
            npc.clearPostActionDialogue();
            return;
        }
        if (npc.isCompletedDialogue()) {
            System.out.println(npc.getName() + " has nothing to say.");
        } else {
            for (String s : npc.getDialogue()) {
                System.out.println(npc.getName() + ':' + s);
                npc.setCompletedDialogue(true);
            }
        }
        questManager.checkTrigger("talk_to_npc:" + npc.getName(),this);
        if(npc.isStartsCombat()) {
            enterCombat();
        }
    }

    public void useItem(String nameItem) {
        Item item = inventory.getItemByName(nameItem);
        if (item == null) {
            System.out.println("You don't have that item.");
            return;
        }
        if (item.getType() != ItemType.USABLE) {
            System.out.println("You can't use this item.");
            return;
        }
        System.out.println("You used: " + item.getName());
        item.applyUsableEffect(this);
        questManager.checkCompletion("use_item:" + item.getName(),this);
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
        if (item.getName().equals("Bone") && currentLocation == world.getCurrentBoneLocation()) {
            currentLocation.removeItem("Bone");
            questManager.checkCompletion("pickup_item:" + item.getName(),this);
            world.clearBoneLocation();
            System.out.println("Togo happily takes the bone from your hands");
            return;
        }
        inventory.addItem(item);
        currentLocation.removeItem(itemName);
        System.out.println("You picked up: " + item.getName());
    }

    public void dropItem(String itemName) {
        Item item = inventory.getItemByName(itemName);
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
        questLog.printActiveQuests();
        questLog.printCompletedQuests();
    }

    public void craftItem(String nameRecipe) {
        Recipe recipe = world.getRecipeByName(nameRecipe);
        if (recipe == null) {
            System.out.println("No such recipe.");
            return;
        }
        for (Item ingredient : recipe.getIngredients()) {
            if (!inventory.hasItem(ingredient.getName())) {
                System.out.println("Missing ingredient: " + ingredient.getName());
                return;
            }
        }
        for (Item ingredient : recipe.getIngredients()) {
            Item invItem = inventory.getItemByName(ingredient.getName());
            inventory.removeItem(invItem);
        }
        Item result = recipe.getResult();
        inventory.addItem(result);
        System.out.println("You crafted: " + result.getName());
        questManager.checkCompletion("craft_item:" + result.getName(),this);
        questManager.checkTrigger("craft_item:" + result.getName(),this);
    }

    public void attack(String nameNPC) {
        NPC target = currentLocation.getNPCByName(nameNPC);
        if (target == null) {
            System.out.println("No such enemy here.");
            return;
        }
        if (!target.isHostile()) {
            System.out.println(target.getName() + " is not hostile.");
            return;
        }
        System.out.println("You attacked: " + target.getName() + " for " + strength + " damage!");
        target.takeDamage(strength);
        if (!target.isAlive()) {
            System.out.println("You defeated: " + target.getName() + '!');
            questManager.checkCompletion("defeat_enemy:" + target.getName(),this);
            if (target.isCompletedDialogue()) {
                System.out.println(target.getName() + " has nothing to say.");
            } else {
                for (String s : target.getDialogue()) {
                    System.out.println(target.getName() + ':' + s);
                    target.setCompletedDialogue(true);
                }
            }
            currentLocation.removeNPC(target);
            resetCombatEffect();
            leaveCombat();
            return;
        }
        System.out.println(target.getName() + " attacks you for " + target.getStrength() + " damage!");
        this.takeDamage(target.getStrength());
        for (Item item : inventory.getItems()) {
            if (item.getType() == ItemType.AUTO) {
                item.applyAutoEffect(this);
            }
        }
        System.out.println("Player health: " + this.health);
        System.out.println("Enemy health: " + target.getHealth());
        if (!this.isAlive()) {
            System.out.println("You died!");
            return;
        }
        enterCombat();
    }

    public void forceMoveTo(String locationName) {
        Location next = world.getLocationByName(locationName.toLowerCase());
        if (next == null) {
            System.out.println("Error: location does not exist");
            return;
        }
        visitedLocations.add(currentLocation);
        visitedLocations.add(next);
        currentLocation = next;
    }
    public void resetCombatEffect() {
        health = baseHealth;
        strength = baseStrength;
    }
    public void enterCombat() {
        this.state = GameState.COMBAT;
        System.out.println("You're combat!");
    }

    public void leaveCombat() {
        this.state = GameState.NORMAL;
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
    public GameState getState() {
        return state;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
    public QuestLog getQuestLog() {
        return questLog;
    }

    public boolean isEscapedBasement() {
        return escapedBasement;
    }
    public void setHeath(int heath) {
        this.health = heath;
    }
    public void setStrength(int strength) {
        this.strength = strength;
    }
    public void setEscapedBasement(boolean escaped) {
        this.escapedBasement = escaped;
    }
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
        }
    }
}
