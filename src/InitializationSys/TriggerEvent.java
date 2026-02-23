package InitializationSys;

public record TriggerEvent(ActionType type, String argument) {
    public TriggerEvent{
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        argument = argument == null ? "" : argument.toLowerCase().trim()    ;
    }
}
