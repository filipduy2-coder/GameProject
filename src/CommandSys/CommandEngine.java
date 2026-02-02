package CommandSys;

import java.util.*;

public class CommandEngine {
    private Map<String, Command> commands = new HashMap<>();

    public void setCommands(List<Command> cmds) {
        commands.clear();
        for (Command c : cmds) {
            commands.put(c.getName().toLowerCase(), c);
        }
    }

    public void handleInput(String input) {
        String[] parts = input.split(" ");
        String commandName = parts[0];
        String[] args =  Arrays.copyOfRange(parts, 1, parts.length);
        Command cmd = commands.get(commandName);
        if (cmd == null) {
            System.out.println("Command " + commandName + " not found");
        } else {
            cmd.execute(args);
        }
    }

    public void printMenu(List<Command> commands) {
        System.out.println("Choose one of the following commands:");
        for (int i = 0; i<commands.size(); i++) {
            System.out.println((i+1) + ") " + commands.get(i).getName());
        }
    }
}
