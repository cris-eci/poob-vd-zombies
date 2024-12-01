package presentation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class POOBvsZombiesGUI extends JFrame {

    private static Clip clip;

    public POOBvsZombiesGUI() {
        setTitle("POOBvsZombies");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window on the screen

        // Custom panel with background image
        JPanel panel = new JPanel() {
            Image backgroundImage = new ImageIcon("resources/images/menu/modalityMenu.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); // Absolute positioning

        // Create buttons with text on them
        JButton PlayerVsPlayerButton = createMenuButton("Player vs Player", "SURVIVAL", 242);
        JButton playerVsMachineButton = createMenuButton("Player vs Machine", "ORIGINAL", 362);
        JButton machineVsMachineButton = createMenuButton("Machine vs Machine", "SURVIVAL", 472);

        // Add buttons to panel
        panel.add(PlayerVsPlayerButton);
        panel.add(playerVsMachineButton);
        panel.add(machineVsMachineButton);

        // Add action listeners for each button
        PlayerVsPlayerButton.addActionListener(e -> {
            // Open Player vs Player Menu
            PlayerVsPlayer PlayerVsPlayerMenu = new PlayerVsPlayer();
            PlayerVsPlayerMenu.setVisible(true);
            dispose();
        });

        playerVsMachineButton.addActionListener(e -> {
            // Open Player vs Machine Menu
            PlayerVSMachine playerVsMachineMenu = new PlayerVSMachine();
            playerVsMachineMenu.setVisible(true);
            dispose(); // Close the main menu
        });

        machineVsMachineButton.addActionListener(e -> {
            // Open Machine vs Machine menu
            MachineVSMachine machineVSMachineMenu = new MachineVSMachine();
            machineVSMachineMenu.setVisible(true);
            dispose();
        });

        // Add panel to frame
        add(panel);
        addTopRightButtons(panel);

        // Play background music if it is not already playing
        playMusic("resources/sound/pvzSound.wav");
    }

    // Helper method to create buttons with consistent styling
    private static JButton createMenuButton(String mainText, String subText, int yPosition) {
        JButton button = new JButton();

        // Use HTML to create multiline text
        button.setText("<html><center>" + mainText + "<br><font color='#FFFF00' size='4'>" + subText + "</font></center></html>");

        button.setBounds(343, yPosition, 250, 90);
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        button.setBackground(new Color(110, 52, 48)); // Button background
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(120, 60, 50)); // Slightly lighter tan
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(110, 52, 48)); // Original tan
            }
        });

        return button;
    }

    private void addTopRightButtons(JPanel panel) {
        String[] buttonImagePaths = {
                "resources/images/buttons/import-icon.png", // Import
                "resources/images/buttons/open-icon.png" // Open
        };

        int x = 40;
        int y = 5;
        int buttonSize = 40;

        for (String imagePath : buttonImagePaths) {
            ImageIcon icon = new ImageIcon(imagePath);
            JButton button = new JButton(
                    new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
            button.setBounds(x, y, buttonSize, buttonSize);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            // Add action events to buttons

            panel.add(button);
            x += 60; // Adjust X position for the next button
        }
    }

    // Method to play background music
    private void playMusic(String filePath) {
        if (clip == null) {
            try {
                File musicFile = new File(filePath);
                if (musicFile.exists()) {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
                } else {
                    System.out.println("The specified music file does not exist: " + filePath);
                }
            } catch (UnsupportedAudioFileException | LineUnavailableException | java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            POOBvsZombiesGUI POOBvsZombiesGUI = new POOBvsZombiesGUI();
            POOBvsZombiesGUI.setVisible(true);
        });
    }
}
