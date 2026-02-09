package InitializationSys;

import CharacterSys.Player;
import Script.ScriptEngine;

import java.util.HashMap;
import java.util.Random;

public class WorldBuilder {
    public static void buildWorld(World world) {
        initForest(world);
        connectForestNeighbours(world);
        placeAcademyEntryBlock(world);
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
                    loc.addNeighbour("N", forest[row - 1][col]);
                if (row < 4)
                    loc.addNeighbour("S", forest[row + 1][col]);
                if (col > 0)
                    loc.addNeighbour("W", forest[row][col - 1]);
                if (col < 4)
                    loc.addNeighbour("E", forest[row][col + 1]);
            }
        }
    }

    private static void placeAcademyEntryBlock(World world) {
        Random r = new Random();
        Location start = world.getStartingLocation();
        Location chosen;
        do {
            int row = r.nextInt(5);
            int col = r.nextInt(5);
            chosen = world.getForest()[row][col];
        } while (chosen == start);
        world.setAcademyEntryBlock(chosen);
    }
    public static void buildScript(World world, ScriptEngine scriptEngine, Player player) {
        scriptEngine.addStep("Basic Magic Classroom","Advanced Spell Laboratory");
        scriptEngine.addStep("Advanced Spell Laboratory","Silent Study Room");
        scriptEngine.addStep("Silent Study Room","Illusion Classroom");
        scriptEngine.addStep("Illusion Classroom","Healing Magic Department");
        scriptEngine.addStep("Healing Magic Department","Magical Supplies Storage");
        scriptEngine.addStep("Magical Supplies Storage","Student Dormitory");
        if (!world.getLocationByName("student dormitory").hasNPC("Elian")) {
            scriptEngine.addStep("Student Dormitory","Silent Study Room");
        }
        if (player.isEscapedBasement()) {
            scriptEngine.addStep("Silent Study Room","Student Dormitory");
        }
    }
}