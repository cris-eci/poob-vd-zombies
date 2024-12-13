package domain;

public class Sunflower extends Plant implements ResourceGenerator {
    private static final int COST = 50;
    private static final int HEALTH = 300;

    public Sunflower() {
        super(HEALTH, COST);
    }

    @Override
    public Resource generateResource(int lane) {
        // Genera un sol de 25 en la posición actual
        return new Resource(Resource.SOL, 25);
    }
}
