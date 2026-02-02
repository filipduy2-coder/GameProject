package CommandSys;

import CharacterSys.Player;

public class CraftItem implements Command{
    private Player player;

    public CraftItem(Player player) {
        this.player = player;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Craft <item>");
            return;
        }
        player.craftItem(args[0]);
    }


    @Override
    public String getName() {
        return "Craft";
    }
}
