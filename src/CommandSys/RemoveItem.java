package CommandSys;

import CharacterSys.Player;
import ItemSys.Item;

public class RemoveItem implements Command{
    private Player player;
    private Item item;

    public RemoveItem(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    @Override
    public void execute() {
        player.dropItem(item);
    }

    @Override
    public String getName() {
        return "Remove Item";
    }
}
