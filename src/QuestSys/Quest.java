package QuestSys;

public class Quest {
    private String name;
    private String description;
    private boolean completed = false;

    public Quest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void complete() {
        this.completed = true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
}