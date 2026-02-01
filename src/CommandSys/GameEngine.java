package CommandSys;

import java.util.List;

public class GameEngine {
    public void executeCommand(Command cmd) {
        cmd.execute();
    }
    public void printMenu(List<Command> commands) {
        System.out.println("Choose one of the following commands:");
        for (int i = 0; i<commands.size(); i++) {
            System.out.println((i+1) + ") " + commands.get(i).getName());
        }
    }
}
