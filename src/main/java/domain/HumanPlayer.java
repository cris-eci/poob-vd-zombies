package domain;

/**
 * Clase abstracta que representa a un jugador humano.
 */
public abstract class HumanPlayer extends Player {

    public HumanPlayer(Team team, String name) {
        super(team, name);
    }

    @Override
    public abstract void playTurn();
}
