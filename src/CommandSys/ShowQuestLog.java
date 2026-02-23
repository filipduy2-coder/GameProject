package CommandSys;

import CharacterSys.Player;

public record ShowQuestLog(Player player) implements Command {

    @Override
    public void execute(String[] args) {
        player.showQuestLog();
    }

    @Override
    public String getName() {
        return "Show QuestLog";
    }
}
