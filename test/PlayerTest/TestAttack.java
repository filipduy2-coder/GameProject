package PlayerTest;

import CharacterSys.NPC;
import CharacterSys.Player;
import InitializationSys.Location;
import InitializationSys.World;
import QuestSys.QuestManager;
import ScriptSys.ScriptEngine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestAttack {
    @Test
    void attackFailsWhenNpcNotFound() {
        World world = new World();
        Location loc = new Location("arena");
        world.getLocations().add(loc);
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo("arena");
        assertFalse(p.attack("ghost"));
    }
    @Test
    void attackFailsWhenNpcNotHostile() {
        World world = new World();
        Location loc = new Location("arena");
        world.getLocations().add(loc);
        NPC npc = new NPC("villager", 20, 0);
        npc.setHostile(false);
        npc.setCompletedDialogue(true);
        loc.addNPC(npc);
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo("arena");
        assertFalse(p.attack("villager"));
    }
    @Test
    void attackFailsWhenDialogueNotCompleted() {
        World world = new World();
        Location loc = new Location("arena");
        world.getLocations().add(loc);
        NPC npc = new NPC("goblin", 20, 5);
        npc.setHostile(true);
        npc.setCompletedDialogue(false);
        loc.addNPC(npc);
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo("arena");
        assertFalse(p.attack("goblin"));
    }
    @Test
    void attackDamagesNpcButNpcSurvives() {
        World world = new World();
        Location loc = new Location("arena");
        world.getLocations().add(loc);
        NPC npc = new NPC("goblin", 50, 5);
        npc.setHostile(true);
        npc.setCompletedDialogue(true);
        loc.addNPC(npc);
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo("arena");
        assertTrue(p.attack("goblin"));
        assertEquals(40, npc.getHealth());
        assertTrue(npc.isAlive());
    }
    @Test
    void attackKillsNpc() {
        World world = new World();
        Location loc = new Location("arena");
        world.getLocations().add(loc);
        NPC npc = new NPC("goblin", 10, 5,true,new ArrayList<>(),new ArrayList<>(),true);
        npc.setCompletedDialogue(true);
        npc.getPostActionDialogue().add("You will pay for that");
        loc.addNPC(npc);
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo("arena");
        assertTrue(p.attack("goblin"));
        assertFalse(npc.isAlive());
        assertFalse(loc.getNpcs().contains(npc));
    }
    @Test
    void attackPlayerDies() {
        World world = new World();
        Location loc = new Location("arena");
        world.getLocations().add(loc);
        NPC npc = new NPC("goblin", 100, 200);
        npc.setHostile(true);
        npc.setCompletedDialogue(true);
        loc.addNPC(npc);
        Player p = new Player("Test", 100, 10, world, new QuestManager(), new ScriptEngine());
        p.forceMoveTo("arena");
        assertTrue(p.attack("goblin"));
        assertFalse(p.isAlive());
    }
}
