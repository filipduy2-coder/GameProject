package CommandSys;

import CharacterSys.Player;

import java.util.ArrayList;
import java.util.List;

import static InitializationSys.GameState.*;

public class CommandManager {
    public List<Command> getCombatCommands(Player p) {
        List<Command> cmds =  List.of(
                new Attack(p),
                new UseItem(p),
                new ShowInventory(p),
                new ShowQuestLog(p),
                SystemCommand.hint(null),
                SystemCommand.exit()
        );
        List<Command> finalCmds = new ArrayList<>(cmds);
        finalCmds.add(SystemCommand.help(finalCmds));
        return finalCmds;
    }

    public List<Command> getNormalCommands(Player p) {
        List<Command> cmds =  List.of(
                new ShowInventory(p),
                new ShowQuestLog(p),
                new PickUp(p),
                new RemoveItem(p),
                new TalkTo(p),
                SystemCommand.hint(null),
                SystemCommand.exit()
        );
        List<Command> finalCmds = new ArrayList<>(cmds);
        finalCmds.add(SystemCommand.help(finalCmds));
        return finalCmds;
    }

    public List<Command> getTrappedCommands(Player p) {
        List<Command> cmds =  List.of(
                new CraftItem(p),
                new ShowInventory(p),
                new ShowQuestLog(p),
                new PickUp(p),
                new RemoveItem(p),
                SystemCommand.hint(null),
                SystemCommand.exit()
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
