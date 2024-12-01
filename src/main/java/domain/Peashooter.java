package domain;

public class Peashooter extends Plant implements Shooter {

    private int damage;

    public Peashooter(int line, int xPos) {
        super(line, xPos, 100, 100); // Salud y costo
        this.damage = 20;
    }

    @Override
    public void act() {
        shoot();
    }

    @Override
    public void shoot() {
        // Crear un proyectil y añadirlo al jardín
        Projectile projectile = new Projectile(line, xPos, damage, 1); // Velocidad 1
        // Añadir el proyectil al juego
    }
}
