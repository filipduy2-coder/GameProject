package PlayerTest;

import CharacterSys.Player;
import InitializationSys.World;
import ItemSys.Item;
import ItemSys.ItemType;
import ItemSys.Recipe;
import QuestSys.QuestManager;
import ScriptSys.ScriptEngine;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCrafting {
    @Test
    void craftFailWhenRecipeDoesNotExist() {
        World world = new World();
        QuestManager questManager = new QuestManager();
        ScriptEngine scriptEngine = new ScriptEngine();
        Player player = new Player("TestUnit",100,10,world,questManager,scriptEngine);
        boolean result = player.craftItem("NO EXIST");
        assertFalse(result);
    }

    @Test
    void craftFailWhenIngradientsMissing() {
        World world = new World();
        QuestManager questManager = new QuestManager();
        ScriptEngine scriptEngine = new ScriptEngine();
        Player player = new Player("TestUnit",100,10,world,questManager,scriptEngine);
        Recipe recipe = new Recipe(
                "hammer",
                List.of("rock","wood"),
                new Item("hammer"));
        world.getRecipes().add(recipe);
        boolean result = player.craftItem("hammer");
        assertFalse(result);
    }

    @Test
    void craftSucceed() {
        World world = new World();
        QuestManager questManager = new QuestManager();
        ScriptEngine scriptEngine = new ScriptEngine();
        Player player = new Player("TestUnit",100,10,world,questManager,scriptEngine);
        Recipe recipe = new Recipe(
                "hammer",
                List.of("rock","wood"),
                new Item("hammer", "Hammer", ItemType.USABLE, new HashMap<>()));
        world.getRecipes().add(recipe);
        player.getInventory().addItem(new Item("rock"));
        player.getInventory().addItem(new Item("wood"));
        boolean result = player.craftItem("hammer");
        assertTrue(result);
        assertTrue(player.getInventory().hasItem("hammer"));
        assertFalse(player.getInventory().hasItem("wood"));
        assertFalse(player.getInventory().hasItem("rock"));
    }
}