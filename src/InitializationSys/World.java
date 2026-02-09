package InitializationSys;

import CharacterSys.Player;
import ItemSys.Item;
import ItemSys.Recipe;
import QuestSys.Quest;
import QuestSys.QuestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private List<Location> locations = new ArrayList<>();
    private Location[][] forest = new Location[5][5];
    private List<Quest> quests = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private Location academyEntryBlock;
    private Location currentBoneLocation;
    private List<Location> boneLocation = new ArrayList<>();
    private QuestManager questManager = new QuestManager();

    public List<Location> getBoneLocation() {
        for (int i = 1; i < 7; i++) {
            boneLocation.add(locations.get(i));
        }
        return boneLocation;
    }
    public void activeBoneQuest(Player player) {
        Random rand = new Random();
        currentBoneLocation = boneLocation.get(rand.nextInt(boneLocation.size()));
        currentBoneLocation.addItem(new Item("Bone"));
        questManager.activeQuestByName("Find a bone for Togo", player);
        System.out.println("Togo seems to sniff toward a room with: " + generateHint(currentBoneLocation));
    }
    public String generateHint(Location loc) {
        String hint = loc.getDescription();
        if (hint.contains(",")) {
            return hint.substring(0, hint.indexOf(","));
        }
        return hint;
    }
    public void clearBoneLocation() {
        currentBoneLocation = null;
    }

    public Location getLocationByName(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase().trim();
        for (Location location : locations) {
            if (location.getId().toLowerCase().trim().equals(key)) {
                return location;
            }
        }
        return null;
    }
    public void printMapForest(Player player) {
        if (player.getCurrentLocation().getType() == LocationType.ACADEMY) {
            return;
        }
        Location[][] forest = this.forest;
        Location playerLoc = player.getCurrentLocation();
        Location academy = this.academyEntryBlock;
        System.out.println("=== MAP ===");
        for (Location[] value : forest) {
            for (Location loc : value) {
                if (loc == playerLoc) {
                    System.out.print("●  ");   // hráč
                } else if (loc == academy) {
                    System.out.print("X  ");   // akademie
                } else {
                    System.out.print(".  ");   // prázdné pole
                }
            }
            System.out.println();
        }
        System.out.println("=============");
    }
    public Item findItemByName(String name) {
        for (Location loc : locations) {
            for (Item item : loc.getItems()) {
                if (item.getName().equals(name)) {
                    return item;
                }
            }
        }
        return null;
    }
    public Location getStartingLocation() {
        return forest[0][0];
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Location[][] getForest() {
        return forest;
    }

    public void setAcademyEntryBlock(Location academyEntryBlock) {
        this.academyEntryBlock = academyEntryBlock;
    }

    public Location getAcademyEntryBlock() {
        return academyEntryBlock;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Location getCurrentBoneLocation() {
        return currentBoneLocation;
    }

    public Recipe getRecipeByName(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(name)) {
                return recipe;
            }
        }
        return null;
    }

}
