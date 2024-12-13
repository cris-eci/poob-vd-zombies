package domain;

public abstract class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }
    
    @Override
    public void setScore() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setScore'");
    }

    public abstract void playTurn();
    
}
