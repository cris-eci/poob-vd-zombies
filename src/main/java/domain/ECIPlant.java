package domain;

public class ECIPlant extends Sunflower {
    private static final int COST = 75;
    private static final int HEALTH = 150;
    private boolean extraSunsGenerated = false;

    public ECIPlant() {
        super();
        this.health = HEALTH;
        this.cost = COST;
    }

    @Override
    public Resource generateResource(int lane) {
        // Genera un sol grande de 50
        return new Resource(Resource.BIG_SOL, 50) ;
    }

    public boolean hasGeneratedExtraSuns() {
        return extraSunsGenerated;
    }

    public void setExtraSunsGenerated(boolean flag) {
        this.extraSunsGenerated = flag;
    }
}
