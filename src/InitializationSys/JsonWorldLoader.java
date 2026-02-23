package InitializationSys;

import CharacterSys.NPC;
import ItemSys.Item;
import ItemSys.ItemType;
import ItemSys.Recipe;
import QuestSys.Quest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Načítá herní svět ze souboru world.json umístěného v resources.
 * Načítá lokace, NPC, předměty, úkoly a recepty a registruje je do objektu World.
 */
public class JsonWorldLoader {

    /**
     * Hlavní metoda pro načtení celého světa.
     * Otevře JSON soubor, načte jeho kořen a předá části specializovaným metodám.
     *
     * @param world instance světa, do které se data načtou
     */
    public static void load(World world) {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream input = JsonWorldLoader.class.getClassLoader().getResourceAsStream("world.json")) {
            if (input == null) {
                throw new FileNotFoundException("world.json is not found in resources");
            }
            JsonNode root = mapper.readTree(input);

            loadLocations(world, root.get("locations"));
            loadQuests(world, root.get("quests"));
            loadRecipes(world, root.get("recipes"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Načte lokace ze sekce "locations" v JSON.
     * Všechny lokace jsou zde načítány jako LocationType.ACADEMY.
     * Tato metoda tedy slouží výhradně pro místnosti v akademii.
     *
     * @param world svět, do kterého se lokace registrují
     * @param arr JSON pole lokací
     */
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
                Map<String, Object> props = pasteProperties(it);
                loc.addItem(new Item(
                        it.get("name").asText().toLowerCase(),
                        it.get("description").asText(),
                        ItemType.valueOf(it.get("type").asText().toUpperCase()),
                        props
                ));
            }
            // NPCs
            for (JsonNode npc : obj.get("npcs")) {
                List<String> pre = new ArrayList<>();
                npc.get("preActionDialogue").forEach(line -> pre.add(line.asText()));
                List<String> post = new ArrayList<>();
                npc.get("postActionDialogue").forEach(line -> post.add(line.asText()));
                NPC npcObj = new NPC(
                        npc.get("name").asText().toLowerCase(),
                        npc.get("health").asInt(),
                        npc.get("strength").asInt(),
                        npc.get("hostile").asBoolean(),
                        pre,
                        post,
                        npc.get("startsCombat").asBoolean()
                );
                // Registrace NPC do světa i lokace
                world.registerNPC(npcObj);
                loc.addNPC(npcObj);
            }
            world.getLocations().add(loc);
        }
    }

    /**
     * Načte úkoly ze sekce "quests".
     * Každý úkol má triggery a podmínky dokončení.
     */
    private static void loadQuests(World world, JsonNode arr) {
        for (JsonNode q : arr) {
            List<TriggerEvent> triggers = new ArrayList<>();
            for (JsonNode t : q.get("trigger")) {
                triggers.add(parseEvent(t.asText()));
            }
            List<TriggerEvent> completions = new ArrayList<>();
            for (JsonNode c : q.get("completion")) {
                completions.add(parseEvent(c.asText()));
            }
            world.getQuests().add(new Quest(
                    q.get("name").asText(),
                    q.get("description").asText(),
                    triggers,
                    completions
            ));
        }
    }

    /**
     * Načte recepty ze sekce "recipes".
     * Každý recept má seznam ingrediencí a výsledný item.
     */
    private static void loadRecipes(World world, JsonNode arr) {
        for (JsonNode r : arr) {
            String name = r.get("name").asText().toLowerCase();
            // INGREDIENTS (only name)
            List<String> ingredients = new ArrayList<>();
            for (JsonNode ing : r.get("ingredients")) {
                ingredients.add(ing.get("name").asText().toLowerCase());
            }
            // RESULT (full item)
            JsonNode resultJson = r.get("result");
            Map<String, Object> props = pasteProperties(resultJson);
            Item result = new Item(
                    resultJson.get("name").asText().toLowerCase(),
                    resultJson.get("description").asText(),
                    ItemType.valueOf(resultJson.get("type").asText().toUpperCase()),
                    props
            );
            world.getRecipes().add(new Recipe(name, ingredients, result));
        }
    }

    /**
     * Pomocná metoda pro načtení vlastností itemu (properties).
     * Automaticky rozpozná typ hodnoty (int, double, boolean, text).
     *
     * @param it JSON uzel reprezentující item
     * @return mapa vlastností itemu
     */
    private static Map<String, Object> pasteProperties(JsonNode it) {
        Map<String, Object> props = new HashMap<>();
        if (it.has("properties")) {
            it.get("properties").fields().forEachRemaining(entry -> {
                JsonNode val = entry.getValue();
                if (val.isInt()) {
                    props.put(entry.getKey(), val.asInt());
                } else if (val.isDouble()) {
                    props.put(entry.getKey(), val.asDouble());
                } else if (val.isBoolean()) {
                    props.put(entry.getKey(), val.asBoolean());
                } else {
                    props.put(entry.getKey(), val.asText());
                }
            });
        }
        return props;
    }

    /**
     * Převede textovou reprezentaci události z JSON na objekt TriggerEvent.
     * Očekávaný formát je "typ:argument", např. "talk_to_npc:lynne".
     *
     * @param raw textová podoba události z JSON
     * @return vytvořený TriggerEvent odpovídající zadanému textu
     * @throws IllegalArgumentException pokud formát nebo typ události není platný
     */
    private static TriggerEvent parseEvent(String raw) {
        String[] parts = raw.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid event format: " + raw);
        }
        String kind = parts[0].toLowerCase().trim();
        String arg = parts[1].toLowerCase().trim();
        ActionType type = switch (kind) {
            case "talk_to_npc" -> ActionType.TALK;
            case "enter_location" -> ActionType.GO;
            case "craft_item" -> ActionType.CRAFT_ITEM;
            case "use_item" -> ActionType.USE_ITEM;
            case "pickup_item" -> ActionType.PICKUP;
            case "defeat_enemy" -> ActionType.ATTACK;
            default -> throw new IllegalArgumentException("Unknown event kind: " + kind);
        };
        return new TriggerEvent(type, arg);
    }
}
