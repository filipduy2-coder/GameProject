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

    public String getName() {
        return name;
    }

    public List<Item> getIngredients() {
        return ingredients;
    }

    public Item getResult() {
        return result;
    }
}


