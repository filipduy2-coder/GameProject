package CommandSys;

import CharacterSys.Player;

public class UseItem implements Command{
    private Player player;

    public UseItem(Player player) {
        this.player = player;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Use Item <item>");
            return;
        }
        player.useItem(args[0]);
    }
    @Override
    public String getName() {
        return "Use Item";
    }
}
