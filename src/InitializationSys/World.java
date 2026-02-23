package InitializationSys;

import CharacterSys.Companion;
import CharacterSys.NPC;
import CharacterSys.Player;
import ItemSys.Item;
import ItemSys.Recipe;
import QuestSys.Quest;

import java.util.*;

/**
 * Reprezentuje celý herní svět. Uchovává seznam lokací, NPC, úkolů,
 * receptů, herní les, vstup do akademie a stav kostního questu.
 *
 * Třída slouží jako centrální úložiště všech herních dat.
 *
 * @author Filip
 */
public class World {
    private final List<Location> locations = new ArrayList<>();
    private final Location[][] forest = new Location[5][5];
    private final List<Quest> quests = new ArrayList<>();
    private final List<Recipe> recipes = new ArrayList<>();
    private Location academyEntryBlock;
    private Location currentBoneLocation;
    private final List<Location> boneLocation = new ArrayList<>();
    private final Map<String, NPC> npcRegistr = new HashMap<>();
    private final Companion companion = new Companion();

    /**
     * Aktivuje kostní úkol. Spustí hlad u společníka,
     * náhodně umístí kost do jedné z lokací
     * a vypíše hráči tematickou nápovědu.
     */
    public void activeBoneQuest() {
        companion.setHungry(true);
        String comName = companion.getName();
        spawnBone();
        System.out.println(comName + " seems to sniff toward a room with " + generateBoneHint(currentBoneLocation));
    }

    /**
     * Vygeneruje textovou nápovědu podle toho,
     * ve které lokaci se nachází kost.
     *
     * @param loc lokace, kde je kost umístěna
     * @return textová nápověda pro hráče
     */
    public String generateBoneHint(Location loc) {
        String comName = companion.getName();
        if (loc == null) {
            return comName + " seems confused and cannot find any scent.";
        }
        return switch (loc.getId()) {
            case LocationNames.CLASSROOM -> "desks and simple magic diagrams.";
            case LocationNames.LABORATORY -> "a lab with a big worktable and experiment circles.";
            case LocationNames.LIBRARY -> "tall bookshelves and soft carpet.";
            case LocationNames.ILLUSION_ROOM -> "full of mirrors and illusion circles.";
            case LocationNames.HEALING_ROOM -> "smell of herbs and healing potions.";
            case LocationNames.STORAGE -> "a dusty storage full of crates and boxes.";
            default -> "something that " + comName + " cannot describe clearly";
        };
    }

    /**
     * Náhodně vybere lokaci z předem určených míst
     * a umístí do ní předmět "bone".
     * Pokud již kost někde byla, odstraní ji.
     */
    public void spawnBone() {
        if (boneLocation.isEmpty()) {
            System.out.println("No bone location found!");
            return;
        }
        if (currentBoneLocation != null) {
            currentBoneLocation.removeItem("bone");
        }
        Location chosen = boneLocation.get(new Random().nextInt(boneLocation.size()));
        currentBoneLocation = chosen;
        chosen.addItem(new Item("bone"));
    }

    /**
     *
     * Umístí NPC do zadané lokace. Pokud NPC nebo lokace neexistují,
     * metoda nic neprovede. NPC je předtím odstraněno ze všech ostatních lokací.
     *
     * @param npcName jméno NPC
     * @param locationId identifikátor lokace
     */
    private void placeNpc(String npcName, String locationId) {
        NPC npc = npcRegistr.get(npcName);
        Location loc = getLocationByName(locationId);
        if (loc == null || npc == null) return;
        removeNPCFromAllLoc(npc);
        loc.addNPC(npc);
    }

    /**
     * Odstraní NPC z konkrétní lokace podle jména.
     *
     * @param npcName jméno NPC
     * @param locationId identifikátor lokace
     */
    private void removeNpc(String npcName, String locationId) {
        Location loc = getLocationByName(locationId);
        if (loc == null) return;
        loc.getNpcs().removeIf(n -> n.getName().equalsIgnoreCase(npcName));
    }

