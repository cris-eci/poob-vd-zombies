package domain;

public class ECIPlant extends Sunflower {

    public ECIPlant(int line, int xPos) {
        super(line, xPos);
    }

    @Override
    public void generateResource() {
        // Generar un sol más grande (mayor valor)
        Resource bigSun = new Resource(line, xPos, "bigSun");
        // Añadir el recurso al juego
    }
}
