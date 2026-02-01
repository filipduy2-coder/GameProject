package CommandSys;

import CharacterSys.Player;
import ItemSys.Recipe;

public class CraftItem implements Command{
    private Player player;
    private Recipe recipe;

    public CraftItem(Player player, Recipe recipe) {
        this.player = player;
        this.recipe = recipe;
    }

    @Override
    public void execute() {
        player.craftItem(recipe);
    }

    @Override
    public String getName() {
        return "Craft";
    }
}
