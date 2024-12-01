package domain;

public class Basic extends Zombie {

    public Basic(int line, int xPos) {
        super(line, xPos, 200, 100, 50, 1); // Salud, costo, daño, velocidad
    }

    @Override
    public void act() {
        // Moverse hacia la izquierda y atacar plantas si las encuentra
    }
}
