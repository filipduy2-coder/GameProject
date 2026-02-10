package InitializationSys;

import CharacterSys.NPC;
import ItemSys.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {
    private String id;
    private String description;
    private Map<String, Location> neighbours = new HashMap<>();
    private List<Item> items;
    private List<NPC> npcs;
    private LocationType type;

    public Location(String name, String description, Map<String, Location> neighbours, LocationType type) {
        this.id = name;
        this.description = description;
        this.neighbours = (neighbours != null) ? neighbours : new HashMap<>();
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
        this.type = type;
    }

    public void addNeighbour(String direction, Location neighbour) {
        neighbours.put(direction, neighbour);
    }
    public void addItem(Item item) {
        items.add(item);
    }
    public void addNPC(NPC npc) {
        npcs.add(npc);
    }
    public String getId() {
        return id;
    }
    public LocationType getType() {
        return type;
    }
    public Map<String, Location> getNeighbours() {
        return neighbours;
    }
    public List<Item> getItems() {
        return items;
    }

    public List<String> getAllItems() {
        List<String> names = new ArrayList<>();
        for (Item item : items) {
            names.add(item.getName());
        }
        return names;
    }
    public List<String> getAllNPCs() {
        List<String> names = new ArrayList<>();
        for (NPC npc : npcs) {
            names.add(npc.getName());
        }
        return names;
    }
    public NPC getNPCByName(String name) {
        for (NPC npc : npcs) {
            if (npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
    }
    public Item getItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) return item;
        }
        return null;
    }
    public String getDescription() {
        return description;
    }
    public void removeItem(String itemName) {
        items.removeIf(item -> item.getName().equals(itemName));
    }
    public void removeNPC(NPC npc) {
        npcs.remove(npc);
    }
    public boolean hasNPC(String name) {
        for (NPC npc : npcs) {
            if (npc.getName().equals(name)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", neighbours=" + neighbours +
                ", items=" + items +
                ", npcs=" + npcs +
                ", type=" + type +
                '}';
    }
}
