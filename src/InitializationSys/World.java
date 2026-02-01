package InitializationSys;

import ItemSys.Item;
import ItemSys.Recipe;
import QuestSys.Quest;

import java.util.ArrayList;
import java.util.List;

public class World {
    private List<Location> locations = new ArrayList<>();
    private Location[][] forest = new Location[5][5];
    private List<Quest> quests = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private Location startingLocation;
    private Location academyEntryBlock;

    public Location getLocationByName(String name) {
        for (Location location : locations) {
            if (location.getId().equals(name)) {
                return location;
            }
        }
        return null;
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
}
