package presentation;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import domain.*;

public class PlayerVSMachine extends JFrame {
    private JButton playerNameButton, timeButton, numberButton;
    private JButton startButton;
    private PlantPanel[] plantPanels; // Array para almacenar los paneles de plantas
    private String playerName;
    private int matchTime;
    private int numberOfHordes;

    public PlayerVSMachine() {
        // Configuración del JFrame
        setTitle("Player vs Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel con fondo personalizado
        JPanel panel = new JPanel() {
            Image backgroundImage = new ImageIcon("resources/images/menu/PlayervsMachineMenu.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Crear y agregar componentes
        addComponents(panel);
        addTopRightButtons(panel);

        add(panel); // Añadir el panel al JFrame
    }

    private void addComponents(JPanel panel) {
        // Etiqueta de información
        JLabel infoLabel = new JLabel("<html>"
                + "Machine will adopt its original<br>"
                + "behavior from PvsZ original game.<br>"
                + "You can configure your name, hordes <br>"
                + "duration and number. You can also <br>"
                + "select your plants to play. Have fun!"
                + "</html>");
        infoLabel.setBounds(485, 215, 500, 100);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        // Texto "SELECT PLANTS"
        JLabel selectPlant = new JLabel("<html>SELECT PLANTS</html>");
        selectPlant.setBounds(385, 525, 500, 100);
        selectPlant.setForeground(Color.WHITE);
        selectPlant.setFont(new Font("Arial", Font.PLAIN, 9));

        // PlayerName
        JLabel playerNameLabel = new JLabel("PlayerName:");
        playerNameLabel.setBounds(260, 205, 150, 30);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        playerNameButton = createPlayerNameButton(225, 235);

        // Time
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setBounds(245, 345, 150, 30);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        timeButton = createTimeNumberButton(230, 370);

        // Number
        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setBounds(320, 345, 150, 30);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 10));
        numberButton = createTimeNumberButton(305, 370);

        // Botón START
        startButton = new JButton("¡START!");
        startButton.setBounds(485, 355, 160, 27);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.BLACK);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setEnabled(false);
        startButton.addActionListener(e -> startGame());

        // Panel para la selección de plantas
        JPanel plantSelectionPanel = new JPanel();
        plantSelectionPanel.setBounds(115, 590, 600, 150);
        plantSelectionPanel.setLayout(new FlowLayout());
        plantSelectionPanel.setOpaque(false);

        // Imágenes de plantas
        String[] plantImages = {
                "resources/images/plants/Sunflower/Sunflower.jpg",
                "resources/images/plants/Peashooter/Peashooter.jpg",
                "resources/images/plants/WallNut/Wall-nutGrass.jpg",
                "resources/images/plants/PotatoMine/Potato_MineGrass.jpg",
                "resources/images/plants/ECIPlant/ECIPlant.png"
        };

        // Inicializar array de paneles de plantas
        plantPanels = new PlantPanel[5];

        // Crear paneles de plantas
        for (int i = 0; i < plantImages.length; i++) {
            PlantPanel plantPanel = new PlantPanel(plantImages[i]);
            plantPanels[i] = plantPanel;
            plantSelectionPanel.add(plantPanel);
        }

        // Añadir componentes al panel
        panel.add(infoLabel);
        panel.add(selectPlant);
        panel.add(playerNameLabel);
        panel.add(playerNameButton);
        panel.add(timeLabel);
        panel.add(timeButton);
        panel.add(numberLabel);
        panel.add(numberButton);
        panel.add(startButton);
        panel.add(plantSelectionPanel);
    }

    private JButton createPlayerNameButton(int x, int y) {
        JButton button = new JButton();
        button.setBounds(x, y, 150, 30);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(229, 206, 172));
        button.setForeground(Color.BLACK);
        button.setEnabled(true);
        button.addActionListener(e -> handleInput(button));
        return button;
    }

    private JButton createTimeNumberButton(int x, int y) {
        JButton button = new JButton();
        button.setBounds(x, y, 60, 14);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(239, 162, 198));
        button.setForeground(Color.BLACK);
        button.setEnabled(true);
        button.addActionListener(e -> handleInput(button));
        return button;
    }

    private void handleInput(JButton button) {
        String label = button == playerNameButton ? "Player Name" :
                button == timeButton ? "Time" : "Number";

        String input = JOptionPane.showInputDialog(this, "Enter " + label + ":");
        if (input != null && !input.trim().isEmpty()) {
            button.setText(input);
            button.setEnabled(false);
            if (button == playerNameButton) {
                playerName = input.trim();
            } else if (button == timeButton) {
                matchTime = Integer.parseInt(input.trim());
            } else if (button == numberButton) {
                numberOfHordes = Integer.parseInt(input.trim());
            }
            checkFields();
        }
    }

    private void checkFields() {
        boolean allFieldsFilled = playerName != null && matchTime > 0 && numberOfHordes > 0;

        boolean atLeastOnePlantSelected = false;
        for (PlantPanel plantPanel : plantPanels) {
            if (plantPanel.isSelected()) {
                atLeastOnePlantSelected = true;
                break;
            }
        }

        startButton.setEnabled(allFieldsFilled && atLeastOnePlantSelected);
    }

    private void startGame() {
        // Obtener las plantas seleccionadas
        ArrayList<String> selectedPlants = new ArrayList<>();
        for (PlantPanel plantPanel : plantPanels) {
            if (plantPanel.isSelected()) {
                selectedPlants.add(plantPanel.getPlantPath());
            }
        }

        // Crear los equipos y jugadores
        Plants plantsTeam = new Plants(100); // Soles iniciales
        PlantsStrategic humanPlayer = new PlantsStrategic(plantsTeam,playerName);

        Zombies zombiesTeam = new Zombies(100); // Cerebros iniciales
        ZombieOriginal machinePlayer = new ZombieOriginal(zombiesTeam, "Machine", numberOfHordes, matchTime);

        ArrayList<Player> players = new ArrayList<>();
        players.add(humanPlayer);
        players.add(machinePlayer);

        // Crear el juego
        POOBvsZombies game = new POOBvsZombies(players, matchTime);

        // Abrir el GardenMenu
        GardenMenu gardenMenu = new GardenMenu(
                selectedPlants.toArray(new String[0]),
                null,
                "PlayerVsMachine",
                game,
                players
        );
        gardenMenu.setVisible(true);

        // Iniciar el juego
        game.startGame();

        // Cerrar la ventana actual
        dispose();
    }

    private void addTopRightButtons(JPanel panel) {
        String buttonImagePath = "resources/images/buttons/return-icon.png";

        int x = 830;
        int y = 5;
        int buttonSize = 40;

        ImageIcon icon = new ImageIcon(buttonImagePath);
        JButton button = new JButton(new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        button.setBounds(x, y, buttonSize, buttonSize);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            dispose();
            POOBvsZombiesGUI mainMenu = new POOBvsZombiesGUI();
            mainMenu.setVisible(true);
        });

        panel.add(button);
    }

    // Clase interna PlantPanel
    class PlantPanel extends JPanel {
        private boolean selected = false;
        private Image plantImage;
        private String plantPath;

        public PlantPanel(String imagePath) {
            this.plantPath = imagePath;
            plantImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selected = !selected;
                    repaint();
                    checkFields();
                }
            });
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
            g.drawImage(plantImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(selected ? new Color(0, 255, 0, 128) : new Color(255, 0, 0, 128));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
