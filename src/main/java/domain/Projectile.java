package domain;

/**
 * Clase que representa un proyectil.
 */
public class Projectile extends Entity {
    private int damage;
    private int speed;

    public Projectile(int line, int xPos, int damage, int speed) {
        super(line, xPos);
        this.damage = damage;
        this.speed = speed;
    }

    /**
     * Método para mover el proyectil.
     */
    public void move() {
        xPos += speed;
        // Verificar colisiones
    }

    public int getDamage() {
        return damage;
    }
}
