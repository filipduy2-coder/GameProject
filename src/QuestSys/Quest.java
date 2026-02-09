package QuestSys;

public class Quest {
    private String name;
    private String description;
    private String trigger;
    private String completion;
    private boolean active = false;
    private boolean completed = false;

    public Quest(String name, String description, String trigger, String completion) {
        this.name = name;
        this.description = description;
        this.trigger = trigger;
        this.completion = completion;
    }
    public String getName() {
        return name;
    }
    public String getTrigger() {
        return trigger;
    }
    public boolean isActive() {
        return active;
    }
    public boolean isCompleted() {
        return completed;
    }
    public String getCompletion() {
        return completion;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}