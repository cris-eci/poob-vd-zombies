// Brainstein
package domain;
public class Brainstein extends Zombie implements ResourceGenerator {
    private static final int COST = 50;
    private static final int HEALTH = 300;
    // Daño = 0 (no ataca)
    public Brainstein() {
        super(HEALTH, COST, 0);
    }

    @Override
    public void generateResource(int lane) {
        // Generar cerebros
    }
}