    /**
     * Aktualizuje rozmístění NPC podle stavu hráče a příběhových podmínek.
     *
     * @param player hráč, jehož stav ovlivňuje NPC
     */
    public void updateNPCState(Player player) {
        if (player == null) return;
        boolean elianAlive = getNPCByName("elian").isAlive();
        boolean escaped = player.isEscapedBasement();
        boolean blockAllQuests = companion.isHungry();
        if (!elianAlive && !escaped && !blockAllQuests) {
            placeNpc("lynne", LocationNames.LIBRARY);
        } else {
            removeNpc("lynne", LocationNames.LIBRARY);
        }
        if (!elianAlive && escaped && !blockAllQuests) {
            placeNpc("rovan", LocationNames.DORMITORY);
        } else {
            removeNpc("rovan", LocationNames.DORMITORY);
        }
    }

    /**
     * Vytiskne mapu lesa. Zobrazuje hráče, vstup do akademie
     * a prázdná pole. Pokud je hráč v akademii, mapa se nevypíše.
     *
     * @param player hráč, jehož pozice se má zobrazit
     */
    public void printMapForest(Player player) {
        if (player.getCurrentLocation().getType() == LocationType.ACADEMY) {
            return;
        }
        Location[][] forest = this.forest;
        Location playerLoc = player.getCurrentLocation();
        Location academy = this.academyEntryBlock;
        System.out.println("=== MAP ===");
        for (Location[] value : forest) {
            for (Location loc : value) {
                if (loc == playerLoc) {
                    System.out.print("●  ");   // hráč
                } else if (loc == academy) {
                    System.out.print("X  ");   // akademie
                } else {
                    System.out.print(".  ");   // prázdné pole
                }
            }
            System.out.println();
        }
        System.out.println("=============");
    }

    /**
     * Zaregistruje NPC do světového registru podle jeho jména.
     *
     * @param npc NPC, které má být registrováno
     */
    public void registerNPC(NPC npc) {
        npcRegistr.put(npc.getName(), npc);
    }

    /**
     * Vrátí recept podle jeho názvu.
     *
     * @param name název receptu
     * @return nalezený recept nebo null
     */
    public Recipe getRecipeByName(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.name().equals(name)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Vrátí NPC podle jména z registru NPC.
     *
     * @param name jméno NPC
     * @return nalezené NPC nebo null
     */
    public NPC getNPCByName(String name) {
        if (name == null) return null;
        return npcRegistr.get(name.toLowerCase());
    }

    /**
     * Vyhledá lokaci podle jejího názvu (nerozlišuje velikost písmen).
     *
     * @param name název lokace
     * @return nalezená lokace nebo null, pokud neexistuje
     */
    public Location getLocationByName(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase().trim();
        for (Location location : locations) {
            if (location.getId().toLowerCase().trim().equals(key)) {
                return location;
            }
        }
        return null;
    }

    /**
     * Pomocná metoda pro odstranění NPC ze všech lokací ve světě.
     *
     * @param npc NPC, které má být odstraněno
     */
    private void removeNPCFromAllLoc(NPC npc) {
        if (npc == null) return;
        for (Location loc : locations) {
            loc.removeNPC(npc);
        }
    }

    /**
     * Vrátí výchozí lokaci hráče v lese (levý horní roh).
     *
     * @return počáteční lokace
     */
    public Location getStartingLocation() {
        return forest[0][0];
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Location[][] getForest() {
        return forest;
    }

    public Companion getCompanion() {
        return companion;
    }

    public void setAcademyEntryBlock(Location academyEntryBlock) {
        this.academyEntryBlock = academyEntryBlock;
    }

    public Location getAcademyEntryBlock() {
        return academyEntryBlock;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Location getCurrentBoneLocation() {
        return currentBoneLocation;
    }


    public List<Location> getBoneLocation() {
        return boneLocation;
    }


}
