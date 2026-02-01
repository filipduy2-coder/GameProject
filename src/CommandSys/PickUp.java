package CommandSys;

import CharacterSys.Player;
import ItemSys.Item;

public class PickUp implements Command{
    private Player player;
    private Item item;

    public PickUp(Player player, Item item) {
        this.player = player;
        this.item = item;
    }
    @Override
    public void execute() {
        player.pickUp(item);
    }

    @Override
    public String getName() {
        return  "Pick Up";
    }
}
