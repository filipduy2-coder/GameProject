package InitializationSys;

import CharacterSys.NPC;
import ItemSys.Item;
import ItemSys.ItemType;
import ItemSys.Recipe;
import QuestSys.Quest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonWorldLoader {

    public static void load(World world) {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream input = new FileInputStream("resources/world.json")) {

            JsonNode root = mapper.readTree(input);

            loadLocations(world, root.get("locations"));
            loadQuests(world, root.get("quests"));
            loadRecipes(world, root.get("recipes"));

        } catch (Exception e) {
            throw new RuntimeException("Chyba při načítání world.json", e);
        }
    }

    private static void loadLocations(World world, JsonNode arr) {
        for (JsonNode obj : arr) {
            Location loc = new Location(
                    obj.get("id").asText(),
                    obj.get("description").asText(),
                    new HashMap<>(),
                    LocationType.ACADEMY
            );
            // ITEMS
            for (JsonNode it : obj.get("items")) {
                loc.addItem(new Item(
                        it.get("name").asText(),
                        it.get("description").asText(),
                        ItemType.valueOf(it.get("type").asText().toUpperCase())
                ));
            }
            // NPCs
            for (JsonNode npc : obj.get("npcs")) {
                List<String> dialogue = new ArrayList<>();
                npc.get("dialogue").forEach(line -> dialogue.add(line.asText()));
                loc.addNPC(new NPC(
                        npc.get("name").asText(),
                        npc.get("health").asInt(),
                        npc.get("strength").asInt(),
                        npc.get("hostile").asBoolean(),
                        dialogue
                ));
            }
            world.getLocations().add(loc);
        }
    }

    private static void loadQuests(World world, JsonNode arr) {
        for (JsonNode q : arr) {
            world.getQuests().add(new Quest(
                    q.get("name").asText(),
                    q.get("description").asText()
            ));
        }
    }

    private static void loadRecipes(World world, JsonNode arr) {
        for (JsonNode r : arr) {
            String name = r.get("name").asText();
            // INGREDIENTS (only name)
            List<Item> ingredients = new ArrayList<>();
            for (JsonNode ing : r.get("ingredients")) {
                String ingName = ing.get("name").asText();
                Item found = world.findItemByName(ingName);
                if (found != null) {
                    ingredients.add(found);
                }
            }
            // RESULT (full item)
            JsonNode resultJson = r.get("result");
            Item result = new Item(
                    resultJson.get("name").asText(),
                    resultJson.get("description").asText(),
                    ItemType.valueOf(resultJson.get("type").asText().toUpperCase())
            );
            world.getRecipes().add(new Recipe(name, ingredients, result));
        }
    }
}
