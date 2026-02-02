package QuestSys;

import java.util.List;

    public class QuestLog {
        private List<Quest> activeQuests;
        private List<Quest> completedQuests;

        public void addQuest(Quest quest) {
            activeQuests.add(quest);
        }
        public void completeQuest(Quest quest) {
            activeQuests.remove(quest);
            completedQuests.add(quest);
        }
        public void printActiveQuests() {
            for (Quest quest : activeQuests) {
                System.out.println(quest);
            }
        }
        public void printCompletedQuests() {
            for (Quest quest : completedQuests) {
                System.out.println(quest);
            }
        }
    }
