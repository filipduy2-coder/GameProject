package ScriptSys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Jednoduchý engine pro skriptované kroky. Umožňuje definovat,
 * z jaké místnosti má skript pokračovat do jiné, a zajišťuje,
 * že každý krok se provede pouze jednou.
 *
 * Slouží jako podpora pro příběhové přesuny hráče v akademii.
 */
public class ScriptEngine {
    private final Map<String, ScriptStep> steps = new HashMap<>();
    private final Set<String> usedSteps = new HashSet<>();

    /**
     * Přidá nový skriptovaný krok. Určuje, že pokud se hráč
     * nebo NPC nachází v místnosti {@code from}, skript povede
     * do místnosti {@code to}.
     *
     * @param from výchozí místnost
     * @param to cílová místnost
     */
    public void addStep(String from, String to) {
        steps.put(from, new ScriptStep(from, to));
    }

    /**
     * Zjistí, zda lze skript v dané místnosti spustit.
     * Skript lze spustit pouze tehdy, pokud:
     * - existuje krok pro danou místnost
     * - krok ještě nebyl použit
     *
     * @param roomId identifikátor místnosti
     * @return true, pokud lze skript spustit
     */
    public boolean canRunScript(String roomId) {
        return steps.containsKey(roomId) && !usedSteps.contains(roomId);
    }

    /**
     * Spustí skript pro danou místnost. Pokud krok existuje,
     * označí jej jako použitý a vrátí cílovou místnost.
     * Pokud krok neexistuje, vrací null.
     *
     * @param roomId místnost, ve které se skript vyhodnocuje
     * @return cílová místnost skriptu nebo null
     */
    public String runScript(String roomId) {
        ScriptStep step = steps.get(roomId);
        if (step == null) return null;
        usedSteps.add(roomId);
        return step.toRoom();
    }

    /**
     * Vymaže všechny skriptované kroky a resetuje historii použitých kroků.
     * Používá se při restartu hry nebo při změně příběhové logiky.
     */
    public void clear() {
        steps.clear();
        usedSteps.clear();
    }
}

