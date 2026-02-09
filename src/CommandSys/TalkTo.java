package CommandSys;

import CharacterSys.Player;

public class TalkTo implements Command{
    private Player player;

    public TalkTo(Player player) {
        this.player = player;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Dialogue <npc>");
            System.out.println("ALl npcs in current location: " + player.getCurrentLocation().getAllNPCs());
            return;
        }
        player.talkTo(args[0]);
    }


    @Override
    public String getName() {
        return "Dialogue";
    }
}
