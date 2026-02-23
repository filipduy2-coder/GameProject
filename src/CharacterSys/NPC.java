package CharacterSys;

import java.util.List;

/**
 * Třída reprezentuje Npc ve hře.
 * Npc může být nepřatelská, může zahájit souboj, obsahuje dialog před
 * i po akci a reaguje na poškození. Také uchovává informaci o tom,
 * jestli zda hráč dokončil dialog před interakcí.
 *
 * Npc dědí základní vlastnosti z třídy Character
 *
 * @author Filip Quan
 */
public class NPC extends Character {
    private List<String> preActionDialogue;
    private List<String> postActionDialogue;
    private boolean completedDialogue = false;
    private boolean hostile;
    private boolean startsCombat;

    /**
     * Vytovoří Npc s kompletní konfigurací.
     *
     * @param name jméno Npc
     * @param hp počet životů
     * @param strength útočná síla
     * @param hostile zda je nepřátelská
     * @param preActionDialogue dialogy před akcí
     * @param postActionDialogue dialogy po akci
     * @param startsCombat zda Npc automaticky zahájí souboj
     */
    public NPC(String name, int hp, int strength, boolean hostile, List<String> preActionDialogue,
               List<String> postActionDialogue,boolean startsCombat) {
        super(name, hp, strength);
        this.hostile = hostile;
        this.preActionDialogue = preActionDialogue;
        this.postActionDialogue = postActionDialogue;
        this.startsCombat = startsCombat;
    }

    /**
     * Vytvoří jednoduché Npc bez dialogů, hostlility a zahájení souboj
     *
     * @param name jméno
     * @param hp počet životů
     * @param strength útoční síla
     */
    public NPC(String name, int hp, int strength) {
        super(name, hp, strength);
    }

    /**
     * Zjistí, zda NPC má dialogy před akcí.
     *
     * @return true, pokud existují dialogy před akcí, jinak false
     */
    public boolean hasPreActionDialogue() {
        return preActionDialogue != null && !preActionDialogue.isEmpty();
    }

    /**
     * Vrátí dialogy podle aktuálního stavu NPC.
     * Pokud je NPC naživu, vrací dialogy před akcí.
     * Pokud je mrtvá, vrací dialogy po akci.
     *
     * @return seznam dialogů odpovídající stavu NPC
     */
    public List<String> getDialogue() {
        return isAlive() ? preActionDialogue : postActionDialogue;
    }

    /**
     * Vymaže dialogy před akcí.
     */
    public void clearPreActionDialogue() {
        preActionDialogue.clear();
    }

    /**
     * Aplikuje poškození na NPC.
     * Pokud životy klesnou na nulu nebo méně, nastaví je na nulu.
     *
     * @param amount množství poškození
     */
    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if(health <= 0){
            health = 0;
        }
    }

    public boolean isStartsCombat() {
        return startsCombat;
    }

    public List<String> getPostActionDialogue() {
        return postActionDialogue;
    }
    public List<String> getPreActionDialogue() {
        return preActionDialogue;
    }
    public boolean isHostile() {
        return hostile;
    }

    public boolean isCompletedDialogue() {
        return completedDialogue;
    }
    public void setCompletedDialogue(boolean completedDialogue) {
        this.completedDialogue = completedDialogue;
    }
    public void setHostile(boolean hostile) {
        this.hostile = hostile;
    }
}
