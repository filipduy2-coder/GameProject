package CommandSys;

import CharacterSys.Player;

public class ShowQuestLog implements Command{
    private Player player;

    public ShowQuestLog(Player player) {
        this.player = player;
    }

    @Override
    public void execute(String[] args) {
        player.showQuestLog();
    }

    @Override
    public String getName() {
        return "Show Quest Log";
    }
}
