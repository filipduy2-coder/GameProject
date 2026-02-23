package CommandSys;

import CharacterSys.Player;

public record ShowInventory(Player player) implements Command {

    @Override
    public void execute(String[] args) {
        player.showInventory();
    }

    @Override
    public String getName() {
        return "Show Inventory";
    }
}
