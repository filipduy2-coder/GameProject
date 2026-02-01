package CommandSys;

import CharacterSys.Player;
import ItemSys.Item;

public class UseItem implements Command{
    private Player player;
    private Item item;

    public UseItem(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    @Override
    public void execute() {
        player.useItem(item);
    }

    @Override
    public String getName() {
        return "Use Item";
    }
}
