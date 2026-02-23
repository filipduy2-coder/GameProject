package CommandSys;

import CharacterSys.Player;
import InitializationSys.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Spravuje seznam příkazů dostupných hráči podle jeho aktuálního stavu.
 * Podle toho, zda je hráč v normálním režimu, v souboji nebo uvězněný,
 * vrací odpovídající sadu příkazů a aktualizuje CommandEngine.
 *
 * @author Filip Quan
 */
public record CommandManager(Game game) {

    /**
     * Vrátí seznam příkazů, které může hráč používat během souboje.
     *
     * @param p hráč
     * @return seznam bojových příkazů
     */
    public List<Command> getCombatCommands(Player p) {
        List<Command> cmds = List.of(
                new Attack(p, game),
                new Talk(p, game),
                new UseItem(p, game),
                new ShowInventory(p),
                new ShowQuestLog(p),
                SystemCommand.hint(null),
                SystemCommand.exit(game)
        );
        List<Command> finalCmds = new ArrayList<>(cmds);
        finalCmds.add(SystemCommand.help(finalCmds));
        return finalCmds;
    }

    /**
     * Vrátí seznam příkazů, které může hráč používat v normálním režimu.
     *
     * @param p hráč
     * @return seznam příkazů pro normální stav
     */
    public List<Command> getNormalCommands(Player p) {
        List<Command> cmds = List.of(
                new Go(p, game),
                new Attack(p, game),
                new ShowInventory(p),
                new ShowQuestLog(p),
                new PickUp(p, game),
                new RemoveItem(p, game),
                new Talk(p, game),
                SystemCommand.hint(null),
                SystemCommand.exit(game)
        );
        List<Command> finalCmds = new ArrayList<>(cmds);
        finalCmds.add(SystemCommand.help(finalCmds));
        return finalCmds;
    }

    /**
     * Vrátí seznam příkazů dostupných, když je hráč uvězněný (TRAPPED).
     * V pasti nemůže útočit ani se pohybovat, ale může vyrábět předměty,
     * používat předměty, spravovat inventář a úkoly.
     *
     * @param p hráč
     * @return seznam příkazů pro stav TRAPPED
     */
    public List<Command> getTrappedCommands(Player p) {
        List<Command> cmds = List.of(
                new CraftItem(p, game),
                new UseItem(p, game),
                new ShowInventory(p),
                new ShowQuestLog(p),
                new PickUp(p, game),
                new RemoveItem(p, game),
                SystemCommand.hint(null),
                SystemCommand.exit(game)
        );
        List<Command> finalCmds = new ArrayList<>(cmds);
        finalCmds.add(SystemCommand.help(finalCmds));
        return finalCmds;
    }

    /**
     * Aktualizuje příkazy v CommandEngine podle aktuálního stavu hráče.
     * Pokud je hráč v souboji, nastaví bojové příkazy.
     * Pokud je ve sklepě, nastaví příkazy pro TRAPPED.
     * Jinak nastaví běžné příkazy.
     *
     * @param commandEngine engine, který zpracovává příkazy
     * @param player hráč, jehož stav určuje dostupné příkazy
     */
    public void updateCommands(CommandEngine commandEngine, Player player) {
        switch (player.getState()) {
            case COMBAT -> commandEngine.setCommands(getCombatCommands(player));
            case NORMAL -> commandEngine.setCommands(getNormalCommands(player));
            case TRAPPED -> commandEngine.setCommands(getTrappedCommands(player));
        }
    }
}
