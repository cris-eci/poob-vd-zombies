package domain;

public abstract class Zombie extends Character {
    protected int damage;

    protected Zombie(int health, int cost, int damage) {
        super(health, cost);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }
}
