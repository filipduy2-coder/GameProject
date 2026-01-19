package world;

import java.util.List;
import java.util.Map;

public class Location {
    private String name;
    private String description;
    private List<Item> items;
    private List<Character> characters;
    private Map<String, Location> neighbours;
    private boolean useDirection;

    public void addNeighbour(String direction, Location neighbour) {}
    public boolean usesDirections() {}
}
