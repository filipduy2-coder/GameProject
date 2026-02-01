package CharacterSys;

public abstract class Character {
    private String name;
    private int health;
    private int strength;
    private boolean hostile;

    public Character(String name, int health, int strength, boolean hostile) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.hostile = hostile;
    }

    public boolean isAlive() {return true;};
    public abstract void attack(Character target);
    public abstract void takeDamage(int amount);
}
