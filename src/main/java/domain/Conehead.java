package domain;

public class Conehead extends Zombie {

    public Conehead(int line, int xPos) {
        super(line, xPos, 300, 150, 50, 1); // Mayor salud debido al cono
    }

    @Override
    public void act() {
        // Moverse y atacar
    }
}
