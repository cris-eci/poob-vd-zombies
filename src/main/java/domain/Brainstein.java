package domain;

public class Brainstein extends Zombie implements ResourceGenerator {

    public Brainstein(int line, int xPos) {
        super(line, xPos, 300, 200, 0, 0); // No se mueve ni ataca
    }

    @Override
    public void act() {
        generateResource();
    }

    @Override
    public void generateResource() {
        // Generar un cerebro y añadirlo al jardín
        Resource brain = new Resource(line, xPos, "brain");
        // Añadir el recurso al juego
    }
}
