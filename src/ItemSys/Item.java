package ItemSys;

public class Item {
    protected String name;
    protected String description;
    protected ItemType type;

    public Item(String name, String description, ItemType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }
}
