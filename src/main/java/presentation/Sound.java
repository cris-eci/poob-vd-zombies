package presentation;

import javazoom.jl.player.Player;
import java.io.FileInputStream;

public class Sound implements Runnable {
    private String filePath;
    private Player player;

    public Sound(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            player = new Player(fis);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (player != null) {
            player.close();
        }
    }
}
