package domain;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class POOBvsZombies {
    private List<Player> players;
    private int matchTime; // en segundos
    private int halfTime;
    private Timer timer;
    private int timeRemaining;
    private GameListener listener;

    public POOBvsZombies(List<Player> players, int matchTime) {
        this.players = players;
        this.matchTime = matchTime;
        this.halfTime = matchTime / 2;
        this.timeRemaining = matchTime;
    }

    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    public void startGame() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                if (listener != null) {
                    listener.onTimeUpdate(timeRemaining);
                }

                if (timeRemaining == halfTime && listener != null) {
                    listener.onHalfTime();
                }

                if (timeRemaining <= 0) {
                    timer.cancel();
                    if (listener != null) {
                        listener.onGameEnd(determineWinner());
                    }
                }
            }
        }, 1000, 1000);
    }

    private String determineWinner() {
        // Implementar lógica para determinar el ganador
        return "Empate";
    }

    public void stopGame() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public interface GameListener {
        void onTimeUpdate(int timeRemaining);
        void onHalfTime();
        void onGameEnd(String result);
        void onHordeChange(int hordeNumber);
    }
    
}
