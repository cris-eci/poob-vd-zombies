
/**
 * The HumanPlayer class represents a human player in the game.
 * This class is an abstract class that extends the Player class.
 * It provides a constructor to initialize the player's name and 
 * an abstract method to play a turn.
 */
package domain;

/**
 * Constructs a HumanPlayer with the specified name.
 * 
 * @param name the name of the human player
 */
public abstract class HumanPlayer extends Player {

    /**
     * Constructs a HumanPlayer with the specified name.
     * 
     * @param name the name of the human player
     */
    public HumanPlayer(String name) {
        super(name);
    }
    
    /**
     * Sets the score for the human player.
     * This method is not implemented and will throw an UnsupportedOperationException if called.
     * 
     * @throws UnsupportedOperationException if the method is called
     */
    @Override
    public void setScore() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setScore'");
    }

    /**
     * Abstract method to play a turn.
     * This method must be implemented by subclasses to define the behavior of playing a turn.
     */
    public abstract void playTurn();
    
}
