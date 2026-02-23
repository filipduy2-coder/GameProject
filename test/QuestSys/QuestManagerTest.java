package QuestSys;

import CharacterSys.Player;
import InitializationSys.ActionType;
import InitializationSys.TriggerEvent;
import InitializationSys.World;
import ScriptSys.ScriptEngine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestManagerTest {
    @Test
    void questActivateOnCorrectTrigger() {
        World world = new World();
        QuestManager qm = new QuestManager();
        ScriptEngine se = new ScriptEngine();
        Player player = new Player("Test", 100, 10, world, qm, se);
        Quest quest = new Quest(
                "FindBone",
                "Find a bone for Togo",
                List.of(new TriggerEvent(ActionType.GO, "forest")),
                List.of(new TriggerEvent(ActionType.PICKUP, "bone"))
        );
        qm.loadQuests(List.of(quest));
        TriggerEvent event = new TriggerEvent(ActionType.GO, "forest");
        qm.checkTrigger(event, player, world);
        assertTrue(quest.isActive());
        assertFalse(quest.isCompleted());
        assertTrue(player.getQuestLog().getActiveQuests().contains(quest));
    }

    @Test
    void questDoesNotActivateOnIncorrectTrigger() {
        World world = new World();
        QuestManager qm = new QuestManager();
        ScriptEngine se = new ScriptEngine();
        Player player = new Player("Test", 100, 10, world, qm, se);
        Quest quest = new Quest(
                "FindBone",
                "Find a bone for Togo",
                List.of(new TriggerEvent(ActionType.GO, "forest")),
                List.of(new TriggerEvent(ActionType.PICKUP, "bone"))
        );
        qm.loadQuests(List.of(quest));
        TriggerEvent event = new TriggerEvent(ActionType.GO, "academy");
        qm.checkTrigger(event, player, world);
        assertFalse(quest.isActive());
    }

    @Test
    void questCompletesOnCorrectTrigger() {
        World world = new World();
        QuestManager qm = new QuestManager();
        ScriptEngine se = new ScriptEngine();
        Player player = new Player("Test", 100, 10, world, qm, se);
        Quest quest = new Quest(
                "FindBone",
                "Find a bone for Togo",
                List.of(new TriggerEvent(ActionType.GO, "forest")),
                List.of(new TriggerEvent(ActionType.PICKUP, "bone"))
        );
        qm.loadQuests(List.of(quest));
        qm.checkTrigger(new TriggerEvent(ActionType.GO, "forest"), player, world);
        qm.checkCompletion(new TriggerEvent(ActionType.PICKUP, "bone"), player, world, se);
        assertFalse(quest.isActive());
        assertTrue(quest.isCompleted());
        assertTrue(player.getQuestLog().getCompletedQuests().contains(quest));
    }

    @Test
    void questDoesNotCompletesOnIncorrectTrigger() {
        World world = new World();
        QuestManager qm = new QuestManager();
        ScriptEngine se = new ScriptEngine();
        Player player = new Player("Test", 100, 10, world, qm, se);
        Quest quest = new Quest(
                "FindBone",
                "Find a bone for Togo",
                List.of(new TriggerEvent(ActionType.GO, "forest")),
                List.of(new TriggerEvent(ActionType.PICKUP, "bone"))
        );
        qm.loadQuests(List.of(quest));
        qm.checkTrigger(new TriggerEvent(ActionType.GO, "forest"), player, world);
        qm.checkCompletion(new TriggerEvent(ActionType.PICKUP, "sand"), player, world, se);
        assertTrue(quest.isActive());
        assertFalse(quest.isCompleted());
        assertFalse(player.getQuestLog().getCompletedQuests().contains(quest));
    }
}