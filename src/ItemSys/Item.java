package ItemSys;

import CharacterSys.Player;

import java.util.Map;

public class Item {
    private String name;
    private String description;
    private ItemType type;
    private Map<String, Object> properties;

    public Item(String name, String description, ItemType type, Map<String, Object> properties) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.properties = properties;
    }
    public Item(String name) {
        this.name = name;
    }
    public void applyAutoEffect(Player player) {
        if (properties.containsKey("reviveHealth")) {
            if (player.getHealth() <= 0) {
                int heal = (int) properties.get("reviveHealth");
                player.setHeath(heal);
                System.out.println(name + " saved your life!");
            }
        }
    }
    public void applyUsableEffect(Player player) {
        if (properties.containsKey("strengthMultiplier")) {
            double multiplier = (double) properties.get("strengthMultiplier");
            player.setStrength((int)(player.getStrength() * multiplier));
            System.out.println("Your strength increased by " + multiplier);
        }
        if (properties.containsKey("explode")) {
            if (properties.containsKey("usabledOnlyIn")) {
                String requiredLocation = (String) properties.get("usabledOnlyIn");
                String current = player.getCurrentLocation().getId().toLowerCase().trim();
                if (!current.equals(requiredLocation)) {
                    System.out.println("You can't use the explosive here.");
                    return;
                }
            }
            String explode = (String) properties.get("explode");
            System.out.println(explode);
            player.setEscapedBasement(true);
            player.leaveTrapped();
            player.forceMoveTo("Silent Study Room");
        }
    }
    public String getName() {
        return name;
    }
    public ItemType getType() {
        return type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
