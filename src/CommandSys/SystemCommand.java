package CommandSys;

import java.util.List;

public class SystemCommand implements Command{
    public enum Type {EXIT, HELP, HINT}
    private Type type;
    private String hint;
    private List<Command> commands;

    public SystemCommand(Type type, String hint, List<Command> commands) {
        this.type = type;
        this.hint = hint;
        this.commands = commands;
    }

    public static SystemCommand exit() {
        return new SystemCommand(Type.EXIT, null, null);
    }
    public static SystemCommand help(List<Command> commands) {
        return new SystemCommand(Type.HELP, null, commands);
    }
    public static SystemCommand hint(String hint) {
        return new SystemCommand(Type.HINT, hint, null);
    }
    @Override
    public void execute(String[] args) {
        switch (type) {
            case EXIT -> {
                System.out.println("Quitting...");
                System.exit(0);
            }
            case HELP -> {
                System.out.println("Available commands:");
                for (int i = 0; i < commands.size(); i++) {
                    System.out.println((i+1) + ") " + commands.get(i).getName());
                }
            }
            case HINT -> System.out.println(hint);
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
