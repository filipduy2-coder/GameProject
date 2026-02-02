package CommandSys;
import CharacterSys.Player;

public class Attack implements Command{
    private Player player;

    public Attack(Player player) {
        this.player = player;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Attack <npc>");
            return;
        }
        player.attack(args[0]);
    }
    @Override
    public String getName() {
        return "Attack";
    }
}
