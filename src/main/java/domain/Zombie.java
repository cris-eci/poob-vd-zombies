package domain;

/**
 * The Zombie class represents an abstract type of character in the game.
 * It extends the Character class and includes additional attributes and methods specific to zombies.
 */
public abstract class Zombie extends Character {
    private boolean  killedByLawnmower = false;
    /**
     * The amount of damage this zombie can inflict.
     */
    protected int damage;
    /**
     * Constructs a new Zombie with the specified health, cost, and damage.
     *
     * @param health the health points of the zombie
     * @param cost the cost to create the zombie
     * @param damage the damage the zombie can inflict
     */
    protected Zombie(int health, int cost, int damage) {
        super(health, cost);
        this.damage = damage;
    }

    /**
     * Returns the amount of damage this zombie can inflict.
     *
     * @return the damage value
     */
    public int getDamage() {
        return damage;
    }

    public void setKilledByLoawnmower(){
        killedByLawnmower = true;
    }

    public boolean getKilledByLawnmower(){
        return killedByLawnmower;
    }
}

    // public void takeDamage(int damage) {
    //     this.health -= damage;
    // }

