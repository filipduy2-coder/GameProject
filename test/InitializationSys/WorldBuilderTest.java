package InitializationSys;

import CharacterSys.Companion;
import CharacterSys.NPC;
import CharacterSys.Player;
import QuestSys.QuestManager;
import ScriptSys.ScriptEngine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WorldBuilderTest {
    private World academyWorld() {
        World world = new World();
        JsonWorldLoader.load(world);
        return world;
    }

    @Test
    void runScriptWhenElianAlive() {
        World world = academyWorld();

        ScriptEngine se = new ScriptEngine();
        Companion companion = new Companion();
        Player player = new Player("TestUnit", 100, 10, world, new QuestManager(), se);
        NPC elian = new NPC("elian", 100, 20);
        world.getLocationByName(LocationNames.DORMITORY).addNPC(elian);
        world.registerNPC(elian);

        WorldBuilder.buildScript(world, se, player, companion);
        assertEquals(LocationNames.LABORATORY, se.runScript(LocationNames.CLASSROOM));
        assertEquals(LocationNames.LIBRARY, se.runScript(LocationNames.LABORATORY));
        assertEquals(LocationNames.ILLUSION_ROOM, se.runScript(LocationNames.LIBRARY));
        assertEquals(LocationNames.HEALING_ROOM, se.runScript(LocationNames.ILLUSION_ROOM));
        assertEquals(LocationNames.STORAGE, se.runScript(LocationNames.HEALING_ROOM));
        assertEquals(LocationNames.DORMITORY, se.runScript(LocationNames.STORAGE));
    }

    @Test
    void runScriptWhenDormitoryEmptyCompanionNotHungry() {
        World world = academyWorld();

        ScriptEngine se = new ScriptEngine();
        Companion companion = new Companion();
        companion.setHungry(false);

        Player player = new Player("TestUnit", 100, 10, world, new QuestManager(), se);
        player.forceMoveTo(LocationNames.LABORATORY);
        player.setState(GameState.NORMAL);

        world.getLocationByName(LocationNames.DORMITORY).getNpcs().clear();

        WorldBuilder.buildScript(world, se, player, companion);

        assertEquals(LocationNames.LIBRARY, se.runScript(LocationNames.LABORATORY));
    }

    @Test
    void runScriptWhenPlayerEscapedCompanionNotHungry() {
        World world = academyWorld();
        ScriptEngine se = new ScriptEngine();
        Companion companion = new Companion();
        companion.setHungry(false);
        Player player = new Player("TestUnit", 100, 10, world, new QuestManager(), se);
        player.forceMoveTo(LocationNames.LIBRARY);
        player.setEscapedBasement(true);
        WorldBuilder.buildScript(world, se, player, companion);
        assertEquals(LocationNames.DORMITORY, se.runScript(LocationNames.LIBRARY));
    }
}