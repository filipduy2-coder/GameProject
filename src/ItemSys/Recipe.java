package ItemSys;

import java.util.HashMap;
import java.util.List;

public record Recipe(String name, List<String> ingredientNames, Item result) {
    public Item createResult() {
        return new Item(
                result.getName(),
                result.getDescription(),
                result.getType(),
                new HashMap<>(result.getProperties()));
    }
}


