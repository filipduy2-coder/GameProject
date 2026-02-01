package CharacterSys;

import ItemSys.Item;

import java.util.List;

public class Inventory {
    private List<Item> items;
    private int capacity;

    public void addItem(Item item) {
    }

    public void removeItem(Item item){}

    public boolean hasItem(Item item){return false;}

    public boolean isFull(){return false;}

    public int getFreeSlots() {return 0;}

}
