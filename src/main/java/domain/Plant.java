package domain;

/**
 * Clase abstracta que representa una planta en el juego.
 */
public abstract class Plant extends Character {

    public Plant(int line, int xPos, int health, int cost) {
        super(line, xPos, health, cost);
    }

    @Override
    public abstract void act();
}
