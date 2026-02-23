package CommandSys;

import java.util.*;

/**
 * Třída zpracovává textové příkazy hráče. Uchovává registrované příkazy,
 * vyhledává je podle názvu, předává jim argumenty a umožňuje vypsat nabídku
 * dostupných příkazů.
 *
 * Slouží jako centrální rozhraní mezi vstupem hráče a logikou jednotlivých příkazů.
 *
 * @author Filip Quan
 */
public class CommandEngine {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Nastaví seznam dostupných příkazů. Původní příkazy jsou odstraněny.
     * Každý příkaz je uložen pod svým názvem převedeným na malá písmena.
     *
     * @param cmds seznam příkazů, které mají být registrovány
     */
    public void setCommands(List<Command> cmds) {
        commands.clear();
        for (Command c : cmds) {
            commands.put(c.getName().toLowerCase(), c);
        }
    }
    public List<Command> getCommandList() {
        return new ArrayList<>(commands.values());
    }

    /**
     * Zpracuje vstup od hráče, rozdělí jej na název příkazu a argumenty
     * a pokusí se najít odpovídající příkaz.
     *
     * Podporuje také dvouslovné příkazy (např. "pick up").
     * Pokud příkaz neexistuje, vypíše chybovou hlášku.
     * Pokud existuje, zavolá jeho metodu execute().
     *
     * @param input textový vstup od hráče
     */
    public void handleInput(String input) {
        // Odstraní přebytečné mezery a rozdělí vstup na jednotlivá slova
        String[] parts = input.trim().split("\\s+");
        // První slovo je název příkazu (např. "go", "pick", "attack")
        String commandName = parts[0].toLowerCase();
        String[] args =  Arrays.copyOfRange(parts, 1, parts.length); // Zbytek slov jsou argumenty příkazu
        Command cmd = commands.get(commandName); // Pokusí se najít příkaz podle prvního slova
        // Pokud příkaz neexistuje a hráč zadal alespoň dvě slova,
        if (cmd == null && parts.length > 1) {
            String twoWord = (parts[0] + " " + parts[1]).toLowerCase(); // Složí první dvě slova do jednoho názvu
            cmd = commands.get(twoWord); // zkusí se najít dvouslovný příkaz
            // Pokud existuje, argumenty začínají až od třetího slova
            if (cmd != null) {
                args = Arrays.copyOfRange(parts, 2, parts.length);
            }
        }
        // Pokud příkaz stále neexistuje, vypíše chyba, jinak zavolá execute()
        if (cmd == null) {
            System.out.println("Command <" + input + "> not found");
        } else {
            cmd.execute(args);
        }
    }

    /**
     * Vypíše nabídku vybraných příkazů, které mají být hráči zobrazeny.
     * Zobrazuje pouze základní příkazy jako help, exit, hint a go.
     *
     * @param commands seznam příkazů, ze kterých se mají vypsat dostupné položky
     */
    public void printMenu(List<Command> commands) {
        System.out.println("Choose one of the following commands:");
        for (Command c : commands) {
            String name = c.getName().toLowerCase();
            if (name.equals("help") || name.equals("exit") || name.equals("hint") || name.equals("go")) {
                System.out.println(" - " + c.getName());
            }
        }
    }
}
