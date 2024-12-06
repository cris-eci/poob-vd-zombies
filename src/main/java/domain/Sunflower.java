// Sunflower
package domain;
public class Sunflower extends Plant implements ResourceGenerator {
    // Produce 25 soles cada 20 segundos
    private static final int COST = 50;
    private static final int HEALTH = 300;

    public Sunflower() {
        super(HEALTH, COST);
    }

    @Override
    public void generateResource(int lane) {
        // Lógica para generar soles
    }
}
