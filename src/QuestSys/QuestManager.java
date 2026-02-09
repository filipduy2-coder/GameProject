package QuestSys;

import CharacterSys.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestManager {
    private Map<String, Quest> quests = new HashMap<>();

    public void loadQuests(List<Quest> load) {
        for (Quest quest : load) {
            quests.put(quest.getName(), quest);
        }
    }
    public void checkTrigger(String event, Player player) {
        for (Quest quest : quests.values()) {
            if (!quest.isActive() && !quest.isCompleted()) {
                if (event.equals(quest.getTrigger())) {
                    quest.setActive(true);
                    player.getQuestLog().addQuest(quest);
                    System.out.println("New quest: " + quest.getName());
                }
            }
        }
    }
    public void checkCompletion(String event, Player player) {
        for (Quest quest : quests.values()) {
            if (quest.isActive() && !quest.isCompleted()) {
                if (event.equals(quest.getCompletion())) {
                    quest.setActive(false);
                    quest.setCompleted(true);
                    player.getQuestLog().completeQuest(quest);
                    System.out.println("Quest completed: " + quest.getName());
                }
            }
        }
    }
    public void activeQuestByName(String questName, Player player) {
        for (Quest quest : quests.values()) {
            if (quest.getName().equals(questName)) {
                quest.setActive(true);
                quest.setCompleted(false);
                player.getQuestLog().addQuest(quest);
                System.out.println("Quest activated: " + quest.getName());
            }
        }
    }
}
