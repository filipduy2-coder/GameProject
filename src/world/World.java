package world;

import java.util.List;

public class World {
    private List<Location> locations;
    private Location[][] forest = new Location[5][5];
    private Location startingLocation;
    private Location academyEntryBlock;

    public void initWorld() {}
    public void initForest() {}
    public void placeAcademyEntryBlock(Location location) {}
    public void connectForestNeighbours() {}
    public Location getLocationByName(String name) {return null;}

}
