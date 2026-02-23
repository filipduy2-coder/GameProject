package CommandSys;

import CharacterSys.Player;
import InitializationSys.ActionType;
import InitializationSys.Game;

public record Go(Player player, Game game) implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Go <target>");
            return;
        }
        String targetId = String.join(" ", args);
        boolean success = player.go(targetId);
        if (success) {
            game.setLastEvent(ActionType.GO, targetId.toLowerCase().trim());
            game.onActionResolved();
        }
    }

    @Override
    public String getName() {
        return "Go";
    }
}
