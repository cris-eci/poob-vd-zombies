package presentation;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MachineVSMachine extends JFrame {

    // Domain Layer
    private POOBvsZombies poobvsZombies;

    // GUI Components
    private JTextField timeField, quantityField;
    private JLabel setSunsLabel, setBrainsLabel;
    private JLabel sunsValueLabel, brainsValueLabel;
    private JButton startButton;
    private List<PlantPanel> plantPanelsList;
    private List<ZombiePanel> zombiePanelsList;

    public MachineVSMachine() {
        prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        // Configuración de la ventana
        setTitle("Machine vs Machine");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel de fondo personalizado
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // Posicionamiento absoluto
        setContentPane(backgroundPanel);

        // Inicializar listas de paneles
        plantPanelsList = new ArrayList<>();
        zombiePanelsList = new ArrayList<>();

        // Campos de texto para tiempo y cantidad de hordas
        timeField = createTextField("Time", 385, 415, 40, 15);
        quantityField = createTextField("Number", 455, 415, 55, 15);

        
        // Etiquetas para "Amount of suns" y "Amount of brains"
        //setSunsLabel = new JLabel("Amount of suns:");
        //setSunsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        //setSunsLabel.setForeground(Color.WHITE);
        //setSunsLabel.setBounds(65, 487, 100, 20);
        //backgroundPanel.add(setSunsLabel);

        //setBrainsLabel = new JLabel("Amount of brains:");
        //setBrainsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        //setBrainsLabel.setForeground(Color.WHITE);
        //setBrainsLabel.setBounds(520, 485, 130, 20);
        //backgroundPanel.add(setBrainsLabel);

        // Valores fijos para suns y brains
        sunsValueLabel = new JLabel("100");
        sunsValueLabel.setFont(new Font("Arial", Font.BOLD, 12));
        sunsValueLabel.setForeground(Color.WHITE);
        sunsValueLabel.setBounds(175, 487, 50, 20);
        backgroundPanel.add(sunsValueLabel);

        brainsValueLabel = new JLabel("200");
        brainsValueLabel.setFont(new Font("Arial", Font.BOLD, 12));
        brainsValueLabel.setForeground(Color.WHITE);
        brainsValueLabel.setBounds(690, 485, 50, 20);
        backgroundPanel.add(brainsValueLabel);

        // Botón START
        startButton = new JButton("¡START!");
        startButton.setBounds(395, 460, 90, 38);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.WHITE); // Color del texto
        startButton.setEnabled(false); // Inicialmente deshabilitado
		
        backgroundPanel.add(startButton);
        backgroundPanel.add(timeField);
        backgroundPanel.add(quantityField);
        

        // Etiquetas
        addLabels(backgroundPanel);

        // Paneles de plantas y zombies
        addPlantPanels(backgroundPanel);
        addZombiePanels(backgroundPanel);

        // Botones en la esquina superior derecha
        addTopRightButtons(backgroundPanel);
    }

    private void prepareActions() {
        // Acción al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                setVisible(false);
                System.exit(0);
            }
        });

        // Acción del botón de inicio
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                actionStart();
            }
        });
		

		// Añadir FocusListeners a los campos de tiempo y cantidad
        addTextFieldListeners(timeField);
        addTextFieldListeners(quantityField);
    }

    // Método para manejar el inicio del juego
    private void actionStart() {
        try {
            // Obtener valores ingresados
            int matchTime = Integer.parseInt(timeField.getText());
            int hordeNumber = Integer.parseInt(quantityField.getText());

            // Plantas y Zombies seleccionados (preseleccionados)
            ArrayList<String> selectedPlants = getSelectedPlantNames();
            ArrayList<String> selectedZombies = getSelectedZombieNames();

            // Crear instancia de POOBvsZombies para MachineVsMachine
            poobvsZombies = new POOBvsZombies(
                    matchTime,
                    hordeNumber
            );

            // Agregar plantas y zombies seleccionados al POOBvsZombies
            poobvsZombies.getPlayerOne().getTeam().setCharacters(selectedPlants);
            poobvsZombies.getPlayerTwo().getTeam().setCharacters(selectedZombies);

            // Setear suns y brains fijos
            poobvsZombies.getPlayerOne().getTeam().setResourceCounter(100); // Suns
            poobvsZombies.getPlayerTwo().getTeam().setResourceCounter(200); // Brains

            // Abrir el GardenMenu con la configuración
            new GardenMenu(poobvsZombies).setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric value. Please check the inputs.", "Error",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos auxiliares
    private JTextField createTextField(String placeholder, int x, int y, int width, int height) {
        JTextField textField = new JTextField(placeholder);
        textField.setFont(new Font("Arial", Font.BOLD, 13));
        textField.setBounds(x, y, width, height);
        textField.setBorder(null);
        textField.setBackground(new Color(228, 206, 171));
        textField.setForeground(new Color(134, 119, 94));
        return textField;
    }

    private void addTextFieldListeners(JTextField textField) {
        String placeholder = textField.getText();
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                }
                checkFields();
            }
        });
    }

    private void addLabels(JPanel panel) {
        // Etiqueta de información
        JLabel infoLabel = new JLabel("<html>"
                + "In this mode, both sides will be controlled <br>by machines.The machines configure intelligent <br>movements to respond to the opponent <br>with strategy.");
        infoLabel.setBounds(320, 270, 380, 100); // Posición y tamaño ajustados
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(infoLabel);

        // Etiqueta "SELECT PLANTS"
        JLabel selectPlantLabel = new JLabel("PLANTS");
        selectPlantLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectPlantLabel.setForeground(Color.WHITE);
        selectPlantLabel.setBounds(165, 295, 150, 30);
        panel.add(selectPlantLabel);

        // Etiqueta "SELECT ZOMBIES"
        JLabel selectZombieLabel = new JLabel("ZOMBIES");
        selectZombieLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectZombieLabel.setForeground(Color.WHITE);
        selectZombieLabel.setBounds(665, 290, 170, 30);
        panel.add(selectZombieLabel);
    }

    private void addPlantPanels(JPanel panel) {
        JPanel plantSelectionPanel = new JPanel();
        plantSelectionPanel.setBounds(130, 340, 120, 120); // Posición y tamaño ajustados
        plantSelectionPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 3 filas, 1 columna, 10px de separación
        plantSelectionPanel.setOpaque(false); // Transparente para ver el fondo

        String[] plantImages = {
            "resources/images/plants/Sunflower/Sunflower.jpg",
            "resources/images/plants/Peashooter/Peashooter.jpg",
            "resources/images/plants/WallNut/Wall-nutGrass.jpg"
        };

        String[] plantNames = {
            "Sunflower",
            "Peashooter",
            "WallNut"
        };

        for (int i = 0; i < plantImages.length; i++) {
            PlantPanel plantPanel = new PlantPanel(plantImages[i], plantNames[i]);
            plantPanelsList.add(plantPanel);
            plantSelectionPanel.add(plantPanel);
        }

        panel.add(plantSelectionPanel);
    }

    private void addZombiePanels(JPanel panel) {
        JPanel zombieSelectionPanel = new JPanel();
        zombieSelectionPanel.setBounds(640, 340, 120, 120); // Posición y tamaño ajustados
        zombieSelectionPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 3 filas, 1 columna, 10px de separación
        zombieSelectionPanel.setOpaque(false); // Transparente para ver el fondo

        String[] zombieImages = {
            "resources/images/zombies/Basic/Basic.jpg",
            "resources/images/zombies/Conehead/Conehead.jpg",
            "resources/images/zombies/BucketHead/Buckethead.jpg"
        };

        String[] zombieNames = {
            "Basic",
            "Conehead",
            "BucketHead"
        };

        for (int i = 0; i < zombieImages.length; i++) {
            ZombiePanel zombiePanel = new ZombiePanel(zombieImages[i], zombieNames[i]);
            zombiePanelsList.add(zombiePanel);
            zombieSelectionPanel.add(zombiePanel);
        }

        panel.add(zombieSelectionPanel);
    }

    private void addTopRightButtons(JPanel panel) {
        String buttonImagePath = "resources/images/buttons/return-icon.png"; // Botón Return

        int x = 830;
        int y = 5;
        int buttonSize = 40;

        ImageIcon icon = new ImageIcon(buttonImagePath);
        JButton button = new JButton(
                new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        button.setBounds(x, y, buttonSize, buttonSize);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Volver al menú principal
                dispose(); // Cerrar la ventana actual
                new POOBvsZombiesGUI().setVisible(true);
            }
        });

        panel.add(button);
    }

    private void checkFields() {
        boolean timeFilled = !timeField.getText().isEmpty() && !timeField.getText().equals("Time");
        boolean quantityFilled = !quantityField.getText().isEmpty() && !quantityField.getText().equals("Number");

        // Dado que las plantas y zombies están preseleccionados y no se pueden modificar,
        // simplemente verificamos que existan.
        boolean atLeastOnePlantSelected = plantPanelsList.size() >= 1;
        boolean atLeastOneZombieSelected = zombiePanelsList.size() >= 1;

        startButton.setEnabled(timeFilled && quantityFilled &&
                               atLeastOnePlantSelected && atLeastOneZombieSelected);
    }

    private ArrayList<String> getSelectedPlantNames() {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (PlantPanel panel : plantPanelsList) {
            if (panel.isSelected()) {
                selectedItems.add(panel.getPlantName());
            }
        }
        return selectedItems;
    }

    private ArrayList<String> getSelectedZombieNames() {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (ZombiePanel panel : zombiePanelsList) {
            if (panel.isSelected()) {
                selectedItems.add(panel.getZombieName());
            }
        }
        return selectedItems;
    }

    // Clases personalizadas
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
        private boolean selected;
        private Image plantImage;
        private String plantPath;
        private String plantName;

        public PlantPanel(String imagePath, String plantName) {
            this.plantPath = imagePath;
            this.plantName = plantName;
            this.selected = true; // Siempre seleccionado
            plantImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);
        }

        public boolean isSelected() {
            return selected;
        }

        public String getPlantName() {
            return plantName;
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
        private boolean selected;
        private Image zombieImage;
        private String zombiePath;
        private String zombieName;

        public ZombiePanel(String imagePath, String zombieName) {
            this.zombiePath = imagePath;
            this.zombieName = zombieName;
            this.selected = true; // Siempre seleccionado
            zombieImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);
        }

        public boolean isSelected() {
            return selected;
        }

        public String getZombieName() {
            return zombieName;
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
