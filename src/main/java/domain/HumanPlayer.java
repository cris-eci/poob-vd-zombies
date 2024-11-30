package domain;

public abstract class HumanPlayer extends Player {

    public HumanPlayer(Team team, String name) {
        super(team, name);
    }
    
    @Override
    public void setScore() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setScore'");
    }

    @Override
    public void addCharacter() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCharacter'");
    }

    public abstract void playTurn();
    
}
