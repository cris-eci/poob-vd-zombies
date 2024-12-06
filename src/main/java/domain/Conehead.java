// Conehead
package domain;
public class Conehead extends Zombie {
    private static final int COST = 150;
    private static final int HEALTH = 380;
    private static final int DAMAGE = 100;

    public Conehead() {
        super(HEALTH, COST, DAMAGE);
    }
}