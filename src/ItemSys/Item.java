package ItemSys;

import CharacterSys.NPC;
import CharacterSys.Player;
import InitializationSys.*;

import java.util.Map;

/**
 * Reprezentuje item ve hře. Item má jméno, popis, typ
 * a volitelné vlastnosti, které určují jeho chování
 * (např. léčivé, posilující nebo skriptované efekty).
 *
 * Item může být automaticky aktivován (AUTO) nebo použit hráčem (USABLE).
 */
public class Item {
    private final String name;
    private String description;
    private ItemType type;
    private Map<String, Object> properties;

    /**
     * Vytvoří nový item s názvem, popisem, typem a vlastnostmi.
     *
     * @param name název itemu
     * @param description popis itemu
     * @param type typ itemu (AUTO, USABLE, QUEST, ...)
     * @param properties mapa vlastností určujících chování itemu
     */
    public Item(String name, String description, ItemType type, Map<String, Object> properties) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.properties = properties;
    }

    /**
     * Vytvoří jednoduchý item pouze se jménem.
     *
     * @param name název itemu
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * Aplikuje automatický efekt itemu, pokud jej item podporuje.
     * Používá se například pro itemy, které hráče oživí při smrti.
     *
     * Pokud má item vlastnost "reviveHealth" a hráč je mrtvý,
     * nastaví hráči nové zdraví a item se odstraní z inventáře.
     *
     * @param player hráč, na kterého se efekt aplikuje
     */
    public void applyAutoEffect(Player player) {
        if (properties.containsKey("reviveHealth")) {
            if (player.getHealth() <= 0) {
                int heal = (int) properties.get("reviveHealth");
                player.setHeath(heal);
                System.out.println(name + " saved your life!");
                player.getInventory().removeItem(this);
            }
        }
    }

    /**
     * Aplikuje efekt použitelného itemu. Chování závisí na vlastnostech:
     *
     * - "strengthMultiplier": zvýší hráčovu sílu, ale pouze v souboji
     * - "explode": spustí skriptovaný únik ze sklepa
     * - "usabledOnlyIn": omezuje použití na konkrétní lokaci
     *
     * @param player hráč, který item používá
     * @param world svět, ve kterém se efekt vyhodnocuje
     * @param game instance hry pro vyvolání událostí
     * @return true, pokud byl efekt úspěšně aplikován, jinak false
     */
    public boolean applyUsableEffect(Player player, World world, Game game) {
        if (properties.containsKey("strengthMultiplier")) {
            // Posilující item lze použít pouze v souboji
            if (player.getState().equals(GameState.COMBAT)) {
                double multiplier = (double) properties.get("strengthMultiplier");
                player.setStrength((int) (player.getStrength() * multiplier));
                System.out.println("Your strength increased by " + multiplier);
                return true;
            } else {
                // Hráč není v souboji, a proto item nelze použít
                System.out.println("This item can only be used in combat");
                return false;
            }
        }
        if (properties.containsKey("explode")) {
            // Pokud je výbušnina omezena na konkrétní lokaci
            if (properties.containsKey("usabledOnlyIn")) {
                String requiredLocation = (String) properties.get("usabledOnlyIn");
                String current = player.getCurrentLocation().getId().trim();
                // Pokud hráč není v požadované lokaci, tak nelze použít
                if (!current.equals(requiredLocation)) {
                    System.out.println("You can't use the explosive here.");
                    return false;
                }
            }
            String explode = (String) properties.get("explode");
            System.out.println(explode);
            player.setEscapedBasement(true);
            player.leaveTrapped();
            // Lynne pronese post-akční dialog po unika hráče
            NPC lynne = world.getNPCByName("lynne");
            for (String s : lynne.getPostActionDialogue()) {
                System.out.println(lynne.getName() + ':' + s);
            }
            player.forceMoveTo(LocationNames.LIBRARY);
            // Oznámení události hře
            game.setLastEvent(ActionType.GO, LocationNames.LIBRARY);
            game.onActionResolved();
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
    public Map<String, Object> getProperties() {
        return properties;
    }
}
