package CommandSys;

import CharacterSys.Player;

public class RemoveItem implements Command{
    private Player player;

    public RemoveItem(Player player) {
        this.player = player;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Remove Item <item>");
            return;
        }
        player.dropItem(args[0]);
    }

    @Override
    public String getName() {
        return "Remove Item";
    }
}
