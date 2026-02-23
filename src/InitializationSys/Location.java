package InitializationSys;

import CharacterSys.NPC;
import ItemSys.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reprezentuje jednu lokaci ve hře.
 * Slouží jako základní stavební prvek herního světa.
 *
 * Lokace může obsahovat předměty, NPC a může být propojena s jinými
 * lokacemi pomocí směrových vazeb.
 *
 * @author Filip
 */
public class Location {
    private final String id;
    private String description;
    private Map<String, Location> neighbours;
    private List<Item> items;
    private final List<NPC> npcs;
    private LocationType type;

    /**
     * Vytvoří novou lokaci s kompletní konfigurací
     *
     * @param name identifikátor lokace
     * @param description textový popis lokace
     * @param neighbours mapa sousedních lokací podle směru
     * @param type typ lokace (např. ACADEMY, FOREST)
     */
    public Location(String name, String description, Map<String, Location> neighbours, LocationType type) {
        this.id = name;
        this.description = description;
        this.neighbours = (neighbours != null) ? neighbours : new HashMap<>();
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
        this.type = type;
    }

    /**
     * Vytvoří lokaci pouze s identifikátorem. Používá se pro jednoduché
     * nebo později inicializované lokace.
     *
     * @param name identifikátor lokace
     */
    public Location(String name) {
        this.id = name;
        this.npcs = new ArrayList<>();
    }

    /**
     * Přidá sousední lokaci pod daným směrem.
     *
     * @param direction směr (např. "N", "south", "left")
     * @param neighbour sousední lokace
     */
    public void addNeighbour(String direction, Location neighbour) {
        neighbours.put(direction, neighbour);
    }

    /**
     * Přidá předmět do lokace.
     *
     * @param item předmět, který má být přidán
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Přidá NPC do lokace.
     *
     * @param npc NPC, které má být přidáno
     */
    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    /**
     * Odstraní item z lokace podle názvu.
     *
     * @param itemName název itemu
     */
    public void removeItem(String itemName) {
        items.removeIf(item -> item.getName().equals(itemName));
    }

    /**
     * Odstraní NPC z lokace.
     *
     * @param npc NPC, které má být odstraněno
     */
    public void removeNPC(NPC npc) {
        npcs.remove(npc);
    }

    /**
     * Vrátí seznam všech itemů v lokaci jako text.
     * Pokud zde žádné itemy nejsou, vrací "None".
     *
     * @return textový seznam itemů
     */
    public String getAllItems() {
        if (items.isEmpty())
            return "None";
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append(item.getName()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    /**
     * Vrátí seznam všech NPC v lokaci jako text.
     * Pokud zde žádné NPC nejsou, vrací "None".
     *
     * @return textový seznam NPC
     */
    public String getAllNPCs() {
        if (npcs.isEmpty()) {
            return "None";
        }
        StringBuilder sb = new StringBuilder();
        for (NPC npc : npcs) {
            sb.append(npc.getName()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    /**
     * Vyhledá NPC podle jména (nerozlišuje velikost písmen).
     *
     * @param name jméno NPC
     * @return nalezené NPC nebo null, pokud neexistuje
     */
    public NPC getNPCByName(String name) {
        for (NPC npc : npcs) {
            if (npc.getName().equalsIgnoreCase(name)) {
                return npc;
            }
        }
        return null;
    }

    /**
     * Vyhledá item podle jména.
     *
     * @param itemName název itemu
     * @return nalezený item nebo null, pokud neexistuje
     */
    public Item getItemByName(String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) return item;
        }
        return null;
    }
    public String getId() {
        return id;
    }

    public LocationType getType() {
        return type;
    }

    public Map<String, Location> getNeighbours() {
        return neighbours;
    }

    public String getDescription() {
        return description;
    }

    public List<NPC> getNpcs() {
        return npcs;
    }



    @Override
    public String toString() {
        return "Location{" +
                "id=" + id + '\'' +
                ", description='" + description + '\'' +
                ", neighbours=" + neighbours +
                ", items=" + items +
                ", npcs=" + npcs +
                ", type=" + type +
                '}';
    }
}
