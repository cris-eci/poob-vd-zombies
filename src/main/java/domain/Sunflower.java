package domain;

public class Sunflower extends Plant implements ResourceGenerator {

    public Sunflower(int line, int xPos) {
        super(line, xPos, 100, 50); // Salud y costo
    }

    @Override
    public void act() {
        generateResource();
    }

    @Override
    public void generateResource() {
        // Generar un sol y añadirlo al jardín
        Resource sun = new Resource(line, xPos, "sun");
        // Añadir el recurso al juego
    }
}
