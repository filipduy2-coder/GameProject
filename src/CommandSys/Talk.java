package CommandSys;

import CharacterSys.Player;
import InitializationSys.ActionType;
import InitializationSys.Game;
import InitializationSys.LocationNames;

public record Talk(Player player, Game game) implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Dialogue <npc>");
            return;
        }
        String npcName = String.join(" ", args);
        boolean success = player.talkTo(npcName);
        if (success) {
            game.setLastEvent(ActionType.TALK, npcName.toLowerCase().trim());
            game.onActionResolved();
            if (npcName.equalsIgnoreCase("lynne")) {
                player.forceMoveTo(LocationNames.BASEMENT);
            }
        }
    }


    @Override
    public String getName() {
        return "Talk";
    }
}
