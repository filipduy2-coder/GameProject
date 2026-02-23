package CommandSys;

import CharacterSys.Player;
import InitializationSys.ActionType;
import InitializationSys.Game;

public record CraftItem(Player player, Game game) implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Craft <item>");
            return;
        }
        String itemName = String.join(" ", args);
        boolean success = player.craftItem(itemName);
        if (success) {
            game.setLastEvent(ActionType.CRAFT_ITEM, itemName.toLowerCase().trim());
            game.onActionResolved();
        }
    }


    @Override
    public String getName() {
        return "Craft";
    }
}
