package CharacterSys;

public class Companion {
    private final String name;
    private  boolean isHungry;
    public Companion() {
        this.name = "Togo";
        this.isHungry = false;
    }

    public boolean isHungry() {
        return isHungry;
    }

    public void setHungry(boolean hungry) {
        isHungry = hungry;
    }
    public String getName() {
        return name;
    }
}
