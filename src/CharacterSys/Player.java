package CharacterSys;

import InitializationSys.*;
import ItemSys.Item;
import ItemSys.ItemType;
import ItemSys.Recipe;
import QuestSys.QuestLog;
import QuestSys.QuestManager;
import ScriptSys.ScriptEngine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Třída reprezentuje hráče ve hře. Uchovává jeho statistiky, inventář,
 * aktuální lokaci, stav hry, seznam navštívených míst a správu úkolů.
 * Hráč se může pohybovat, bojovat, sbírat a používat předměty,
 * komunikovat s NPC a vyrábět nové předměty podle receptů.
 *
 * @author Filip Quan
 */
public class Player extends Character {
    private final int baseHealth;
    private final int baseStrength;
    private final Inventory inventory;
    private Location currentLocation;
    private final Set<Location> visitedLocations;
    private final QuestLog questLog;
    private final QuestManager questManager;
    private GameState state = GameState.NORMAL;
    private final World world;
    private boolean escapedBasement;
    private final ScriptEngine scriptEngine;

    /**
     * Vytvoří nového hráče s počátečními statistikami, inventářem,
     * odkazem na svět, správce úkolů a skriptovací engine.
     *
     * @param name         jméno hráče
     * @param hp           počáteční zdraví
     * @param strength     počáteční síla útoku
     * @param world        herní svět
     * @param questManager správce úkolů
     * @param scriptEngine engine pro spouštění skriptovaných událostí
     */
    public Player(String name, int hp, int strength, World world, QuestManager questManager, ScriptEngine scriptEngine) {
        super(name, hp, strength);
        this.baseHealth = hp;
        this.baseStrength = strength;
        this.inventory = new Inventory(new ArrayList<>(), 3);
        this.visitedLocations = new HashSet<>();
        this.questLog = new QuestLog();
        this.escapedBasement = false;
        this.world = world;
        this.questManager = questManager;
        this.scriptEngine = scriptEngine;
    }

    /**
     * Pokusí se přesunout hráče do jiné lokace.
     * Ve FOREST se používají směry N, S, W, E.
     * V ACADEMY lze vstoupit pouze do již navštívených míst.
     *
     * @param input směr nebo název místnosti
     * @return true, pokud byl pohyb úspěšný, jinak false
     */
    public boolean go(String input) {
        LocationType type = currentLocation.getType();
        switch (type) {
            case FOREST -> {
                input = input.toUpperCase();
                // POKUD ZADÁ NEPLATNÝ INPUT
                if (!input.equals("N") && !input.equals("S") && !input.equals("W") && !input.equals("E")) {
                    System.out.println("Invalid direction. Use N, S, W or E");
                    return false;
                }
                // POKUD JE NA OKRAJI LESA
                Location next = currentLocation.getNeighbours().get(input);
                if (next == null) {
                    System.out.println("You can't go that way");
                    return false;
                }
                currentLocation = next;
                System.out.println("You moved to " + next.getId());
                return true;
            }
            case ACADEMY -> {
                input = input.toLowerCase();
                Location next = world.getLocationByName(input);
                // POKUD ZADÁ NEEXISTUJÍCÍ NÁZEV LOKACE
                if (next == null || next.getType() != LocationType.ACADEMY) {
                    System.out.println("No such room in the academy");
                    return false;
                }
                // POKUD HRÁČ NA TOMTO LOKACI NEBYL ANI JEDNOU
                if (!visitedLocations.contains(next)) {
                    System.out.println("You haven't visited this room yet.");
                    return false;
                }
                // ZABRÁNÍ HRÁČI VSTUPU DO SKLEPY, POKUD JIŽ UNIKL
                if (next.getId().equalsIgnoreCase(LocationNames.BASEMENT) && escapedBasement) {
                    System.out.println("You can't return to the basement. The entrance collapsed");
                    return false;
                }
                currentLocation = next;
                System.out.println("You entered " + next.getId());
                return true;
            }
        }
        return false;
    }

    /**
     * Zahájí rozhovor s NPC v aktuální lokaci.
     * Zobrazí dialogy před akcí nebo běžné dialogy a případně spustí souboj,
     * když npc má vlastonost zahájení souboje.
     *
     * @param nameNPC jméno NPC
     * @return true, pokud rozhovor proběhl, jinak false
     */
    public boolean talkTo(String nameNPC) {
        String name = nameNPC.toLowerCase();
        NPC npc = currentLocation.getNPCByName(name);
        // POKUD JMÉNO POSTAVY NEEXISTUJE
        if (npc == null) {
            System.out.println("There is no one named " + nameNPC + " here.");
            return false;
        }
        // POKUD JMÉNO POSTAVY JE LYNNE A NEDOKONČILA DIALOG
        if (npc.getName().equalsIgnoreCase("lynne") && npc.hasPreActionDialogue()) {
            for (String s : npc.getPreActionDialogue()) {
                System.out.println(npc.getName() + ':' + s);
            }
            npc.clearPreActionDialogue();
            return true;
        }
        // POKUD DIALOG POSTAVY JE NEDOKOČEN, VYPÍŠE DIALOG, JINAK NE
        if (npc.isCompletedDialogue()) {
            System.out.println(npc.getName() + " has nothing to say.");
        } else {
            for (String s : npc.getDialogue()) {
                System.out.println(npc.getName() + ':' + s);
                npc.setCompletedDialogue(true);
            }
        }
        // POKUD NPC MÁ VLASTNOST ZAHÁJENÍ SOUBOJ, NASTAVÍ STAV HRÁČE NA COMBAT
        if (npc.isStartsCombat()) {
            enterCombat();
        }
        return true;
    }

