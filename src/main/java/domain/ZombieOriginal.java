package domain;


/**
 * Jugador máquina que controla los zombies utilizando la estrategia original.
 */
public class ZombieOriginal extends MachinePlayer {

    public ZombieOriginal(Team team, String name, int numberOfHordes, int hordeInterval) {
        super(team, name, new ZombiesStrategy(team, numberOfHordes, hordeInterval));
    }
}
