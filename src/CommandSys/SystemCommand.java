package CommandSys;

import java.util.List;

public class SystemCommand implements Command{
    public enum Type {EXIT, HELP, HINT}
    private Type type;
    private String hint;
    private List<Command> commands;

    public SystemCommand(Type type) {
        this.type = type;
    }
    public SystemCommand(Type type, String hint) {
        this.type = type;
        this.hint = hint;
    }
    public SystemCommand(Type type, List<Command> commands) {
        this.type = type;
        this.commands = commands;
    }
    @Override
    public void execute() {
        switch (type) {
            case EXIT:
                System.out.println("Quitting...");
                System.exit(0);
                break;
            case HELP:
                System.out.println("Available commands:");
                for (int i = 0; i < commands.size(); i++) {
                    System.out.println((i+1) + ") " + commands.get(i).getName());
                }
                break;
            case HINT:
                System.out.println(hint);
                break;
        }
    }

    @Override
    public String getName() {
        return switch (type) {
            case EXIT -> "Exit";
            case HELP -> "Help";
            case HINT -> "Hint";
        };
    }
}
