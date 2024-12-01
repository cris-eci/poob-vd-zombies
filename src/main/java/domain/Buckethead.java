package domain;

public class Buckethead extends Zombie {

    public Buckethead(int line, int xPos) {
        super(line, xPos, 400, 200, 50, 1); // Mayor salud debido al cubo
    }

    @Override
    public void act() {
        // Moverse y atacar
    }
}
