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

    public List<Item> getItems() {
        return items;
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
