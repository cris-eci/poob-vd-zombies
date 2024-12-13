// PotatoMine.java
package domain;

public class PotatoMine extends Plant {
    private static final int COST = 25;
    private static final int HEALTH = 100;
    public static final int ACTIVATION_TIME = 14; // En segundos
    private boolean activated = false;

    public PotatoMine() {
        super(HEALTH, COST);
    }

    public void activate() {
        activated = true;
    }

    public boolean isActivated() {
        return activated;
    }

    public void explode() {
        if (activated) {
            // Lógica de explosión
            System.out.println("PotatoMine ha explotado.");
            // Implementa la lógica de daño a los zombies adyacentes
        }
    }
}
