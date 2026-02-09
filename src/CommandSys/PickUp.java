package CommandSys;

import CharacterSys.Player;

public class PickUp implements Command{
    private Player player;

    public PickUp(Player player) {
        this.player = player;
    }
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Pick Up <item>");
            System.out.println("All items in current location: " + player.getCurrentLocation().getAllItems());
            return;
        }
        player.pickUp(args[0]);
    }

    @Override
    public String getName() {
        return  "Pick Up";
    }
}
