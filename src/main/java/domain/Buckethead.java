// Buckethead
package domain;
public class Buckethead extends Zombie {
    private static final int COST = 200;
    private static final int HEALTH = 800;
    private static final int DAMAGE = 100;

    public Buckethead() {
        super(HEALTH, COST, DAMAGE);
    }
}