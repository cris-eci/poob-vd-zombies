// ECIZombie
package domain;
public class ECIZombie extends Zombie implements Shooter {
    private static final int COST = 250;
    private static final int HEALTH = 200;
    private static final int DAMAGE = 50;

    public ECIZombie() {
        super(HEALTH, COST, DAMAGE);
    }

    @Override
    public void shoot(int lane) {
        // Disparar POOmBa
    }
}