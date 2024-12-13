/**
 * The ECIZombie class represents a type of Zombie with predefined cost, health, and damage values.
 * It extends the Zombie class and implements the Shooter interface.
 * This type of zombie can shoot.
 * 
 * @version 2024
 */
package domain;

public class ECIZombie extends Zombie implements Shooter {
    /**
     * The cost of creating an ECIZombie.
     */
    private static final int COST = 250;

    /**
     * The health points of an ECIZombie.
     */
    private static final int HEALTH = 200;

    /**
     * The damage points an ECIZombie can inflict.
     */
    public static final int DAMAGE = 50;

    /**
     * Constructs an ECIZombie with predefined health, cost, and damage values.
     */
    public ECIZombie() {
        super(HEALTH, COST, 0);
    }

    /**
     * Shoots a projectile in the specified lane.
     * 
     * @param lane the lane in which the ECIZombie will shoot
     */
    @Override
    public void shoot(int lane) {
        // Disparar POOmBa
    }
}
