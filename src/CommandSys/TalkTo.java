package CommandSys;

import CharacterSys.NPC;
import CharacterSys.Player;

public class TalkTo implements Command{
    private Player player;
    private NPC npc;

    public TalkTo(Player player, NPC npc) {
        this.player = player;
        this.npc = npc;
    }

    @Override
    public void execute() {
        player.talkTo(npc);
    }

    @Override
    public String getName() {
        return "Dialogue";
    }
}