    /**
     * Pokusí se použít předmět z inventáře.
     * Pokud je předmět použitelný, aplikuje jeho efekt a odstraní jej z inventáře.
     *
     * @param nameItem název předmětu
     * @param game     instance hry pro vyhodnocení událostí
     * @return true, pokud byl předmět úspěšně použit, jinak false
     */
    public boolean useItem(String nameItem, Game game) {
        String name = nameItem.toLowerCase();
        Item item = inventory.getItemByName(name);
        // POKUD V INVENTÁŘE NEOBSAHUJE ZADANÝ ITEM
        if (item == null) {
            System.out.println("You don't have that item.");
            return false;
        }
        // POKUD ZADANÝ ITEM NENÍ PRO DANÝ TYP VHODNÁ
        if (item.getType() != ItemType.USABLE) {
            System.out.println("You can't use this item.");
            return false;
        }
        System.out.println("You used: " + item.getName());
        boolean used = item.applyUsableEffect(this, world, game);
        if (used) {
            inventory.removeItem(item);
        }
        return used;
    }

    /**
     * Pokusí se zvednout předmět z aktuální lokace.
     * Zkontroluje kapacitu inventáře a speciální chování některých předmětů.
     *
     * @param itemName název předmětu
     * @return true, pokud byl předmět zvednut, jinak false
     */
    public boolean pickUp(String itemName) {
        String name = itemName.toLowerCase();
        Item item = currentLocation.getItemByName(name);
        // POKUD ZADANÝ ITEM V AKTUÁLNÍ MÍSTNOSTI NEEXISTUJE
        if (item == null) {
            System.out.println("No such item here.");
            return false;
        }
        // POKUD INVENTÁŘ HRÁČE JE PLNÝ
        if (inventory.isFull()) {
            System.out.println("Your inventory is full.");
            return false;
        }
        // POKUD JE ZADANÝM ITEMEM KOST, NEBUDE PŘIDÁNA DO INVENTÁŘE A BUDE ODSTRANĚNA ZE SVÉHO AKTUÁLNÍHO UMIŠTĚNÍ.
        if (item.getName().equals("bone") && currentLocation == world.getCurrentBoneLocation()) {
            currentLocation.removeItem(name);
            System.out.println("Togo happily takes the bone from your hands");
            return true;
        }
        inventory.addItem(item);
        currentLocation.removeItem(name);
        System.out.println("You picked up: " + item.getName());
        return true;
    }

    /**
     * Odloží item z inventáře do aktuální lokace.
     *
     * @param itemName název itemu
     * @return true, pokud byl item odložen, jinak false
     */
    public boolean dropItem(String itemName) {
        String name = itemName.toLowerCase();
        Item item = inventory.getItemByName(name);
        if (item == null) {
            System.out.println("You don't have that item.");
            return false;
        }
        inventory.removeItem(item);
        currentLocation.addItem(item);
        System.out.println("You dropped: " + item.getName());
        return true;
    }

    /**
     * Vypíše obsah inventáře a počet volných slotů.
     */
    public void showInventory() {
        System.out.println("Inventory:");
        inventory.printInventory();
        System.out.println("Free slots: " + inventory.getFreeSlots());
    }

    /**
     * Vypíše aktivní a dokončené úkoly hráče.
     */
    public void showQuestLog() {
        questLog.printActiveQuests();
        questLog.printCompletedQuests();
    }

    /**
     * Pokusí se vycraftit item podle receptu.
     * Zkontroluje dostupnost ingrediencí a vytvoří výsledný item.
     *
     * @param nameRecipe název receptu
     * @return true, pokud byl item úspěšně vyroben, jinak false
     */
    public boolean craftItem(String nameRecipe) {
        Recipe recipe = world.getRecipeByName(nameRecipe.toLowerCase());
        if (recipe == null) {
            System.out.println("No such recipe.");
            return false;
        }
        List<String> missing = new ArrayList<>();
        for (String ing : recipe.ingredientNames()) {
            if (!inventory.hasItem(ing)) {
                missing.add(ing);
            }
        }
        if (!missing.isEmpty()) {
            System.out.println("Missing ingredients: " + String.join(", ", missing));
            return false;
        }
        for (String ing : recipe.ingredientNames()) {
            inventory.removeItem(inventory.getItemByName(ing));
        }
        inventory.addItem(recipe.createResult());
        System.out.println("You crafted: " + recipe.result().getName());
        return true;
    }

