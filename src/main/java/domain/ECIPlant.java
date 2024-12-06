// ECIPlant
package domain;
public class ECIPlant extends Sunflower {
    // ECIPlant es una Sunflower modificada
    // Cost = 75, Health = 150, genera sol de valor 50
    private static final int COST = 75;
    private static final int HEALTH = 150;

    public ECIPlant() {
        super();
        this.health = HEALTH;
        this.cost = COST;
    }

    @Override
    public void generateResource(int lane) {
        // Generar un sol más grande (50)
    }
}