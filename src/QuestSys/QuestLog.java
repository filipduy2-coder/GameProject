package QuestSys;

import java.util.ArrayList;
import java.util.List;

    public class QuestLog {
        private List<Quest> activeQuests = new ArrayList<>();
        private List<Quest> completedQuests = new ArrayList<>();

        public void addQuest(Quest quest) {
            activeQuests.add(quest);
        }
        public void completeQuest(Quest quest) {
            activeQuests.remove(quest);
            completedQuests.add(quest);
        }
        public void printActiveQuests() {
            System.out.println("Active quests:");
            for (Quest quest : activeQuests) {
                System.out.println(" - " + quest.getName());
            }
        }
        public void printCompletedQuests() {
            System.out.println("Completed quests:");
            for (Quest quest : completedQuests) {
                System.out.println(" - " + quest);
            }
        }
        public List<Quest> getActiveQuests() {
            return activeQuests;
        }
    }
