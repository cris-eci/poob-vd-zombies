package domain;

/**
 * Clase abstracta que representa un zombie en el juego.
 * Hereda de Character y agrega atributos específicos de los zombies.
 */
public abstract class Zombie extends Character {
    protected int damage;
    protected int speed;

    public Zombie(int line, int xPos, int health, int cost, int damage, int speed) {
        super(line, xPos, health, cost);
        this.damage = damage;
        this.speed = speed;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (this.health <= 0) {
            // Manejar la muerte del zombie
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public abstract void act();
}
