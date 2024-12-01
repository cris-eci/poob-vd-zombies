package domain;

public class ECIZombie extends Zombie implements Shooter {

    public ECIZombie(int line, int xPos) {
        super(line, xPos, 250, 150, 40, 1); // Salud, costo, daño, velocidad
    }

    @Override
    public void act() {
        shoot();
        // Moverse hacia la izquierda
    }

    @Override
    public void shoot() {
        // Crear un proyectil y añadirlo al jardín
        Projectile projectile = new Projectile(line, xPos, damage, -1); // Velocidad negativa
        // Añadir el proyectil al juego
    }
}
