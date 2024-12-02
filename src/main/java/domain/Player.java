package domain;

/**
 * Clase abstracta que representa a un jugador en el juego.
 */
public abstract class Player {
    protected String name;
    protected int score;
    protected Team team;

    public Player(Team team, String name){
        this.team = team;
        this.name = name;
        this.score = 0;
    }

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public String getTeamName(){
        return team.getName();
    }

    public Team getTeam() {
        return team;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addCharacter(Entity character) {
        team.getCharacters().add(character);
    }

    /**
     * Método para que el jugador recoja un recurso.
     */
    public void collectResource(Resource resource) {
        int currentAmount = team.getResourceCounterAmount();
        currentAmount += resource.getValue();
        team.setResourceCounter(currentAmount);
    }

    
    /**
     * Método abstracto para que el jugador realice su turno.
     */
    public abstract void playTurn();
}
