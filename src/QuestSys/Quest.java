package QuestSys;

import InitializationSys.TriggerEvent;

import java.util.List;

/**
 * Reprezentuje jeden quest ve hře.
 *
 * Quest může být aktivní nebo dokončený a jeho stav se mění
 * podle akcí hráče a vyhodnocení QuestManageru.
 */
public class Quest {
    private final String name;
    private final String description;
    private final List<TriggerEvent> trigger;
    private final List<TriggerEvent> completion;
    private boolean active = false;
    private boolean completed = false;

    /**
     * Vytvoří nový quest s názvem, popisem, aktivačními triggery
     * a podmínkami dokončení.
     *
     * @param name název questu
     * @param description popis úkolu
     * @param trigger seznam událostí, které quest aktivují
     * @param completion seznam událostí, které quest dokončí
     */
    public Quest(String name, String description, List<TriggerEvent> trigger, List<TriggerEvent> completion) {
        this.name = name;
        this.description = description;
        this.trigger = trigger;
        this.completion = completion;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getName() {
        return name;
    }

    public List<TriggerEvent> getTrigger() {
        return trigger;
    }


    public List<TriggerEvent> getCompletion() {
        return completion;
    }

    public String getDescription() {
        return description;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}