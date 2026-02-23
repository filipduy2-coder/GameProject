package InitializationSys;

import CharacterSys.Player;
import CommandSys.CommandEngine;
import CommandSys.CommandManager;
import QuestSys.Quest;
import QuestSys.QuestManager;
import ScriptSys.ScriptEngine;

import java.util.Scanner;

/**
 * Hlavní třída hry. Spravuje svět, hráče, příkazy, questy,
 * skriptovací engine a hlavní herní smyčku.
 *
 * Zajišťuje inicializaci hry, zpracování vstupu hráče,
 * vyhodnocování akcí, aktualizaci questů a ukončení hry.
 */
public class Game {
    private World world;
    private Player player;
    private CommandManager commandManager;
    private QuestManager questManager;
    private CommandEngine engine;
    private boolean running;
    private ScriptEngine scriptEngine = new ScriptEngine();
    private TriggerEvent lastEvent;

    /**
     * Vytvoří instanci hry s předaným světem, hráčem a skriptovacím enginem.
     *
     * @param world herní svět
     * @param player hráč
     * @param scriptEngine engine pro skriptované kroky
     */
    public Game(World world, Player player, ScriptEngine scriptEngine) {
        this.world = world;
        this.player = player;
        this.scriptEngine = scriptEngine;
    }

    public Game() {}

    /**
     * Nastaví poslední událost, která se má vyhodnotit
     * v rámci questů a skriptů.
     *
     * @param type typ akce (GO, TALK, PICKUP, ...)
     * @param argument argument akce (např. název lokace)
     */
    public void setLastEvent(ActionType type, String argument) {
        this.lastEvent = new TriggerEvent(type, argument.toLowerCase());
    }

    public void setQuestManager(QuestManager questManager) {
        this.questManager = questManager;
    }

    /**
     * Spustí hru. Inicializuje svět, hráče, příkazy
     * a následně spustí hlavní herní smyčku.
     */
    public void start() {
        initGame();
        mainLoop();
        endGame();
    }

    /**
     * Inicializuje celý herní svět:
     * - vytvoří svět
     * - načte akademii z JSON
     * - vytvoří les
     * - načte questy
     * - vytvoří hráče
     * - nastaví příkazy
     *
     * Po inicializaci je hra připravena ke spuštění.
     */
    public void initGame() {
        world = new World();
        JsonWorldLoader.load(world);
        WorldBuilder.buildWorld(world);
        questManager = new QuestManager();
        questManager.loadQuests(world.getQuests());
        player = new Player("Mia", 100, 25, world, questManager, scriptEngine);
        player.setCurrentLocation(world.getStartingLocation());
        engine = new CommandEngine();
        commandManager = new CommandManager(this);
        commandManager.updateCommands(engine, player);
        running = true;
        System.out.println("Game initialized. You are in forest, you need to reach academy: ");
    }

    /**
     * Hlavní herní smyčka. Opakuje se, dokud je hra spuštěná.
     * Zobrazuje informace o lokaci, vypisuje mapu,
     * zobrazuje dostupné příkazy a zpracovává vstup hráče.
     *
     * Smyčka končí, pokud hráč zemře, Rovan zemře,
     * nebo pokud je hra ukončena příkazem.
     */
    public void mainLoop() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            // Pokud hráč není v souboji, vypíše se stav lokace
            if (player.getState() != GameState.COMBAT) {
                System.out.println("You are in " + player.getCurrentLocation().getId());
                if (player.getCurrentLocation().getType() == LocationType.ACADEMY) {
                    System.out.println(player.getCurrentLocation().getDescription());
                }
                System.out.println("Items: " + player.getCurrentLocation().getAllItems());
                System.out.println("NPCs: " + player.getCurrentLocation().getAllNPCs());
            }
            // Vypíše mapu lesa, pokud je hráč v lese
            world.printMapForest(player);
            engine.printMenu(engine.getCommandList());
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            processInput(input);
            commandManager.updateCommands(engine, player);
            System.out.println();
            // Pokud hráč zemřel, tak konec hry
            if (!player.isAlive()) endGame();
            // Pokud Rovan zemře, tak konec hry
            if (!world.getNPCByName("rovan").isAlive()) {
                endGame();
                break;
            }
            // Pokud hra byla ukončena příkazem, tak konec smyčky
            if (!running) break;
        }
        scanner.close();
    }

    /**
     * Zpracuje vstup hráče. Pokud je vstup prázdný,
     * pokusí se spustit skript v aktuální lokaci.
     * Jinak předá vstup CommandEngine.
     *
     * @param input textový vstup od hráče
     */
    public void processInput(String input) {
        if (input.trim().isEmpty()) {
            String current = player.getCurrentLocation().getId();
            if (scriptEngine.canRunScript(current)) {
                String next = scriptEngine.runScript(current);
                player.forceMoveTo(next);
                setLastEvent(ActionType.GO, next);
                onActionResolved();
                return;
            }
            System.out.println("Nothing happens.");
            return;
        }
        engine.handleInput(input);
    }

    /**
     * Vyhodnotí poslední akci hráče. Zpracuje:
     * - vstup do akademie
     * - aktivaci questů
     * - dokončení questů
     * - aktualizaci NPC
     * - skriptované kroky
     *
     * Po vyhodnocení se poslední událost resetuje.
     */
    public void onActionResolved() {
        if (lastEvent == null) return;
        if (player.getCurrentLocation() == world.getAcademyEntryBlock()) {
            System.out.println("You step into the academy.");
            player.forceMoveTo(LocationNames.CLASSROOM);
        }
        switch (lastEvent.type()) {
            case GO, TALK -> questManager.checkTrigger(lastEvent, player, world);
            case CRAFT_ITEM, USE_ITEM -> {
                questManager.checkCompletion(lastEvent, player, world, scriptEngine);
                questManager.checkTrigger(lastEvent, player, world);
            }
            case PICKUP -> questManager.checkCompletion(lastEvent, player, world, scriptEngine);
        }
        world.updateNPCState(player);
        // Speciální logika pro kostní quest
        Quest bone = questManager.getQuestByName("Find a bone for Togo");
        if (bone != null && bone.isCompleted()) {
            // Reset skriptů po dokončení kostního questu
            scriptEngine.clear();
            // Znovu sestaví skript podle aktuálního stavu světa, přejít k dalšímu segmentu
            WorldBuilder.buildScript(world, scriptEngine, player, world.getCompanion());
        }
        // Obecné sestavení skriptu (pro dynamické situace)
        WorldBuilder.buildScript(world, scriptEngine, player, world.getCompanion());
        lastEvent = null;
    }

    /**
     * Ukončí hru, pokud ještě běží, a vypíše hlášení GAME OVER.
     */
    public void endGame() {
        if (!running) return;
        running = false;
        System.out.println("GAME OVER");
    }
}
