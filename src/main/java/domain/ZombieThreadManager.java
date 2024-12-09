package domain;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import presentation.GardenMenu;

public class ZombieThreadManager {

    private POOBvsZombies game;
    private GardenMenu garden;

    public ZombieThreadManager(POOBvsZombies game, GardenMenu garden) {
        this.game = game;
        this.garden = garden;
    }

    public void registerZombie(int row, Zombie zombie, JLabel zombieLabel) {
        Thread t = new Thread(() -> zombieLogic(row, zombie, zombieLabel));
        t.start();
    }

    private void zombieLogic(int row, Zombie zombie, JLabel zombieLabel) {
        while (true) {
            if (!(zombie instanceof Brainstein)){
                
            // Buscar la planta más cercana
            int plantCol = game.getFirstPlantInRow(row);
            if (plantCol == -1) {
                // No hay más plantas, mover hasta el final (col=0) si no está ya allí
                int currentCol = getCurrentColumn(zombieLabel.getX());
                if (currentCol > 0) {
                    moveZombie(row, 0, zombieLabel);
                }
                // Ya está al final, detener el hilo
                break;
            }

            int targetCol = plantCol + 1; 
            moveZombie(row, targetCol, zombieLabel);

            // Ahora el zombi está adyacente a la planta. Atacar.
            attackPlant(row, plantCol, zombie, zombieLabel);
            // Si la planta murió, se remueve y el loop continúa para buscar la siguiente.
            } else {
                // Brainstein no ataca, solo genera recursos
                ((Brainstein) zombie).generateResource(row);
                // Esperar 2 segundos antes de generar otro recurso
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }   
        }
    }

    /**
     * Calcula la columna actual del zombie basándose en su posición X.
     */
    private int getCurrentColumn(int xPosition) {
        int cellWidth = 80;
        int gridStartX = 40;
        return Math.max(0, (xPosition - gridStartX) / cellWidth);
    }

    private void moveZombie(int row, int targetCol, JLabel zombieLabel) {
        int cellWidth = 80;
        int startX = zombieLabel.getX(); // Posición actual X
        int startY = zombieLabel.getY();
        int endX = 40 + targetCol * cellWidth;

        int currentX = startX;
        int currentY = startY;

        while (currentX > endX) {
            currentX -= 5;
            int finalX = currentX;
            int finalY = currentY;
            SwingUtilities.invokeLater(() -> {
                zombieLabel.setLocation(finalX, finalY);
            });
            try {
                // Espera de 150ms para controlar la velocidad de movimiento
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void attackPlant(int row, int plantCol, Zombie zombie, JLabel zombieLabel) {
        // Obtener la planta desde el dominio
        Plant plant = game.getPlantAt(row, plantCol);
        if (plant == null) {
            // Ya no hay planta (otro zombi la mató?), volver
            return;
        }

        // Mientras la planta esté viva, atacarla cada 0.5s
        while (!plant.isDead()) {
            plant.takeDamage(zombie.getDamage());
            if (plant.isDead()) {
                // Planta muerta, remover del dominio y de la interfaz
                game.removeEntity(row, plantCol);
                SwingUtilities.invokeLater(() -> {
                    garden.removePlantAt(row, plantCol);
                });
                break;
            }

            // Esperar 0.5 segundos antes del próximo ataque
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
