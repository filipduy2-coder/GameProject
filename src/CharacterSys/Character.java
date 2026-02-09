package CharacterSys;

public abstract class Character {
    protected String name;
    protected int health;
    protected int strength;

    public Character(String name, int health, int strength) {
        this.name = name;
        this.health = health;
        this.strength = strength;
    }

    public boolean isAlive() {return health > 0;};
    public abstract void takeDamage(int amount);
    public String getName(){return name;}
    public int getHealth(){return health;}
    public int getStrength(){return strength;}
}