    /**
     * Provede útok na NPC v aktuální lokaci.
     * Zohledňuje dialog, nepřátelství, zranění, smrt NPC i hráče
     * a vyhodnocuje úkoly a skriptované události.
     *
     * @param nameNPC jméno NPC
     * @return true, pokud útok proběhl (úspěšně nebo hráč zemřel), jinak false
     */
    public boolean attack(String nameNPC) {
        String targetName = nameNPC.toLowerCase();
        NPC target = currentLocation.getNPCByName(targetName);
        if (target == null) {
            System.out.println("No such enemy here.");
            return false;
        }
        if (!target.isHostile()) {
            System.out.println(target.getName() + " is not hostile.");
            return false;
        }
        if (!target.isCompletedDialogue()) {
            System.out.println("You need to complete dialogue first.");
            return false;
        }
        System.out.println("You attacked: " + target.getName() + " for " + strength + " damage!");
        target.takeDamage(strength);
        if (!target.isAlive()) {
            System.out.println("You defeated: " + target.getName() + '!');
            TriggerEvent defeatEvent = new TriggerEvent(ActionType.ATTACK, target.getName().toLowerCase());
            questManager.checkCompletion(defeatEvent, this, world, scriptEngine); //Zkontroluje, zda jsou po porážce nepřítele splněny nějaké úkoly.
            for (String s : target.getDialogue()) {
                System.out.println(target.getName() + ':' + s); // VYPÍŠE DIALOG NPC PO PORÁŽCE
            }
            target.setCompletedDialogue(true);
            resetCombatEffect();
            leaveCombat();
            questManager.checkTrigger(defeatEvent, this, world);
            currentLocation.removeNPC(target);
            return true;
        }
        System.out.println(target.getName() + " attacks you for " + target.getStrength() + " damage!");
        this.takeDamage(target.getStrength());
        for (Item item : inventory.items()) {
            if (item.getType() == ItemType.AUTO) {
                item.applyAutoEffect(this); // Zkontroluje, zda je automatický aktivovaný nějaký item z inventáře
            }
        }
        System.out.println("Player health: " + this.health);
        System.out.println("Enemy health: " + target.getHealth());
        if (!this.isAlive()) {
            if (targetName.equals("rovan")) {
                System.out.println("Togo sacrificed himself to save you from death.");
                System.out.println("Your anger ignited a strength you didn’t know you had, and you defeated the enemy.");
            } else {
                System.out.println("You died!");
            }
            return true;
        }
        enterCombat();
        return true;
    }

    /**
     * Přesune hráče do zadané lokace bez kontrol (používá se pro skripty).
     * Zaznamená lokaci jako navštívenou a aktivuje stav TRAPPED, pokud jde o sklep.
     *
     * @param locationName název lokace
     */
    public void forceMoveTo(String locationName) {
        Location next = world.getLocationByName(locationName.toLowerCase());
        if (next == null) {
            System.out.println("Error: location does not exist");
            return;
        }
        if (next == world.getLocationByName(LocationNames.BASEMENT)) {
            enterTrapped();
        }
        visitedLocations.add(currentLocation);
        visitedLocations.add(next);
        currentLocation = next;
    }

    /**
     * Obnoví hráčovo zdraví a sílu na základní hodnoty.
     */
    public void resetCombatEffect() {
        health = baseHealth;
        strength = baseStrength;
    }

    /**
     * Nastaví stav hráče na COMBAT a oznámí začátek souboje.
     */
    public void enterCombat() {
        this.state = GameState.COMBAT;
        System.out.println();
        System.out.println("You're combat!");
    }

    /**
     * Ukončí souboj a nastaví stav hráče na NORMAL.
     */
    public void leaveCombat() {
        this.state = GameState.NORMAL;
        System.out.println("Combat ended!");
    }

    /**
     * Nastaví stav hráče na TRAPPED (uvězněn ve sklepě).
     */
    public void enterTrapped() {
        this.state = GameState.TRAPPED;
    }

    /**
     * Ukončí stav TRAPPED a oznámí únik ze sklepa.
     */
    public void leaveTrapped() {
        this.state = GameState.NORMAL;
        System.out.println("You escaped the basement!");
    }

    public GameState getState() {
        return state;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public QuestLog getQuestLog() {
        return questLog;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean isEscapedBasement() {
        return escapedBasement;
    }

    public Set<Location> getVisitedLocations() {
        return visitedLocations;
    }

    public void setHeath(int heath) {
        this.health = heath;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setEscapedBasement(boolean escaped) {
        this.escapedBasement = escaped;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Sníží zdraví hráče o zadanou hodnotu zranění.
     * Pokud zdraví klesne na nulu nebo méně, nastaví se na nulu.
     *
     * @param amount množství zranění
     */
    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
        }
    }
}
