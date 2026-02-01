package CommandSys;

import CharacterSys.Player;
import InitializationSys.Location;

public class Go implements Command {
    private Player player;
    private Location target;

    public Go(Player player, Location target) {
        this.player = player;
        this.target = target;
    }

    @Override
    public void execute() {
        player.go(target);
    }

    @Override
    public String getName() {
        return "Go";
    }
}
