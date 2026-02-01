package CommandSys;
import CharacterSys.Character;

public class Combat implements Command{
    private Character attacker;
    private Character target;

    public Combat(Character character, Character target) {
        this.attacker = character;
        this.target = target;
    }

    @Override
    public void execute() {
        attacker.attack(target);
    }

    @Override
    public String getName() {
        return "Attack";
    }
}
