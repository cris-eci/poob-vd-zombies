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

        // Text Field para el nombre del Jugador Uno
        JTextField playerOneName = new JTextField("Name player one");
        playerOneName.setFont(new Font("Arial", Font.BOLD, 13)); // Texto en negrita y tamaño 13
        playerOneName.setBounds(270, 380, 150, 30);
        playerOneName.setBorder(null); // Eliminar borde
        playerOneName.setBackground(new Color(228, 206, 171));
        playerOneName.setForeground(new Color(134, 119, 94)); // Color del texto
        panel.add(playerOneName);

        // Text Field para el nombre del Jugador Dos
        JTextField playerTwoName = new JTextField("Name player two");
        playerTwoName.setFont(new Font("Arial", Font.BOLD, 13)); // Texto en negrita y tamaño 13
        playerTwoName.setBorder(null); // Eliminar borde
        playerTwoName.setBounds(465, 380, 150, 30);
        playerTwoName.setBackground(new Color(228, 206, 171));
        playerTwoName.setForeground(new Color(134, 119, 94)); // Color del texto
        panel.add(playerTwoName);


        // Text Field para el tiempo de la partida
        JTextField matchTime = new JTextField("Time");
        matchTime.setFont(new Font("Arial", Font.BOLD, 11)); // Texto en negrita y tamaño 13
        matchTime.setBounds(350, 455, 40, 20); // Posición abajo de playerOneName y un poco más a la derecha
        matchTime.setBorder(null); // Eliminar borde
        matchTime.setBackground(new Color(228, 206, 171));
        matchTime.setForeground(new Color(134, 119, 94)); // Color del texto
        panel.add(matchTime);

        // Botón para empezar
        JButton startButton = new JButton("¡START!");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBounds(465, 448, 150, 30);
        startButton.setBackground(Color.ORANGE);
        panel.add(startButton);

        // Label "Select your plants" para Player One
        JLabel selectPlantsLabel = new JLabel("Select your plants");
        selectPlantsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectPlantsLabel.setForeground(Color.WHITE);
        selectPlantsLabel.setBounds(46, 420, 150, 30);
        panel.add(selectPlantsLabel);

        // Label "Select your zombies" para Player Two
        JLabel selectZombiesLabel = new JLabel("Select your zombies");
        selectZombiesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectZombiesLabel.setForeground(Color.WHITE);
        selectZombiesLabel.setBounds(690, 420, 170, 30);
        panel.add(selectZombiesLabel);

        JLabel gameModeLabel = new JLabel("<html><div style='width:385px;'>" +

                "In this mode, players control plants and zombies, defining strategies." +
                " <br> The plant team has 2 minutes to set up and must withstand zombie <br>waves configured by the zombie team. Each player decides their <br>team's starting resources."
                +
                "</div></html>");
        gameModeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gameModeLabel.setForeground(Color.WHITE);
        gameModeLabel.setBounds(245, 520, 380, 100); // Ajusta la altura
        panel.add(gameModeLabel);
        

        // Botón "Set Initial amount of suns" para Player One
        // Crear un JTextField para ingresar el monto inicial de soles
        JTextField setSunsField = new JTextField("Amount of suns");
        setSunsField.setFont(new Font("Arial", Font.BOLD, 12));
        setSunsField.setBounds(82, 613, 90, 20); // Mismas dimensiones que el botón
        setSunsField.setBackground(new Color(253, 210, 1)); // Color de fondo personalizado
        setSunsField.setForeground(Color.WHITE); // Asegúrate de que el texto sea legible
        setSunsField.setBorder(null); // Eliminar borde
        panel.add(setSunsField);

        // Botón "Set Initial amount of brains" para Player Two
        // Botón "Set Initial amount of brains" convertido en JTextField para Player Two
        JTextField setBrainsField = new JTextField("Amount of brains");
        setBrainsField.setFont(new Font("Arial", Font.BOLD, 12)); // Texto en negrita
        setBrainsField.setBounds(730, 613, 100, 20); // Mismas dimensiones que el botón
        setBrainsField.setBackground(new Color(240, 162, 198)); // Color de fondo personalizado
        setBrainsField.setForeground(Color.WHITE); // Texto en blanco
        setBrainsField.setBorder(null); // Eliminar borde
        panel.add(setBrainsField);

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
