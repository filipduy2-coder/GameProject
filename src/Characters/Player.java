package Characters;

public class Player extends Character {
    private Inventory inventory;
    private Companion companion;
    private Location currentLocation;
    private QuestLog questLog;

    public void go(String target) {}
    public void talkTo(NPC npc) {}
    public void useItem(String itemName) {}
    public void pickUp(Item item) {}
    public void dropItem(Item item) {}
    public void showInventory() {}
    public void showQuestLog() {}
    public void craftItem(Recipe recipe) {}

}
