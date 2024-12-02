package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class POOBvsZombies {
    private List<Player> players;
    private List<Entity> entities;
    private String gameMode;
    private GameListener listener;
    
    // Para PlayerVsPlayer
    private int matchTime; // en minutos
    private int halfTime;  // en minutos
    private int initialSetupTime = 2 * 60; // 2 minutos en segundos
    private Timer timer;
    private int timeRemaining; // en segundos

    // Para modos con hordas
    private int numberOfHordes;
    private int hordeDuration; // en minutos
    private int currentHorde;

    private GameListener gameListener;
    private Timer resourceGenerationTimer;
    
    // Constructor para PlayerVsPlayer
    public POOBvsZombies(List<Player> players, int matchTime, String gameMode) {
        this.players = players;
        this.matchTime = matchTime;
        this.gameMode = gameMode;
        this.halfTime = matchTime / 2;
        entities = new ArrayList<>();
    }
    
    // Constructor para modos con hordas
    public POOBvsZombies(List<Player> players, int numberOfHordes, int hordeDuration, String gameMode) {
        this.players = players;
        this.numberOfHordes = numberOfHordes;
        this.hordeDuration = hordeDuration;
        this.gameMode = gameMode;
        entities = new ArrayList<>();
    }

    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    public void startGame() {
        switch (gameMode) {
            case "PlayerVsPlayer":
                startPlayerVsPlayerGame();
                break;
            case "PlayerVsMachine":
            case "MachineVsMachine":
                startGameWithHordes();
                break;
            default:
                throw new IllegalArgumentException("Modo de juego no reconocido: " + gameMode);
        }
    }
    
    
    private void startPlayerVsPlayerGame() {
        // Fase de preparación inicial
        timeRemaining = initialSetupTime;
        notifyInitialSetupTimeUpdate();
    
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                notifyInitialSetupTimeUpdate();
    
                if (timeRemaining <= 0) {
                    timer.cancel();
                    startPlayerVsPlayerRound(1);
                }
            }
        }, 1000, 1000);
    }
    
    private void startPlayerVsPlayerRound(int roundNumber) {
        notifyRoundStart(roundNumber);
    
        timeRemaining = (halfTime) * 60; // Convertir minutos a segundos
        notifyTimeUpdate();
    
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                notifyTimeUpdate();
    
                if (timeRemaining <= 0) {
                    timer.cancel();
                    if (roundNumber == 1) {
                        startPlayerVsPlayerIntermediateSetup();
                    } else {
                        endGame();
                    }
                }
            }
        }, 1000, 1000);
    }
    
    private void startPlayerVsPlayerIntermediateSetup() {
        // Fase de preparación intermedia
        timeRemaining = initialSetupTime;
        notifyInitialSetupTimeUpdate();
    
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                notifyInitialSetupTimeUpdate();
    
                if (timeRemaining <= 0) {
                    timer.cancel();
                    startPlayerVsPlayerRound(2);
                }
            }
        }, 1000, 1000);
    }
    
    private void endGame() {
        if (listener != null) {
            listener.onGameEnd(determineWinner());
        }
    }
    
    // Dentro de la clase POOBvsZombies

    private void startGameWithHordes() {
        // Fase de preparación inicial (2 minutos)
        timeRemaining = initialSetupTime;
        notifyInitialSetupTimeUpdate();
    
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                notifyInitialSetupTimeUpdate();
    
                if (timeRemaining <= 0) {
                    timer.cancel();
                    currentHorde = 1;
                    startNextHorde();
                }
            }
        }, 1000, 1000);
    }
    

    private void startNextHorde() {
        notifyHordeChange(currentHorde); // Notificar el inicio de la horda actual
    
        timeRemaining = hordeDuration * 60; // Convertir minutos a segundos
        notifyTimeUpdate();
    
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                notifyTimeUpdate();
    
                if (timeRemaining <= 0) {
                    timer.cancel();
                    if (currentHorde < numberOfHordes) {
                        currentHorde++;
                        startIntermediateSetup();
                    } else {
                        endGame();
                    }
                }
            }
        }, 1000, 1000);
    }
    

    private void startIntermediateSetup() {
        // Fase de preparación intermedia (2 minutos)
        timeRemaining = initialSetupTime;
        notifyInitialSetupTimeUpdate();
    
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                notifyInitialSetupTimeUpdate();
    
                if (timeRemaining <= 0) {
                    timer.cancel();
                    startNextHorde();
                }
            }
        }, 1000, 1000);
    }
    


    private void notifyTimeUpdate() {
        if (listener != null) {
            listener.onTimeUpdate(timeRemaining);
        }
    }
    
    private void notifyInitialSetupTimeUpdate() {
        if (listener != null) {
            listener.onInitialSetupTimeUpdate(timeRemaining);
        }
    }
    
    private void notifyRoundStart(int roundNumber) {
        if (listener != null) {
            listener.onRoundStart(roundNumber);
        }
    }
    
    private void notifyHordeChange(int hordeNumber) {
        if (listener != null) {
            listener.onHordeChange(hordeNumber);
        }
    }
    
    private String determineWinner() {
        // Implementar lógica para determinar el ganador basado en el puntaje
        return "Resultado del juego";
    }


    public void stopGame() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }
    public interface GameListener {
        void onTimeUpdate(int timeRemaining);            // Durante rondas o hordas
        void onInitialSetupTimeUpdate(int timeRemaining); // Durante fases de preparación
        void onRoundStart(int roundNumber);              // Al iniciar una ronda
        void onGameEnd(String result);                   // Al finalizar el juego
        void onHordeChange(int hordeNumber);             // Al cambiar de horda
    }
    
    
}
