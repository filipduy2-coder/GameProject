package PlayerTest;

import CharacterSys.Player;
import InitializationSys.*;
import QuestSys.QuestManager;
import ScriptSys.ScriptEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMovement {
    private World forestWorld() {
        World world = new World();
        WorldBuilder.initForest(world);
        WorldBuilder.connectForestNeighbours(world);
        return world;
    }
    private World academyWorld() {
        World world = new World();
        JsonWorldLoader.load(world);
        return world;
    }
    @Test
    void forestInvalidDirection() {
        World world = forestWorld();
        Player p = new Player("TestUnit",1,1,world,new QuestManager(),new ScriptEngine());
        p.forceMoveTo("forest_2_2");
        assertFalse(p.go("X"));
    }
    @Test
    void forestBlockedDirection() {
        World world = forestWorld();
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo("forest_0_0");
        assertFalse(p.go("N"));
    }
    @Test
    void forestValidDirection() {
        World world = forestWorld();
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo("forest_2_2");
        assertTrue(p.go("N"));
        assertEquals("forest_1_2", p.getCurrentLocation().getId());
    }
    @Test
    void academyRoomDoesNotExist() {
        World world = academyWorld();
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo(LocationNames.CLASSROOM);
        assertFalse(p.go("nonexistent"));
    }
    @Test
    void academyRoomNotVisited() {
        World world = academyWorld();
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo(LocationNames.CLASSROOM);
        assertFalse(p.go(LocationNames.LIBRARY));
    }
    @Test
    void academyRoomVisited() {
        World world = academyWorld();
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo(LocationNames.CLASSROOM);
        String library = LocationNames.LIBRARY;
        p.getVisitedLocations().add(world.getLocationByName(library));
        assertTrue(p.go(library));
        assertEquals(library, p.getCurrentLocation().getId());
    }
    @Test
    void academyBasementBlockedAfterEscape() {
        World world = academyWorld();
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo(LocationNames.LIBRARY);
        p.getVisitedLocations().add(world.getLocationByName(LocationNames.BASEMENT));
        p.setEscapedBasement(true);
        assertFalse(p.go(LocationNames.BASEMENT));
    }
}
