package InitializationSys;

import CharacterSys.Player;
import CommandSys.CommandEngine;
import CommandSys.CommandManager;
import QuestSys.QuestManager;
import Script.ScriptEngine;

import java.util.Scanner;

public class Game {
    World world;
    Player player;
    CommandManager commandManager;
    QuestManager questManager;
    private CommandEngine engine;
    private boolean running;
    private ScriptEngine scriptEngine = new ScriptEngine();

    public void start() {
        initGame();
        mainLoop();
        endGame();
    }

    public void initGame() {
        world = new World();
        JsonWorldLoader.load(world);
        WorldBuilder.buildWorld(world);
        questManager = new QuestManager();
        questManager.loadQuests(world.getQuests());
        player = new Player("Mia", 100, 25, world, questManager);
        WorldBuilder.buildScript(world, scriptEngine, player);
        player.setCurrentLocation(world.getStartingLocation());
        engine = new CommandEngine();
        commandManager = new CommandManager(this);
        commandManager.updateCommands(engine, player);
        running = true;
        System.out.println("Game initialized. You are in forest, you need to reach academy: ");
    }

    public void mainLoop() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            if (player.getState() != GameState.COMBAT) {
                System.out.println("You are in " + player.getCurrentLocation().getId());
                System.out.println(player.getCurrentLocation().getDescription());
                System.out.println("Items: " + player.getCurrentLocation().getAllItems());
                System.out.println("NPCs: " + player.getCurrentLocation().getAllNPCs());
            }
            engine.printMenu(engine.getCommandList());
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            processInput(input);
            world.printMapForest(player);
            commandManager.updateCommands(engine, player);
            System.out.println();
            if (!player.isAlive()) endGame();
            if (!world.getLocationByName("student dormitory").getNPCByName("Rovan").isAlive()) endGame();
            if (!running) break;
        }
        scanner.close();
    }

    public void processInput(String input) {
        if (input.trim().isEmpty()) {
            String loc = player.getCurrentLocation().getId();
            if (scriptEngine.canRunScript(loc)) {
                String next = scriptEngine.runScript(loc);
                player.forceMoveTo(next);
                onActionResolved();
                return;
            }
            System.out.println("Nothing happens.");
            onActionResolved();
            return;
        }
        engine.handleInput(input);
        onActionResolved();
    }

    private void onActionResolved() {
        Location loc = player.getCurrentLocation();
        if (player.getCurrentLocation() == world.getAcademyEntryBlock()) {
            System.out.println("You step into the academy.");
            player.forceMoveTo("Basic Magic Classroom");
        }
        if (player.getCurrentLocation().getId().equals("Abandoned Basement") && !player.isEscapedBasement()) {
            player.setState(GameState.TRAPPED);
        }
        questManager.checkTrigger("enter_location:" + loc.getId(),player);
    }

    public void endGame() {
        running = false;
        System.out.println("GAME OVER");
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
