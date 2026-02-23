package CommandSys;

import CharacterSys.Player;
import InitializationSys.ActionType;
import InitializationSys.Game;

public record PickUp(Player player, Game game) implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Pick Up <item>");
            return;
        }
        String itemName = String.join(" ", args);
        boolean success = player.pickUp(itemName);
        if (success) {
            game.setLastEvent(ActionType.PICKUP, itemName.toLowerCase().trim());
            game.onActionResolved();
        }
    }

    @Override
    public String getName() {
        return "Pick";
    }
}
