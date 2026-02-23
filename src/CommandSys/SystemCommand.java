package CommandSys;

import InitializationSys.Game;

import java.util.List;
import java.util.Objects;

/**
 * Systémový příkaz, který reprezentuje tři speciální akce:
 * EXIT (ukončení hry), HELP (výpis dostupných příkazů)
 * a HINT (zobrazení nápovědy).
 *
 * Každý typ příkazu používá jiné parametry – některé potřebují hru,
 * jiné seznam příkazů nebo text nápovědy.
 *
 * @author Filip
 */
public record SystemCommand(Type type, String hint, List<Command> commands, Game game) implements Command {
    public enum Type {EXIT, HELP, HINT}

    /**
     * Vytvoří systémový příkaz pro ukončení hry.
     */
    public static SystemCommand exit(Game game) {
        return new SystemCommand(Type.EXIT, null, null, game);
    }

    /**
     * Vytvoří systémový příkaz pro výpis dostupných příkazů.
     */
    public static SystemCommand help(List<Command> commands) {
        return new SystemCommand(Type.HELP, null, commands, null);
    }

    /**
     * Vytvoří systémový příkaz pro zobrazení nápovědy.
     */
    public static SystemCommand hint(String hint) {
        return new SystemCommand(Type.HINT, hint, null, null);
    }

    @Override
    public void execute(String[] args) {
        // Rozhodne, jaký typ systémového příkazu se má vykonat
        switch (type) {
            case EXIT -> {
                // Pokud je k dispozici instance hry, ukončí ji
                if (game != null) {
                    game.endGame();
                }
            }
            case HELP -> {
                // Vypíše seznam dostupných příkazů
                System.out.println("Available commands:");
                // Rozdělí seznam příkazů na dvě poloviny pro dvousloupcový výpis
                int half = (commands.size() + 1) / 2;
                for (int i = 0; i < half; i++) {
                    String left = (i + 1) + ") " + commands.get(i).getName();
                    String right = "";
                    if (i + half < commands.size()) {
                        right = (i + half + 1) + ") " + commands.get(i + half).getName();
                    }
                    System.out.printf("%-25s %s%n", left, right);
                }
            }
            case HINT -> {
                // Vypíše nápovědu, nebo informaci, že není dostupná
                System.out.println(Objects.requireNonNullElse(hint, "No current hint is available"));
            }
        }
    }

    @Override
    public String getName() {
        // Vrací název příkazu podle jeho typu
        return switch (type) {
            case EXIT -> "Exit";
            case HELP -> "Help";
            case HINT -> "Hint";
        };
    }
}
