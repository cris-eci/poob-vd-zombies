package domain;

public class WallNut extends Plant {

    public WallNut(int line, int xPos) {
        super(line, xPos, 300, 50); // Salud alta y costo
    }

    @Override
    public void act() {
        // No hace nada en cada turno
    }
}
