// Peashooter
package domain;
public class Peashooter extends Plant implements Shooter {
    private static final int COST = 100;
    private static final int HEALTH = 300;
    public static final int DAMAGE = 100; // Daño por proyectil

    public Peashooter() {
        super(HEALTH, COST);
    }

    @Override
    public void shoot(int lane) {
        // Lógica para disparar
    }

    public int getDamage() {
        return DAMAGE;
    }
}
