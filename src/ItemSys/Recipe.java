package ItemSys;

import java.util.List;

public class Recipe {
    private String name;
    private List<Item> ingredients;
    private Item result;

    public Recipe(String name, List<Item> ingredients, Item result) {
        this.name = name;
        this.ingredients = ingredients;
        this.result = result;
    }
}


