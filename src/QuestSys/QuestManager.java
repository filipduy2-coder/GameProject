package QuestSys;

import CharacterSys.Player;
import InitializationSys.TriggerEvent;
import InitializationSys.World;
import ScriptSys.ScriptEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spravuje všechny questy ve hře. Umožňuje jejich načtení,
 * vyhledávání podle názvu, aktivaci na základě triggerů
 * a označení questů jako dokončené.
 *
 * Třída vyhodnocuje hráčovy akce a podle nich mění stav questů.
 */
public class QuestManager {
    private final Map<String, Quest> quests = new HashMap<>();

    /**
     * Vyhledá quest podle jeho názvu.
     *
     * @param name název questu
     * @return quest s daným názvem nebo null, pokud neexistuje
     */
    public Quest getQuestByName(String name) {
        return quests.get(name);
    }

    /**
     * Načte seznam questů do interní mapy. Každý quest je uložen
     * pod svým názvem jako klíčem.
     *
     * @param load seznam questů, které mají být zaregistrovány
     */
    public void loadQuests(List<Quest> load) {
        for (Quest quest : load) {
            quests.put(quest.getName(), quest);
        }
    }

    /**
     * Zkontroluje, zda daná událost aktivuje některý quest.
     * Pokud quest není aktivní a jeho trigger odpovídá události,
     * quest se aktivuje, přidá do QuestLogu hráče a vypíše se jeho popis.
     *
     * Speciální případ: pokud se aktivuje quest "Find a bone for Togo",
     * spustí se kostní úkol ve světě.
     *
     * @param event událost, která nastala
     * @param player hráč, jehož quest log se má aktualizovat
     * @param world svět, který může reagovat na aktivaci questu
     */
    public void checkTrigger(TriggerEvent event, Player player, World world) {
        for (Quest quest : quests.values()) {
            if (!quest.isActive()) {
                for (TriggerEvent trigger : quest.getTrigger()) {
                    if (matches(event, trigger)) {
                        quest.setActive(true);
                        quest.setCompleted(false);
                        player.getQuestLog().addQuest(quest);
                        System.out.println("New quest: " + quest.getName());
                        System.out.println("Detail: " + quest.getDescription());
                        if (quest.getName().equals("Find a bone for Togo")) {
                            world.activeBoneQuest();
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Zkontroluje, zda daná událost dokončuje některý aktivní quest.
     * Pokud quest splňuje podmínky dokončení, je označen jako dokončený,
     * přesunut do seznamu completed questů a vypíše se oznámení.
     *
     * Speciální případ: dokončení questu "Find a bone for Togo"
     * odstraní hlad společníka.
     *
     * @param event událost, která nastala
     * @param player hráč, jehož quest log se má aktualizovat
     * @param world svět, který může reagovat na dokončení questu
     * @param scriptEngine engine pro skriptované události (pokud je potřeba)
     */
    public void checkCompletion(TriggerEvent event, Player player, World world, ScriptEngine scriptEngine) {
        for (Quest quest : quests.values()) {
            if (quest.isActive() && !quest.isCompleted()) {
                for (TriggerEvent completion : quest.getCompletion()) {
                    if (matches(event, completion)) {
                        quest.setActive(false);
                        quest.setCompleted(true);
                        player.getQuestLog().completeQuest(quest);
                        System.out.println("Quest completed: " + quest.getName());
                        if (quest.getName().equalsIgnoreCase("Find a bone for Togo")) {
                            world.getCompanion().setHungry(false);
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Porovná dvě události a zjistí, zda mají stejný typ i argument.
     *
     * @param actual skutečná událost, která nastala
     * @param expected očekávaná událost z triggeru nebo completion
     * @return true, pokud události odpovídají
     */
    private boolean matches(TriggerEvent actual, TriggerEvent expected) {
        return actual.type() == expected.type() && actual.argument().equals(expected.argument());
    }
}
