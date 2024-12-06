// Basic
package domain;
public class Basic extends Zombie {
    private static final int COST = 100;
    private static final int HEALTH = 100;
    private static final int DAMAGE = 100;

    public Basic() {
        super(HEALTH, COST, DAMAGE);
    }
}