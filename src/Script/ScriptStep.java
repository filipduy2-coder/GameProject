package Script;

public class ScriptStep {
    private final String fromRoom;
    private final String toRoom;

    public ScriptStep(String fromRoom, String toRoom) {
        this.fromRoom = fromRoom;
        this.toRoom = toRoom;
    }

    public String getFromRoom() {
        return fromRoom;
    }

    public String getToRoom() {
        return toRoom;
    }
}
