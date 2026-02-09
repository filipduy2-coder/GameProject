package CommandSys;

import CharacterSys.Player;
import InitializationSys.Game;

import java.util.ArrayList;
import java.util.List;


public class CommandManager {
    private final Game game;
    public CommandManager(Game game) {
        this.game = game;
    }
    public List<Command> getCombatCommands(Player p) {
        List<Command> cmds =  List.of(
                new Attack(p),
                new TalkTo(p),
                new UseItem(p),
                new ShowInventory(p),
                new ShowQuestLog(p),
                SystemCommand.hint(null),
                SystemCommand.exit(game)
        );
        List<Command> finalCmds = new ArrayList<>(cmds);
        finalCmds.add(SystemCommand.help(finalCmds));
        return finalCmds;
    }

    public List<Command> getNormalCommands(Player p) {
        List<Command> cmds =  List.of(
                new Go(p),
                new ShowInventory(p),
                new ShowQuestLog(p),
                new PickUp(p),
                new RemoveItem(p),
                new TalkTo(p),
                SystemCommand.hint(null),
                SystemCommand.exit(game)
        );
        List<Command> finalCmds = new ArrayList<>(cmds);
        finalCmds.add(SystemCommand.help(finalCmds));
        return finalCmds;
    }

    public List<Command> getTrappedCommands(Player p) {
        List<Command> cmds =  List.of(
                new CraftItem(p),
                new UseItem(p),
                new ShowInventory(p),
                new ShowQuestLog(p),
                new PickUp(p),
                new RemoveItem(p),
                SystemCommand.hint(null),
                SystemCommand.exit(game)
        );
        List<Command> finalCmds = new ArrayList<>(cmds);
        finalCmds.add(SystemCommand.help(finalCmds));
        return finalCmds;
    }

    public void updateCommands(CommandEngine commandEngine, Player player) {
        switch (player.getState()) {
            case COMBAT -> commandEngine.setCommands(getCombatCommands(player));
            case NORMAL -> commandEngine.setCommands(getNormalCommands(player));
            case TRAPPED -> commandEngine.setCommands(getTrappedCommands(player));
        }
    }
}
