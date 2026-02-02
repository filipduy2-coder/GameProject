package CommandSys;

import CharacterSys.Player;

public class ShowInventory implements Command{
    private Player player;

    public ShowInventory(Player player) {
        this.player = player;
    }

    @Override
    public void execute(String[] args) {
        player.showInventory();
    }

    @Override
    public String getName() {
        return "Show Inventory";
    }
}
