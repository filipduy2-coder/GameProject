package CharacterSys;

import java.util.List;

public class NPC extends Character {
    private List<String> preActionDialogue;
    private List<String> postActionDialogue;
    private int dialogueIndex = 0;
    private boolean completedDialogue = false;
    private boolean hostile;
    private boolean startsCombat;
    public NPC(String name, int hp, int strength, boolean hostile, List<String> preActionDialogue, List<String> postActionDialogue,boolean startsCombat) {
        super(name, hp, strength);
        this.hostile = hostile;
        this.preActionDialogue = preActionDialogue;
        this.postActionDialogue = postActionDialogue;
        this.startsCombat = startsCombat;
    }
    public boolean hasPreActionDialogue() {
        return preActionDialogue != null && !preActionDialogue.isEmpty();
    }
    public List<String> getDialogue() {
        return isAlive() ? preActionDialogue : postActionDialogue;
    }
    public void clearPreActionDialogue() {
        preActionDialogue.clear();
    }
    public void clearPostActionDialogue() {
        postActionDialogue.clear();
    }
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
}
