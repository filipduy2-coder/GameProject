package InitializationSys;

import CharacterSys.Companion;
import CharacterSys.NPC;
import CharacterSys.Player;
import ScriptSys.ScriptEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Pomocná třída pro sestavení herního světa. Obsahuje metody pro vytvoření lesa,
 * propojení sousedních polí, umístění vstupu do akademie, inicializaci míst
 * pro kostní úkol a sestavení skriptovaných kroků NPC.
 *
 * Třída slouží jako inicializační fáze před začátkem hry.
 */
public class WorldBuilder {

    /**
     * Vytvoří celý herní svět. Inicializuje les, propojí jeho sousedy,
     * náhodně umístí vstup do akademie a připraví lokace pro kostní úkol.
     *
     * @param world svět, který má být sestaven
     */
    public static void buildWorld(World world) {
        initForest(world);
        connectForestNeighbours(world);
        placeAcademyEntryBlock(world);
        initializeBoneSpawnPool(world);
    }

    /**
     * Vytvoří 5×5 lesních lokací typu FOREST a uloží je do světa.
     * Každá lokace dostane unikátní ID ve formátu "forest_row_col".
     *
     * @param world svět, do kterého se lesní lokace uloží
     */
    public static void initForest(World world) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Location loc = new Location("forest_" + row + "_" + col,
                        "Dense forest",
                        new HashMap<>(),
                        LocationType.FOREST
                );
                world.getForest()[row][col] = loc;
                world.getLocations().add(loc);
            }
        }
    }

    /**
     * Propojí všechny lesní lokace se svými sousedy.
     * Každá lokace může mít sousedy na severu, jihu, západě a východě,
     * pokud existují v rámci 5×5 mřížky.
     *
     * @param world svět obsahující lesní mapu
     */
    public static void connectForestNeighbours(World world) {
        Location[][] forest = world.getForest();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Location loc = forest[row][col];
                if (row > 0)
                    loc.addNeighbour("N", forest[row - 1][col]);
                if (row < 4)
                    loc.addNeighbour("S", forest[row + 1][col]);
                if (col > 0)
                    loc.addNeighbour("W", forest[row][col - 1]);
                if (col < 4)
                    loc.addNeighbour("E", forest[row][col + 1]);
            }
        }
    }

    /**
     * Náhodně vybere jednu lesní lokaci (kromě startovní)
     * a označí ji jako vstup do akademie.
     *
     * @param world svět, ve kterém se má vstup nastavit
     */
    private static void placeAcademyEntryBlock(World world) {
        Random r = new Random();
        Location start = world.getStartingLocation();
        Location chosen;
        do {
            int row = r.nextInt(5);
            int col = r.nextInt(5);
            chosen = world.getForest()[row][col];
        } while (chosen == start);
        world.setAcademyEntryBlock(chosen);
    }

    /**
     * Vytvoří skriptované kroky pro pohyb hráče podle stavu světa a Npc.
     * Pokud Elian žije, vytvoří pevnou trasu přes akademii.
     * Pokud jsou splněny další podmínky, přidá dynamické kroky
     * podle aktuální pozice hráče a stavu společníka.
     *
     * @param world svět obsahující lokace a NPC
     * @param scriptEngine engine pro skriptované události
     * @param player hráč, jehož stav ovlivňuje skript
     * @param companion společník, jehož hlad ovlivňuje skript
     */
    public static void buildScript(World world, ScriptEngine scriptEngine, Player player, Companion companion) {
        NPC elian = world.getNPCByName("elian");
        if (elian != null && elian.isAlive()) {
            scriptEngine.addStep(LocationNames.CLASSROOM, LocationNames.LABORATORY);
            scriptEngine.addStep(LocationNames.LABORATORY, LocationNames.LIBRARY);
            scriptEngine.addStep(LocationNames.LIBRARY, LocationNames.ILLUSION_ROOM);
            scriptEngine.addStep(LocationNames.ILLUSION_ROOM, LocationNames.HEALING_ROOM);
            scriptEngine.addStep(LocationNames.HEALING_ROOM, LocationNames.STORAGE);
            scriptEngine.addStep(LocationNames.STORAGE, LocationNames.DORMITORY);
        }
        if (world.getLocationByName(LocationNames.DORMITORY).getNpcs().isEmpty()
                && !companion.isHungry()
                && player.getState() != GameState.TRAPPED) {
            scriptEngine.addStep(player.getCurrentLocation().getId(), LocationNames.LIBRARY);
        }
        if (player.isEscapedBasement() && !companion.isHungry()) {
            scriptEngine.addStep(player.getCurrentLocation().getId(), LocationNames.DORMITORY);
        }
    }

    /**
     * Inicializuje seznam lokací, ve kterých se může objevit kost.
     * Přidává pouze existující lokace z akademie. Pokud není nalezena
     * žádná platná lokace, vyvolá výjimku.
     *
     * @param world svět obsahující lokace akademie
     */
    private static void initializeBoneSpawnPool(World world) {
        List<Location> pool = world.getBoneLocation();
        addIfExists(pool, world, LocationNames.CLASSROOM);
        addIfExists(pool, world, LocationNames.LABORATORY);
        addIfExists(pool, world, LocationNames.LIBRARY);
        addIfExists(pool, world, LocationNames.ILLUSION_ROOM);
        addIfExists(pool, world, LocationNames.HEALING_ROOM);
        addIfExists(pool, world, LocationNames.STORAGE);
        if (pool.isEmpty()) {
            throw new IllegalArgumentException("No valid bone spawn locations found!");
        }
    }

    /**
     * Přidá lokaci do seznamu, pokud existuje ve světě.
     *
     * @param pool seznam lokací pro kostní úkol
     * @param world svět, ve kterém se lokace hledá
     * @param name název lokace
     */
    private static void addIfExists(List<Location> pool, World world, String name) {
        Location loc = world.getLocationByName(name);
        if (loc != null) {
            pool.add(loc);
        }
    }
}
