package domain;

public abstract class MachinePlayer extends Player {
    protected String[][] strategy;
    
    public MachinePlayer(Team team, String name) {
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

}
