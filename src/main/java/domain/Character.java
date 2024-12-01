package domain;

/**
 * Clase abstracta que representa a un personaje en el juego (planta o zombie).
 * Hereda de Entity y agrega atributos comunes como salud y costo.
 */
public abstract class Character extends Entity {
    protected int health;
    protected int cost;

    public Character(int line, int xPos, int health, int cost) {
        super(line, xPos);
        this.health = health;
        this.cost = cost;
    }

    public int getHealth() {
        return health;
    }

    public int getCost() {
        return cost;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Método para aplicar daño al personaje.
     * @param damage Cantidad de daño a aplicar.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
    }

    /**
     * Método abstracto que define la acción del personaje en cada turno.
     */
    public abstract void act();
}
