package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domain.POOBvsZombies;

public class PlayerVSMachine extends JFrame {
    private JButton playerNameButton, timeButton, numberButton;
    private JButton startButton;
    private JPanel[] plantPanels; // Array to store plant panels
    private POOBvsZombies poobvsZombies;

    public PlayerVSMachine() {
        // JFrame configuration
        setTitle("Player vs Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel with custom background
        JPanel panel = new JPanel() {
            Image backgroundImage = new ImageIcon("resources/images/menu/PlayervsMachineMenu.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Create a JLabel for multi-line text
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

        // Create text "SELECT PLANTS"
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

        // START button
        startButton = new JButton("¡START!");
        startButton.setBounds(485, 355, 160, 27);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.BLACK);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setEnabled(false);
        startButton.addActionListener(e -> {
            // Create a list for the selected plants
            List<String> selectedPlants = new ArrayList<>();

            // Iterate over the plant selection panels to identify the selected ones
            for (int i = 0; i < plantPanels.length; i++) {
                PlantPanel plantPanel = (PlantPanel) plantPanels[i];
                if (plantPanel.isSelected()) {
                    selectedPlants.add(plantPanel.getPlantPath());
                }
            }

            // Open the garden menu with the selected plants
            //new GardenMenu(selectedPlants.toArray(new String[0]), null, "PlayerVsMachine").setVisible(true);

            // Close the current menu
            dispose();
        }); 

        // FlowLayout for plants
        JPanel plantSelectionPanel = new JPanel();
        plantSelectionPanel.setBounds(115, 590, 600, 150);
        plantSelectionPanel.setLayout(new FlowLayout());
        plantSelectionPanel.setOpaque(false);

        // Specific path for each plant button
        String[] plantImages = {
                "resources/images/plants/Sunflower/Sunflower.jpg",
                "resources/images/plants/Peashooter/Peashooter.jpg",
                "resources/images/plants/WallNut/Wall-nutGrass.jpg",
                "resources/images/plants/PotatoMine/Potato_MineGrass.jpg",
                "resources/images/plants/ECIPlant/ECIPlant.png"
        };

        // Initialize the array of plant panels
        plantPanels = new PlantPanel[5];

        // Assign specific images to each button
        for (int i = 0; i < plantImages.length; i++) {
            // Create custom plant panel
            PlantPanel plantPanel = new PlantPanel(plantImages[i]);
            plantPanels[i] = plantPanel; // Save in the array for later use
            plantSelectionPanel.add(plantPanel); // Add to the main container
        }

        // Add components to the main panel
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
        

        add(panel); // Add the panel to the JFrame
        addTopRightButtons(panel);
    }

    // Method to create an input button
    private JButton createPlayerNameButton(int x, int y) {
        JButton button = new JButton();
        button.setBounds(x, y, 150, 30);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(229, 206, 172));
        button.setForeground(Color.BLACK);
        button.setEnabled(true); // Initially enabled
        button.addActionListener(e -> handleInput(button));
        return button;
    }

    // Method to create an input button
    private JButton createTimeNumberButton(int x, int y) {
        JButton button = new JButton();
        button.setBounds(x, y, 60, 14);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(239, 162, 198));
        button.setForeground(Color.BLACK);
        button.setEnabled(true); // Initially enabled
        button.addActionListener(e -> handleInput(button));
        return button;
    }

    // Handle input in buttons
    private void handleInput(JButton button) {
        String label = button == playerNameButton ? "Player Name" :
                button == timeButton ? "Time" : "Number";

        String input = JOptionPane.showInputDialog(this, "Enter " + label + ":");
        if (input != null && !input.trim().isEmpty()) {
            button.setText(input);
            button.setEnabled(false); // Disable the button after input
            checkFields(); // Validate if actions can be enabled
        }
    }

    // Method to enable the START button if all fields have values and at least one plant is selected
    private void checkFields() {
        boolean allFieldsFilled = !playerNameButton.getText().isEmpty() &&
                !timeButton.getText().isEmpty() &&
                !numberButton.getText().isEmpty();

        boolean atLeastOnePlantSelected = false;
        for (int i = 0; i < plantPanels.length; i++) {
            PlantPanel plantPanel = (PlantPanel) plantPanels[i];
            if (plantPanel.isSelected()) {
                atLeastOnePlantSelected = true;
                break;
            }
        }

        startButton.setEnabled(allFieldsFilled && atLeastOnePlantSelected);
    }


    private void addTopRightButtons(JPanel panel) {
        String buttonImagePath = "resources/images/buttons/return-icon.png";     // Return;

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
            // Back to main menu
            dispose(); // Close the current window
            POOBvsZombiesGUI POOBvsZombiesGUI = new POOBvsZombiesGUI(); // Open the main menu
            POOBvsZombiesGUI.setVisible(true);
        });
    }

        panel.add(button);
    }


    // PlantPanel class that represents each plant panel and handles its selection
    class PlantPanel extends JPanel {
        private boolean selected = false;
        private Image plantImage;
        private String plantPath;

        public PlantPanel(String imagePath) {
            this.plantPath = imagePath;
            plantImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);

            // Change selection state on click
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selected = !selected; // Toggle state
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerVSMachine frame = new PlayerVSMachine();
            frame.setVisible(true);
        });
    }
}