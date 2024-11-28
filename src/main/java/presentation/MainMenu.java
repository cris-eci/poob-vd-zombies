package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public class MainMenu extends JFrame {

    private Thread audioThread;

    public MainMenu() {
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
            stopAudio();
            dispose(); 
        });

        playerVsMachineButton.addActionListener(e -> {
            // Open Player vs Machine Menu
            PlayerVSMachine playerVsMachineMenu = new PlayerVSMachine();
            playerVsMachineMenu.setVisible(true);
            stopAudio();
            dispose(); // Close the main menu
        });

        machineVsMachineButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Opening Machine vs Machine Menu");
        });

        // Add panel to frame
        add(panel);

        // Start background music
        startBackgroundMusic("resources/sound/pvzSound.mp3");
    }

    private void startBackgroundMusic(String filePath) {
        audioThread = new Thread(new Sound(filePath));
        audioThread.start();
    }

    private void stopAudio() {
        if (audioThread != null && audioThread.isAlive()) {
            audioThread.interrupt();
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}
