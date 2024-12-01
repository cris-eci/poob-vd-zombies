package domain;

import domain.Team;
import domain.Zombie;
import domain.Basic;
import domain.Conehead;
import domain.Buckethead;
import java.util.Random;

/**
 * Estrategia para el jugador máquina de zombies.
 * Genera hordas de zombies aleatoriamente según el número y tiempo especificados.
 */
public class ZombiesStrategy implements Strategy {
    private Team team;
    private int numberOfHordes;
    private int hordeInterval; // En segundos
    private Random random;

    public ZombiesStrategy(Team team, int numberOfHordes, int hordeInterval) {
        this.team = team;
        this.numberOfHordes = numberOfHordes;
        this.hordeInterval = hordeInterval;
        this.random = new Random();
    }

    @Override
    public void executeStrategy() {
        // Generar hordas de zombies de forma aleatoria
        // Implementar lógica para generar hordas en intervalos de tiempo
        // En cada horda, generar zombies en líneas aleatorias

        // Ejemplo simplificado:
        for (int horde = 0; horde < numberOfHordes; horde++) {
            // Esperar el intervalo de hordas (esto debe manejarse con temporizadores en el juego real)
            // ...

            // Generar zombies en posiciones aleatorias
            for (int i = 0; i < 3; i++) { // Cantidad de zombies por horda
                if (team.getResourceCounterAmount() >= 100) { // Costo de BasicZombie
                    int line = random.nextInt(5); // Línea aleatoria (0-4)
                    Zombie zombie = new Basic(line, 9); // Columna 9 (última)
                    team.addCharacter(zombie);
                    team.setResourceCounter(team.getResourceCounterAmount() - zombie.getCost());
                }
            }
        }
    }
}
