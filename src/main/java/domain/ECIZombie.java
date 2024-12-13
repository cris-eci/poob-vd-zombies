// ECIZombie
package domain;
public class ECIZombie extends Zombie implements Shooter {
    public static final int COST = 250;
    public static final int HEALTH = 200;
    public static final int DAMAGE = 50;

    public ECIZombie() {
        super(HEALTH, COST, 0);
    }

    @Override
    public void shoot(int lane) {
        // Disparar POOmBa
    }
}
