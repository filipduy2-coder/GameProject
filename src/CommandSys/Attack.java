package CommandSys;
import CharacterSys.Player;
import InitializationSys.ActionType;
import InitializationSys.Game;

public record Attack(Player player, Game game) implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Attack <npc>");
            return;
        }
        String enemyName = String.join(" ", args);
        boolean success = player.attack(enemyName);
        if (success) {
            game.setLastEvent(ActionType.ATTACK, enemyName.toLowerCase().trim());
            game.onActionResolved();
        }
    }

    @Override
    public String getName() {
        return "Attack";
    }
}
