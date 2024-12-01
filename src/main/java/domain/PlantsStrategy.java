package domain;

import domain.Team;
import domain.Plant;
import domain.Sunflower;
import domain.Peashooter;
import domain.WallNut;

/**
 * Estrategia para el jugador máquina de plantas.
 * Construye una defensa bajo la estructura "producción - ataque - defensa".
 */
public class PlantsStrategy implements Strategy {
    private Team team;

    public PlantsStrategy(Team team) {
        this.team = team;
    }

    @Override
    public void executeStrategy() {
        // Estrategia "producción - ataque - defensa"
        // Colocar girasoles en la primera columna
        for (int line = 0; line < 5; line++) {
            if (team.getResourceCounterAmount() >= 50) { // Costo de Sunflower
                Plant sunflower = new Sunflower(line, 0);
                team.addCharacter(sunflower);
                team.setResourceCounter(team.getResourceCounterAmount() - sunflower.getCost());
            }
        }

        // Colocar peashooters en la segunda columna
        for (int line = 0; line < 5; line++) {
            if (team.getResourceCounterAmount() >= 100) { // Costo de Peashooter
                Plant peashooter = new Peashooter(line, 1);
                team.addCharacter(peashooter);
                team.setResourceCounter(team.getResourceCounterAmount() - peashooter.getCost());
            }
        }

        // Colocar wall-nuts en la tercera columna
        for (int line = 0; line < 5; line++) {
            if (team.getResourceCounterAmount() >= 50) { // Costo de WallNut
                Plant wallNut = new WallNut(line, 2);
                team.addCharacter(wallNut);
                team.setResourceCounter(team.getResourceCounterAmount() - wallNut.getCost());
            }
        }

        // Simplificar la estructura si no hay suficientes recursos o plantas disponibles
    }
}
