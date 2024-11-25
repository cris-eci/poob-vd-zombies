package presentation;
import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class PlayerVsPlayer extends JFrame {

    public PlayerVsPlayer() {
        // Configuración básica de la ventana
        setTitle("Player vs Player");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Para centrar la ventana en la pantalla

        // Establecer el fondo con la imagen
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // Para poder posicionar los componentes de forma absoluta
        setContentPane(backgroundPanel);

        // Crear y agregar los labels y text fields necesarios
        addComponents(backgroundPanel);
    }

    private void addComponents(JPanel panel) {
        // Label "Player One"
        JLabel playerOneLabel = new JLabel("Player One");
        playerOneLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerOneLabel.setForeground(Color.WHITE);
        playerOneLabel.setBounds(200, 250, 150, 30);
        panel.add(playerOneLabel);

        // Text Field para el nombre del Jugador Uno
        JTextField playerOneName = new JTextField();
        playerOneName.setBounds(200, 290, 200, 30);
        panel.add(playerOneName);

        // Label "Player Two"
        JLabel playerTwoLabel = new JLabel("Player Two");
        playerTwoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerTwoLabel.setForeground(Color.WHITE);
        playerTwoLabel.setBounds(500, 250, 150, 30);
        panel.add(playerTwoLabel);

        // Text Field para el nombre del Jugador Dos
        JTextField playerTwoName = new JTextField();
        playerTwoName.setBounds(500, 290, 200, 30);
        panel.add(playerTwoName);

        // Botón para empezar
        JButton startButton = new JButton("¡START!");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBounds(375, 400, 150, 50);
        panel.add(startButton);

        // Label "Select your plants" para Player One
        JLabel selectPlantsLabel = new JLabel("Select your plants");
        selectPlantsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        selectPlantsLabel.setForeground(Color.WHITE);
        selectPlantsLabel.setBounds(100, 150, 150, 30);
        panel.add(selectPlantsLabel);

        // Label "Select your zombies" para Player Two
        JLabel selectZombiesLabel = new JLabel("Select your zombies");
        selectZombiesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        selectZombiesLabel.setForeground(Color.WHITE);
        selectZombiesLabel.setBounds(700, 150, 150, 30);
        panel.add(selectZombiesLabel);
    }

    // Panel personalizado para pintar la imagen de fondo
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Cargar la imagen desde la ruta especificada
            URL imageUrl = getClass().getResource("/presentation/img/pvsp.png");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                backgroundImage = icon.getImage();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibujar la imagen en todo el fondo del panel
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        // Crear y mostrar la ventana
        SwingUtilities.invokeLater(() -> {
            PlayerVsPlayer frame = new PlayerVsPlayer();
            frame.setVisible(true);
        });
    }
}