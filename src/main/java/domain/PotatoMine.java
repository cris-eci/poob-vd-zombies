package domain;

public class PotatoMine extends Plant {

    private int activationTime;
    private boolean isActive;

    public PotatoMine(int line, int xPos) {
        super(line, xPos, 100, 25); // Salud y costo
        this.activationTime = 14; // Tiempo de activación en segundos
        this.isActive = false;
    }

    @Override
    public void act() {
        if (!isActive) {
            activationTime--;
            if (activationTime <= 0) {
                isActive = true;
                // Cambiar la imagen a la activa
            }
        } else {
            // Verificar si hay un zombie en la misma posición para explotar
        }
    }
}
