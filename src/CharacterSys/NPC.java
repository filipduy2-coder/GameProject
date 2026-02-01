package CharacterSys;

import java.util.List;

public class NPC extends Character {
    private List<String> dialogue;

    public NPC(String name, int hp, int strength, boolean hostile, List<String> dialogue) {
        super(name, hp, strength, hostile);
        this.dialogue = dialogue;
    }
    public void talk() {}

    @Override
    public void attack(Character target) {

    }

    @Override
    public void takeDamage(int amount) {

    }
}
