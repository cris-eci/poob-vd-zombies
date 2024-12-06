package domain;

public abstract class Character extends Entity {
    protected int health;
    protected int cost;

    protected Character(int health, int cost) {
        this.health = health;
        this.cost = cost;
    }

    public int getHealth() {
        return health;
    }

    public int getCost() {
        return cost;
    }

    public void shoot(int line){
        // Disparar
    }
}
