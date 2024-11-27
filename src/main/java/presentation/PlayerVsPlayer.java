package presentation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
        // Añadir un FocusListener para el JTextField de nombre del Jugador Uno
        playerOneName.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (playerOneName.getText().equals("Name player one")) {
                    playerOneName.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (playerOneName.getText().isEmpty()) {
                    playerOneName.setText("Name player one");
                }
            }
        });


        // Text Field para el nombre del Jugador Dos
        JTextField playerTwoName = new JTextField("Name player two");
        playerTwoName.setFont(new Font("Arial", Font.BOLD, 13)); // Texto en negrita y tamaño 13
        playerTwoName.setBorder(null); // Eliminar borde
        playerTwoName.setBounds(465, 380, 150, 30);
        playerTwoName.setBackground(new Color(228, 206, 171));
        playerTwoName.setForeground(new Color(134, 119, 94)); // Color del texto
        panel.add(playerTwoName);
        
        // Añadir un FocusListener para el JTextField de nombre del Jugador Dos
        playerTwoName.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (playerTwoName.getText().equals("Name player two")) {
                    playerTwoName.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (playerTwoName.getText().isEmpty()) {
                    playerTwoName.setText("Name player two");
                }
            }
        });

        // Text Field para el tiempo de la partida
        JTextField matchTime = new JTextField("Time");
        matchTime.setFont(new Font("Arial", Font.BOLD, 11)); // Texto en negrita y tamaño 13
        matchTime.setBounds(350, 455, 40, 20); // Posición abajo de playerOneName y un poco más a la derecha
        matchTime.setBorder(null); // Eliminar borde
        matchTime.setBackground(new Color(228, 206, 171));
        matchTime.setForeground(new Color(134, 119, 94)); // Color del texto
        panel.add(matchTime);
        // Añadir un FocusListener para el JTextField de tiempo de partida
        matchTime.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
            if (matchTime.getText().equals("Time")) {
                matchTime.setText("");
            }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
            if (matchTime.getText().isEmpty()) {
                matchTime.setText("Time");
            }
            }
        });
        // Botón para empezar
        JButton startButton = new JButton("¡START!");
        startButton.setBounds(462, 449, 160, 30);

        startButton.setBackground(Color.ORANGE);

        startButton.setForeground(Color.WHITE); // Establecer el color del texto a blanco

        panel.add(startButton);
        startButton.setBackground(Color.ORANGE);
        panel.add(startButton);

        // Label "Select your plants" para Player One
        JLabel selectPlantsLabel = new JLabel("Select your plants");
        selectPlantsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectPlantsLabel.setForeground(Color.WHITE);
        selectPlantsLabel.setBounds(46, 405, 150, 30);
        panel.add(selectPlantsLabel);

        // Reemplaza la sección que agrega las plantas y zombies en tu código `PlayerVsPlayer` con lo siguiente:

        // Crear y agregar los JPanels para las plantas
        JPanel plantsPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 filas, 2 columnas, espacio de 10px
        plantsPanel.setBounds(65, 440, 100, 160); // Ajusta la posición y tamaño según sea necesario
        plantsPanel.setOpaque(false); // Hacer el panel transparente para ver el fondo

        String[] plantImages = {
            "resources/images/plants/Sunflower/Sunflower.jpg",
            "resources/images/plants/Peashooter/Peashooter.jpg",
            "resources/images/plants/WallNut/Wall-nutGrass.jpg",
            "resources/images/plants/PotatoMine/Potato_MineGrass.jpg",
            "resources/images/plants/ECIPlant/ECIPlant.png"
        };

        // Crear y agregar los JPanels con imágenes de las plantas
        for (String imagePath : plantImages) {
            JPanel plantPanel = new JPanel() {
                private Image backgroundImage;
                private Color overlayColor = new Color(255, 0, 0, 128); // Rojo translúcido inicial

                {
                    // Cargar la imagen desde la ruta especificada
                    ImageIcon icon = new ImageIcon(imagePath);
                    backgroundImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

                    // Añadir un MouseListener para alternar el color al hacer clic
                    addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            if (overlayColor.equals(new Color(255, 0, 0, 128))) {
                                overlayColor = new Color(0, 255, 0, 128); // Verde translúcido
                            } else {
                                overlayColor = new Color(255, 0, 0, 128); // Rojo translúcido
                            }
                            repaint(); // Repintar para actualizar el color
                        }
                    });
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Dibujar la imagen de fondo
                    if (backgroundImage != null) {
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    }
                    // Dibujar el rectángulo translúcido
                    g.setColor(overlayColor);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            plantPanel.setPreferredSize(new Dimension(50, 50));
            plantPanel.setOpaque(false); // Transparente para ver el fondo
            plantsPanel.add(plantPanel); // Agregar el panel al contenedor de plantas
        }
        panel.add(plantsPanel);


        // Label "Select your zombies" para Player Two
        JLabel selectZombiesLabel = new JLabel("Select your zombies");
        selectZombiesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectZombiesLabel.setForeground(Color.WHITE);
        selectZombiesLabel.setBounds(690, 405, 170, 30);
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

        // Crear un panel para contener los JPanels de los zombies
        JPanel zombiesPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 filas, 2 columnas, espacio de 10px
        zombiesPanel.setBounds(710, 440, 100, 160); // Ajusta la posición y tamaño según sea necesario
        zombiesPanel.setOpaque(false); // Hacer el panel transparente para ver el fondo

        String[] zombieImages = {
            "resources/images/zombies/Basic/Basic.jpg",
            "resources/images/zombies/Conehead/Conehead.jpg",
            "resources/images/zombies/BucketHead/Buckethead.jpg",
            "resources/images/zombies/ECIZombie/ECIZombie.png",
            "resources/images/zombies/Brainstein/brainsteinGarden.jpeg"
        };

        // Crear y agregar los JPanels con imágenes de los zombies
        for (String imagePath : zombieImages) {
            JPanel zombiePanel = new JPanel() {
                private Image backgroundImage;
                private Color overlayColor = new Color(255, 0, 0, 128); // Rojo translúcido inicial

                {
                    // Cargar la imagen desde la ruta especificada
                    ImageIcon icon = new ImageIcon(imagePath);
                    backgroundImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

                    // Añadir un MouseListener para alternar el color al hacer clic
                    addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            if (overlayColor.equals(new Color(255, 0, 0, 128))) {
                                overlayColor = new Color(0, 255, 0, 128); // Verde translúcido
                            } else {
                                overlayColor = new Color(255, 0, 0, 128); // Rojo translúcido
                            }
                            repaint();
                        }
                    });
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Dibujar la imagen de fondo
                    if (backgroundImage != null) {
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    }
                    // Dibujar el rectángulo translúcido
                    g.setColor(overlayColor);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            zombiePanel.setPreferredSize(new Dimension(50, 50));
            zombiePanel.setOpaque(false);
            zombiesPanel.add(zombiePanel);
        }
        panel.add(zombiesPanel);


        // Botón "Set Initial amount of suns" para Player One
        // Crear un JTextField para ingresar el monto inicial de soles
        JTextField setSunsField = new JTextField("Amount of suns");
        setSunsField.setFont(new Font("Arial", Font.BOLD, 12));
        setSunsField.setBounds(82, 613, 90, 20); // Mismas dimensiones que el botón
        setSunsField.setBackground(new Color(253, 210, 1)); // Color de fondo personalizado
        setSunsField.setForeground(Color.WHITE); // Asegúrate de que el texto sea legible
        setSunsField.setBorder(null); // Eliminar borde
        panel.add(setSunsField);
        // Añadir un FocusListener para el JTextField de soles
        setSunsField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
            if (setSunsField.getText().equals("Amount of suns")) {
                setSunsField.setText("");
            }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
            if (setSunsField.getText().isEmpty()) {
                setSunsField.setText("Amount of suns");
            }
            }
        });

        // Botón "Set Initial amount of brains" para Player Two
        // Botón "Set Initial amount of brains" convertido en JTextField para Player Two
        JTextField setBrainsField = new JTextField("Amount of brains");
        setBrainsField.setFont(new Font("Arial", Font.BOLD, 12)); // Texto en negrita
        setBrainsField.setBounds(730, 613, 100, 20); // Mismas dimensiones que el botón
        setBrainsField.setBackground(new Color(240, 162, 198)); // Color de fondo personalizado
        setBrainsField.setForeground(Color.WHITE); // Texto en blanco
        setBrainsField.setBorder(null); // Eliminar borde
        panel.add(setBrainsField);
        // Añadir un FocusListener para el JTextField de cerebros
        setBrainsField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (setBrainsField.getText().equals("Amount of brains")) {
                    setBrainsField.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (setBrainsField.getText().isEmpty()) {
                    setBrainsField.setText("Amount of brains");
                }
            }
        });

    }

    // Panel personalizado para pintar la imagen de fondo
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Cargar la imagen desde la ruta especificada
            backgroundImage = new ImageIcon("resources/images/menu/PlayerVsPlayerMenu.png").getImage();
            setLayout(null); // Configurar el layout del panel
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibujar la imagen en el fondo del panel
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