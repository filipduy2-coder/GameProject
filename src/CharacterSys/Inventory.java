package CharacterSys;

import ItemSys.Item;

import java.util.List;

public class Inventory {
    private List<Item> items;
    private int capacity;

    public Inventory(List<Item> items, int capacity) {
        this.items = items;
        this.capacity = capacity;
    }

    public boolean addItem(Item item) {
        if (isFull()) return false;
        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public boolean hasItem(String name) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Item getItemByName(String name) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return null;
    }

    public boolean isFull() {
        return items.size() >= capacity;
    }
    public int getFreeSlots() {
        return capacity - items.size();
    }
    public List<Item> getItems() {
        return items;
    }
    public void printInventory() {
        for (Item i : items) {
            System.out.println(i.getName());
        }
    }
}
