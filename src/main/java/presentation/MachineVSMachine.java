package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MachineVSMachine extends JFrame {
    private List<PlantPanel> plantPanelsList = new ArrayList<>();
    private List<ZombiePanel> zombiePanelsList = new ArrayList<>();
    private JTextField time, quantity, setSunsField, setBrainsField;
    private JButton startButton;

    public MachineVSMachine() {
        // Configuración básica de la ventana
        setTitle("Machine vs Machine");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel de fondo personalizado
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // Posicionamiento absoluto
        setContentPane(backgroundPanel);

        // Crear y agregar los componentes necesarios
        addComponents(backgroundPanel);
        addTopRightButtons(backgroundPanel);
    }

    private void addComponents(JPanel panel) {

        // Campo de texto para el tiempo
        time = new JTextField("Time");
        time.setFont(new Font("Arial", Font.BOLD, 13)); // Texto en negrita, tamaño 13
        time.setBounds(385, 415, 40, 15);
        time.setBorder(null); // Eliminar borde
        time.setBackground(new Color(241, 161, 198));
        time.setForeground(Color.BLACK); // Color del texto
        panel.add(time);
        // Añadir un FocusListener para el campo de tiempo
        time.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (time.getText().equals("Time")) {
                    time.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (time.getText().isEmpty()) {
                    time.setText("Time");
                }
                checkFields();
            }
        });

        // Campo de texto para la cantidad
        quantity = new JTextField("Number");
        quantity.setFont(new Font("Arial", Font.BOLD, 13)); // Texto en negrita, tamaño 13
        quantity.setBorder(null); // Eliminar borde
        quantity.setBounds(455, 415, 55, 15);
        quantity.setBackground(new Color(241, 161, 198));
        quantity.setForeground(Color.BLACK); // Color del texto
        panel.add(quantity);

        // Añadir un FocusListener para el campo de cantidad
        quantity.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (quantity.getText().equals("Number")) {
                    quantity.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (quantity.getText().isEmpty()) {
                    quantity.setText("Number");
                }
                checkFields();
            }
        });


        // Botón START
        startButton = new JButton("¡START!");
        startButton.setBounds(395, 460, 90, 38);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.WHITE); // Color del texto
        startButton.setEnabled(false); // Inicialmente deshabilitado
        panel.add(startButton);

        // Añadir ActionListener al botón start
        startButton.addActionListener(e -> {
            // Recopilar las plantas seleccionadas
            List<String> selectedPlants = new ArrayList<>();
            List<String> selectedZombies = new ArrayList<>();

            // Iterar sobre los paneles de plantas
            for (PlantPanel plantPanel : plantPanelsList) {
                if (plantPanel.isSelected()) {
                    selectedPlants.add(plantPanel.getPlantPath());
                }
            }

            // Iterar sobre los paneles de zombies
            for (ZombiePanel zombiePanel : zombiePanelsList) {
                if (zombiePanel.isSelected()) {
                    selectedZombies.add(zombiePanel.getZombiePath());
                }
            }

            // Abrir el GardenMenu con las plantas y zombies seleccionados
            new GardenMenu(selectedPlants.toArray(new String[0]), selectedZombies.toArray(new String[0]), "MachineVsMachine").setVisible(true);

            // Cerrar la ventana actual
            dispose();
        });

        // Etiqueta "Select your plants"
        JLabel selectPlantsLabel = new JLabel("Plants");
        selectPlantsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectPlantsLabel.setForeground(Color.WHITE);
        selectPlantsLabel.setBounds(165, 295, 150, 30);
        panel.add(selectPlantsLabel);

        // Panel para las plantas
        JPanel plantsPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 filas, 2 columnas, 10px de separación
        plantsPanel.setBounds(130, 340, 120, 120); // Posición y tamaño ajustados
        plantsPanel.setOpaque(false); // Transparente para ver el fondo

        String[] plantImages = {
            "resources/images/plants/Sunflower/Sunflower.jpg",
            "resources/images/plants/Peashooter/Peashooter.jpg",
            "resources/images/plants/WallNut/Wall-nutGrass.jpg"
        };

        // Crear y agregar los paneles de plantas preseleccionados
        for (String imagePath : plantImages) {
            PlantPanel plantPanel = new PlantPanel(imagePath, true); // Preseleccionado
            plantPanelsList.add(plantPanel);
            plantsPanel.add(plantPanel); // Agregar al contenedor de plantas
        }
        panel.add(plantsPanel);

        // Etiqueta "Select your zombies"
        JLabel selectZombiesLabel = new JLabel("Zombies");
        selectZombiesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectZombiesLabel.setForeground(Color.WHITE);
        selectZombiesLabel.setBounds(665, 290, 170, 30);
        panel.add(selectZombiesLabel);

        JLabel gameModeLabel = new JLabel("<html>" +

                "In this mode, both sides will be controlled <br>by machines.The machines configure intelligent <br>movements to respond to the opponent <br>with strategy.");

        gameModeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        gameModeLabel.setForeground(Color.WHITE);
        gameModeLabel.setBounds(320, 270, 380, 100); // Altura ajustada
        panel.add(gameModeLabel);

        // Panel para los zombies
        JPanel zombiesPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 filas, 2 columnas, 10px de separación
        zombiesPanel.setBounds(640, 340, 120, 120); // Posición y tamaño ajustados
        zombiesPanel.setOpaque(false); // Transparente para ver el fondo

        String[] zombieImages = {
            "resources/images/zombies/Basic/Basic.jpg",
            "resources/images/zombies/Conehead/Conehead.jpg",
            "resources/images/zombies/BucketHead/Buckethead.jpg"
        };

        // Crear y agregar los paneles de zombies preseleccionados
        for (String imagePath : zombieImages) {
            ZombiePanel zombiePanel = new ZombiePanel(imagePath, true); // Preseleccionado
            zombiePanelsList.add(zombiePanel);
            zombiesPanel.add(zombiePanel);
        }
        panel.add(zombiesPanel);

        // Campo de texto para la cantidad de suns
        setSunsField = new JTextField("Amount of suns");
        setSunsField.setFont(new Font("Arial", Font.BOLD, 12));
        setSunsField.setBounds(155, 487, 90, 20); // Mismas dimensiones que el botón
        setSunsField.setBackground(new Color(253, 210, 1)); // Color de fondo personalizado
        setSunsField.setForeground(Color.WHITE); // Asegurar que el texto sea legible
        setSunsField.setBorder(null); // Eliminar borde
        panel.add(setSunsField);
        // Añadir un FocusListener para el campo de suns
        setSunsField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (setSunsField.getText().equals("Amount of suns")) {
                    setSunsField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (setSunsField.getText().isEmpty()) {
                    setSunsField.setText("Amount of suns");
                }
                checkFields();
            }
        });

        // Campo de texto para la cantidad de brains
        setBrainsField = new JTextField("Amount of brains");
        setBrainsField.setFont(new Font("Arial", Font.BOLD, 12)); // Texto en negrita
        setBrainsField.setBounds(660, 485, 100, 20); // Mismas dimensiones que el botón
        setBrainsField.setBackground(new Color(240, 162, 198)); // Color de fondo personalizado
        setBrainsField.setForeground(Color.WHITE); // Texto blanco
        setBrainsField.setBorder(null); // Eliminar borde
        panel.add(setBrainsField);
        // Añadir un FocusListener para el campo de brains
        setBrainsField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (setBrainsField.getText().equals("Amount of brains")) {
                    setBrainsField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (setBrainsField.getText().isEmpty()) {
                    setBrainsField.setText("Amount of brains");
                }
                checkFields();
            }
        });

        // Inicialmente deshabilitar el botón start
        startButton.setEnabled(false);
    }

    // Método para habilitar el botón START si todos los campos tienen valores
    private void checkFields() {
        boolean timeFilled = !time.getText().isEmpty() && !time.getText().equals("Time");
        boolean quantityFilled = !quantity.getText().isEmpty() && !quantity.getText().equals("Number");
        boolean sunsFilled = !setSunsField.getText().isEmpty() && !setSunsField.getText().equals("Amount of suns");
        boolean brainsFilled = !setBrainsField.getText().isEmpty() && !setBrainsField.getText().equals("Amount of brains");

        // En este modo, las plantas y zombies están preseleccionados
        boolean atLeastOnePlantSelected = plantPanelsList.size() >= 1;
        boolean atLeastOneZombieSelected = zombiePanelsList.size() >= 1;

        startButton.setEnabled(timeFilled && quantityFilled && sunsFilled && brainsFilled &&
                               atLeastOnePlantSelected && atLeastOneZombieSelected);
    }

    private void addTopRightButtons(JPanel panel) {
        String buttonImagePath = "resources/images/buttons/return-icon.png"; // Botón Return

        int x = 830;
        int y = 5;
        int buttonSize = 40;

        ImageIcon icon = new ImageIcon(buttonImagePath);
        JButton button = new JButton(new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        button.setBounds(x, y, buttonSize, buttonSize);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);


        if (buttonImagePath.contains("return-icon")) {
            button.addActionListener(e -> {
                // Volver al menú principal
                dispose(); // Cerrar la ventana actual
                POOBvsZombiesGUI POOBvsZombiesGUI = new POOBvsZombiesGUI(); // Abrir el menú principal
                POOBvsZombiesGUI.setVisible(true);
            });
        }

        panel.add(button);
    }

    // Panel personalizado para pintar la imagen de fondo
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Cargar la imagen desde la ruta especificada
            backgroundImage = new ImageIcon("resources/images/menu/MachineVSMachineMenu.png").getImage();
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

    // PlantPanel que representa cada panel de planta preseleccionada
    class PlantPanel extends JPanel {
        private boolean selected = true; // Siempre seleccionado
        private Image plantImage;
        private String plantPath;

        public PlantPanel(String imagePath, boolean isPreselected) {
            this.plantPath = imagePath;
            plantImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);

            if (isPreselected) {
                // No añadir MouseListener para evitar cambios
                // Las plantas están preseleccionadas con recuadro verde
            }
        }

        public boolean isSelected() {
            return selected;
        }

        public String getPlantPath() {
            return plantPath;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibujar la imagen de la planta
            if (plantImage != null) {
                g.drawImage(plantImage, 0, 0, getWidth(), getHeight(), this);
            }
            // Dibujar el recuadro verde transparente
            g.setColor(new Color(0, 255, 0, 128)); // Verde con transparencia
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ZombiePanel que representa cada panel de zombie preseleccionado
    class ZombiePanel extends JPanel {
        private boolean selected = true; // Siempre seleccionado
        private Image zombieImage;
        private String zombiePath;

        public ZombiePanel(String imagePath, boolean isPreselected) {
            this.zombiePath = imagePath;
            zombieImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);

            if (isPreselected) {
                // No añadir MouseListener para evitar cambios
                // Los zombies están preseleccionados con recuadro verde
            }
        }

        public boolean isSelected() {
            return selected;
        }

        public String getZombiePath() {
            return zombiePath;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibujar la imagen del zombie
            if (zombieImage != null) {
                g.drawImage(zombieImage, 0, 0, getWidth(), getHeight(), this);
            }
            // Dibujar el recuadro verde transparente
            g.setColor(new Color(0, 255, 0, 128)); // Verde con transparencia
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        // Crear y mostrar la ventana
        SwingUtilities.invokeLater(() -> {
            MachineVSMachine frame = new MachineVSMachine();
            frame.setVisible(true);
        });
    }
}