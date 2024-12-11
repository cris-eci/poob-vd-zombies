// ECIZombie
package domain;
public class ECIZombie extends Zombie implements Shooter {
    private static final int COST = 250;
    private static final int HEALTH = 200;
    private static final int DAMAGE = 50;

    public ECIZombie() {
        // El ECI Zombie no hace daño, solo se mueve, el daño lo hace el proyectil con su daño DAMAGE.
        super(HEALTH, COST, 0);
    }

    @Override
    public void shoot(int lane) {
        // Disparar POOmBa
    }
}
