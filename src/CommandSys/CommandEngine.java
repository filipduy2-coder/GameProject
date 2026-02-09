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
    public List<Command> getCommandList() {
        return new ArrayList<>(commands.values());
    }

    public void handleInput(String input) {
        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0].toLowerCase();
        String[] args =  Arrays.copyOfRange(parts, 1, parts.length);
        Command cmd = commands.get(commandName);
        if (cmd == null && parts.length > 1) {
            String twoWord = (parts[0] + " " + parts[1]).toLowerCase();
            cmd = commands.get(twoWord);
            if (cmd != null) {
                args = Arrays.copyOfRange(parts, 2, parts.length);
            }
        }
        if (cmd == null) {
            System.out.println("Command " + parts[0] + " not found");
        } else {
            cmd.execute(args);
        }
    }

    public void printMenu(List<Command> commands) {
        System.out.println("Choose one of the following commands:");
        for (Command c : commands) {
            String name = c.getName().toLowerCase();
            if (name.equals("help") || name.equals("exit") || name.equals("hint") || name.equals("go")) {
                System.out.println(" - " + c.getName());
            }
        }
    }
}
