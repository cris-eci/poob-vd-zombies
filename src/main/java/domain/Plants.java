package domain;

public class Plants extends Team {

    public static final String NAME = "Plants";

    public Plants(int initialSuns) {
        super(initialSuns);
    }

    @Override
    public void increaseResourceAmount() {
        resourceCounter += RESOURCE_AMOUNT_INCREASE;
    }

    @Override
    public void selectCharacters() {
        // Implementar selección de plantas
    }

    @Override
    public String getName() {
        return NAME;
    }

}
