package domain;

public class Brainstein extends Zombie implements ResourceGenerator {
    private static final int COST = 50;
    private static final int HEALTH = 300;

    public Brainstein() {
        super(HEALTH, COST, 0);
    }

    @Override
    public Resource generateResource(int lane) {
        // Genera un cerebro de 25 en la posición actual
        return new Resource(Resource.BRAIN, 25);
    }
}
