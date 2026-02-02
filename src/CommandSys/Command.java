package CommandSys;

public interface Command {
    void execute(String[] args);
    String getName();
}
