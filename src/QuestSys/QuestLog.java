package QuestSys;

import java.util.ArrayList;
import java.util.List;

/**
 * Uchovává seznam aktivních a dokončených questů hráče.
 * Umožňuje přidávání nových questů, označení questů jako dokončené
 * a výpis obou seznamů. Slouží jako deník hráče.
 */
public class QuestLog {
    private final List<Quest> activeQuests = new ArrayList<>();
    private final List<Quest> completedQuests = new ArrayList<>();

    /**
     * Přidá quest do seznamu aktivních questů.
     * Pokud byl quest dříve dokončen, je nejprve odstraněn
     * ze seznamu dokončených.
     *
     * @param quest quest, který má být aktivován
     */
    public void addQuest(Quest quest) {
        completedQuests.remove(quest);
        activeQuests.add(quest);
    }

    /**
     * Označí quest jako dokončený. Quest je odstraněn
     * ze seznamu aktivních a přesunut do seznamu dokončených.
     *
     * @param quest quest, který má být dokončen
     */
    public void completeQuest(Quest quest) {
        activeQuests.remove(quest);
        completedQuests.add(quest);
    }

    /**
     * Vypíše všechny aktivní questy do konzole.
     * Každý quest je vypsán pouze názvem.
     */
    public void printActiveQuests() {
        System.out.println("Active quests:");
        for (Quest quest : activeQuests) {
            System.out.println(" - " + quest.getName());
        }
    }

    /**
     * Vypíše všechny dokončené questy do konzole.
     * Každý quest je vypsán pouze názvem.
     */
    public void printCompletedQuests() {
        System.out.println("Completed quests:");
        for (Quest quest : completedQuests) {
            System.out.println(" - " + quest.getName());
        }
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }

    public List<Quest> getCompletedQuests() {
        return completedQuests;
    }
}
