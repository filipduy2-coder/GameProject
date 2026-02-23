package CommandSys;

import CharacterSys.Player;
import InitializationSys.ActionType;
import InitializationSys.Game;

public record UseItem(Player player, Game game) implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Use Item <item>");
            return;
        }
        String itemName = String.join(" ", args);
        boolean success = player.useItem(itemName, game);
        if (success) {
            game.setLastEvent(ActionType.USE_ITEM, itemName.toLowerCase().trim());
            game.onActionResolved();
        }
    }

    @Override
    public String getName() {
        return "Use";
    }
}
