package ItemSys;

import CharacterSys.NPC;
import CharacterSys.Player;
import InitializationSys.*;
import QuestSys.QuestManager;
import ScriptSys.ScriptEngine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private World simpleWorld() {
        World world = new World();
        JsonWorldLoader.load(world);
        return world;
    }

    @Test
    void useStrengthMultiplierInCombat() {
        World world = simpleWorld();
        ScriptEngine se = new ScriptEngine();
        Game game = new Game();
        Player player = new Player("Test", 100, 10, world, new QuestManager(), se);
        player.forceMoveTo(LocationNames.LIBRARY);
        player.enterCombat();
        Map<String, Object> props = new HashMap<>();
        props.put("strengthMultiplier", 2.0);

        Item potion = new Item("PowerPotion","increase your strength" ,ItemType.USABLE, props);
        boolean result = potion.applyUsableEffect(player, world, game);
        assertTrue(result);
        assertEquals(20, player.getStrength());
    }

    @Test
    void failStrengthMultiplierOutsideCombat() {
        World world = simpleWorld();
        ScriptEngine se = new ScriptEngine();
        Game game = new Game();

        Player player = new Player("Test", 100, 10, world, new QuestManager(), se);
        player.forceMoveTo(LocationNames.LIBRARY);
        player.setState(GameState.NORMAL);

        HashMap<String, Object> props = new HashMap<>();
        props.put("strengthMultiplier", 2.0);

        Item potion = new Item("PowerPotion","increase your strength", ItemType.USABLE, props);
        boolean result = potion.applyUsableEffect(player, world, game);
        assertFalse(result);
        assertEquals(10, player.getStrength());
    }

    @Test
    void useExplosiveInCorrectLocation() {
        World world = simpleWorld();
        ScriptEngine se = new ScriptEngine();
        QuestManager qm = new QuestManager();

        Player player = new Player("Test", 100, 10, world, qm, se);
        player.forceMoveTo(LocationNames.BASEMENT);

        Game game = new Game(world,player,se);
        game.setQuestManager(qm);

        HashMap<String, Object> props = new HashMap<>();
        props.put("explode", "BOOM!");
        props.put("usabledOnlyIn", LocationNames.BASEMENT);

        Item bomb = new Item("Explosive","BOOM!", ItemType.USABLE, props);
        boolean result = bomb.applyUsableEffect(player, world, game);
        assertTrue(result);
        assertTrue(player.isEscapedBasement());
        assertEquals(LocationNames.LIBRARY, player.getCurrentLocation().getId());
    }
    @Test
    void useExplosiveInWrongLocation() {
        World world = simpleWorld();
        ScriptEngine se = new ScriptEngine();
        QuestManager qm = new QuestManager();

        Player player = new Player("Test", 100, 10, world,qm, se);
        player.forceMoveTo(LocationNames.BASEMENT);

        Game game = new Game(world,player,se);
        game.setQuestManager(qm);

        HashMap<String, Object> props = new HashMap<>();
        props.put("explode", "BOOM!");
        props.put("usabledOnlyIn", LocationNames.LIBRARY);

        Item bomb = new Item("Explosive","BOOM!", ItemType.USABLE, props);
        boolean result = bomb.applyUsableEffect(player, world, game);
        assertFalse(result);
        assertFalse(player.isEscapedBasement());
    }
}