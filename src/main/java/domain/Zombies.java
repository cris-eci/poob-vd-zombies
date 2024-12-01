package domain;


public class Zombies extends Team {

    public static final String NAME = "Zombies";

    public Zombies(int initialBrains) {
        super(initialBrains);
    }

    @Override
    public void increaseResourceAmount() {
        resourceCounter += RESOURCE_AMOUNT_INCREASE;
    }

    @Override
    public void selectCharacters() {
        // Implementar selección de zombies
    }

    @Override
    public String getName() {
        return NAME;
    }

}
