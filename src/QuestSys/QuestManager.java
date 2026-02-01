package QuestSys;

import java.util.HashMap;
import java.util.Map;

public class QuestManager {
    private Map<String, Quest> quests = new HashMap<>();

    public void addQuest(Quest quest) {
        quests.put(quest.getName(), quest);
    }

    public Quest getQuest(String questName) {
        return quests.get(questName);
    }

    public boolean hasQuest(String questName) {
        return quests.containsKey(questName);
    }

    public void completeQuest(String questName) {
        Quest  quest = quests.get(questName);
        if (quest != null) {
            quest.complete();
        }
    }
    public Map<String, Quest> getAllQuests() {
        return quests;
    }

}
