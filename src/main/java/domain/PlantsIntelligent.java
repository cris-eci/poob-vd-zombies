package domain;



/**
 * Jugador máquina que controla las plantas utilizando una estrategia inteligente.
 */
public class PlantsIntelligent extends MachinePlayer {

    public PlantsIntelligent(Team team, String name) {
        super(team, name, new PlantsStrategy(team));
    }
}
