package InitializationSys;

import java.util.HashMap;
import java.util.Random;

public class WorldBuilder {
    public static World buildWorld() {
        World world = new World();
        initForest(world);
        connectForestNeighbours(world);
        placeAcademyEntryBlock(world);
        connectAcademyToWorld(world);
        return world;
    }

    private static void initForest(World world) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Location loc = new Location("forest_" + row + "_" + col,
                        "You are in a dense forest.",
                        new HashMap<>(),
                        LocationType.FOREST
                );
                world.getForest()[row][col] = loc;
                world.getLocations().add(loc);
            }
        }
    }

    private static void connectForestNeighbours(World world) {
        Location[][] forest = world.getForest();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Location loc = forest[row][col];
                if (row > 0)
                    loc.addNeighbour("north", forest[row - 1][col]);
                if (row < 4)
                    loc.addNeighbour("south", forest[row + 1][col]);
                if (col > 0)
                    loc.addNeighbour("west", forest[row][col - 1]);
                if (col < 4)
                    loc.addNeighbour("east", forest[row][col + 1]);
            }
        }
    }

    private static void placeAcademyEntryBlock(World world) {
        Random r = new Random();
        int row = r.nextInt(5);
        int col = r.nextInt(5);
        world.setAcademyEntryBlock(world.getForest()[row][col]);
    }
    private static void connectAcademyToWorld(World world) {
        Location entry = world.getAcademyEntryBlock();
        Location destination = world.getLocationByName("Basic Magic Classroom");

        if (entry == null || destination == null) {
            System.out.println("Error: academy entry or destination not found");
            return;
        }
        entry.addNeighbour("academy", destination);
    }
}