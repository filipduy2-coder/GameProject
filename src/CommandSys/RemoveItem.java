package CommandSys;

import CharacterSys.Player;
import InitializationSys.ActionType;
import InitializationSys.Game;

public record RemoveItem(Player player, Game game) implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Remove Item <item>");
            return;
        }
        String itemName = String.join(" ", args);
        boolean success = player.dropItem(itemName);
        if (success) {
            game.setLastEvent(ActionType.DROP_ITEM, itemName.toLowerCase().trim());
            game.onActionResolved();
        }
    }

    @Override
    public String getName() {
        return "Remove";
    }
}
