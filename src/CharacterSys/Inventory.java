package CharacterSys;

import ItemSys.Item;

import java.util.List;

/**
 * Inventář hráče s pevně danou kapacitou.
 * Umožňuje uchovávat předměty a poskytuje základní operace
 * pro jejich přidávání, odebírání a vyhledávání.
 *
 * @param items    seznam aktuálně uložených itemů
 * @param capacity maximální počet itemů, které lze uložit
 *
 * @author Filip Quan
 */
public record Inventory(List<Item> items, int capacity) {

    /**
     * Přidá item do inventáře, pokud není plný.
     *
     * @param item item, který má být přidán
     */
    public void addItem(Item item) {
        if (isFull()) return;
        items.add(item);
    }

    /**
     * Odebírání item z inventáře.
     *
     * @param item item, který má být odebrán
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Zjistí, zda inventář obsashuje item se zadaným názvem.
     *
     * @param name název hledaného itemu
     * @return true, pokud je předmět nalezen, jinak false
     */
    public boolean hasItem(String name) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vrátí item podle názvu.
     *
     * @param name název vraceného itemu
     * @return nalezený předmět, nebo null pokud neexistuje
     */
    public Item getItemByName(String name) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Zjistí, zda je inventář plný.
     *
     * @return true, pokud počet itemů dosáhl kapacity, jinak false
     */
    public boolean isFull() {
        return items.size() >= capacity;
    }

    /**
     * Vrátí počet volných slotů.
     *
     * @return počet zbývajících míst
     */
    public int getFreeSlots() {
        return capacity - items.size();
    }

    /**
     * Vypíše obsah inventáře.
     * Každý předmět je vypsán se svým názvem a popisem.
     */
    public void printInventory() {
        for (Item i : items) {
            System.out.println(i.getName() + " (" + i.getDescription() + ")");
        }
    }
}
