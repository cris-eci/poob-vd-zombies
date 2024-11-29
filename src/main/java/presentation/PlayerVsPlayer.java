package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.util.*;

public class PlayerVsPlayer extends JFrame {
    private List<PlantPanel> plantPanelsList = new ArrayList<>();
    private List<ZombiePanel> zombiePanelsList = new ArrayList<>();
    private JTextField playerOneName, playerTwoName, matchTime, setSunsField, setBrainsField;
    private JButton startButton;

    public PlayerVsPlayer() {
        // Basic window configuration
        setTitle("Player vs Player");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        // Set the background with the image
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // To position components absolutely
        setContentPane(backgroundPanel);

        // Create and add the necessary labels and text fields
        addComponents(backgroundPanel);
        addTopRightButtons(backgroundPanel);
    }

    private void addComponents(JPanel panel) {

        // Text Field for Player One Name
        playerOneName = new JTextField("Name player one");
        playerOneName.setFont(new Font("Arial", Font.BOLD, 13)); // Bold text, size 13
        playerOneName.setBounds(270, 380, 150, 30);
        playerOneName.setBorder(null); // Remove border
        playerOneName.setBackground(new Color(228, 206, 171));
        playerOneName.setForeground(new Color(134, 119, 94)); // Text color
        panel.add(playerOneName);
        // Add a FocusListener for the Player One Name JTextField
        playerOneName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (playerOneName.getText().equals("Name player one")) {
                    playerOneName.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (playerOneName.getText().isEmpty()) {
                    playerOneName.setText("Name player one");
                }
                checkFields();
            }
        });

        // Text Field for Player Two Name
        playerTwoName = new JTextField("Name player two");
        playerTwoName.setFont(new Font("Arial", Font.BOLD, 13)); // Bold text, size 13
        playerTwoName.setBorder(null); // Remove border
        playerTwoName.setBounds(465, 380, 150, 30);
        playerTwoName.setBackground(new Color(228, 206, 171));
        playerTwoName.setForeground(new Color(134, 119, 94)); // Text color
        panel.add(playerTwoName);

        // Add a FocusListener for the Player Two Name JTextField
        playerTwoName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (playerTwoName.getText().equals("Name player two")) {
                    playerTwoName.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (playerTwoName.getText().isEmpty()) {
                    playerTwoName.setText("Name player two");
                }
                checkFields();
            }
        });

        // Text Field for match time
        matchTime = new JTextField("Time");
        matchTime.setFont(new Font("Arial", Font.BOLD, 11)); // Bold text, size 13
        matchTime.setBounds(350, 455, 40, 20); // Position below playerOneName and slightly to the right
        matchTime.setBorder(null); // Remove border
        matchTime.setBackground(new Color(228, 206, 171));
        matchTime.setForeground(new Color(134, 119, 94)); // Text color
        panel.add(matchTime);
        // Add a FocusListener for the match time JTextField
        matchTime.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (matchTime.getText().equals("Time")) {
                    matchTime.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (matchTime.getText().isEmpty()) {
                    matchTime.setText("Time");
                }
                checkFields();
            }
        });

        // Start button
        startButton = new JButton("¡START!");
        startButton.setBounds(462, 449, 160, 30);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.WHITE); // Set text color to white
        startButton.setEnabled(false); // Initially disabled
        panel.add(startButton);

        // Add action listener to the start button
        startButton.addActionListener(e -> {
            // Gather selected plants and zombies
            List<String> selectedPlants = new ArrayList<>();
            List<String> selectedZombies = new ArrayList<>();

            // Iterate over plant panels
            for (PlantPanel plantPanel : plantPanelsList) {
                if (plantPanel.isSelected()) {
                    selectedPlants.add(plantPanel.getPlantPath());
                }
            }

            // Iterate over zombie panels
            for (ZombiePanel zombiePanel : zombiePanelsList) {
                if (zombiePanel.isSelected()) {
                    selectedZombies.add(zombiePanel.getZombiePath());
                }
            }

            // Now, open the GardenMenu with the selected plants and zombies
            new GardenMenu(selectedPlants.toArray(new String[0]), selectedZombies.toArray(new String[0]), "PlayerVsPlayer").setVisible(true);

            // Close the current window
            dispose();
        });

        // Label "Select your plants" for Player One
        JLabel selectPlantsLabel = new JLabel("Select your plants");
        selectPlantsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectPlantsLabel.setForeground(Color.WHITE);
        selectPlantsLabel.setBounds(46, 405, 150, 30);
        panel.add(selectPlantsLabel);

        // Create and add the JPanels for plants
        JPanel plantsPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, 10px spacing
        plantsPanel.setBounds(65, 440, 100, 160); // Adjust position and size as needed
        plantsPanel.setOpaque(false); // Make the panel transparent to see the background

        String[] plantImages = {
            "resources/images/plants/Sunflower/Sunflower.jpg",
            "resources/images/plants/Peashooter/Peashooter.jpg",
            "resources/images/plants/WallNut/Wall-nutGrass.jpg",
            "resources/images/plants/PotatoMine/Potato_MineGrass.jpg",
            "resources/images/plants/ECIPlant/ECIPlant.png"
        };

        // Create and add the JPanels with plant images
        for (String imagePath : plantImages) {
            PlantPanel plantPanel = new PlantPanel(imagePath);
            plantPanelsList.add(plantPanel);
            plantsPanel.add(plantPanel); // Add the panel to the plants container
        }
        panel.add(plantsPanel);

        // Label "Select your zombies" for Player Two
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
        gameModeLabel.setBounds(245, 520, 380, 100); // Adjust height
        panel.add(gameModeLabel);

        // Create a panel to contain the JPanels of zombies
        JPanel zombiesPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, 10px spacing
        zombiesPanel.setBounds(710, 440, 100, 160); // Adjust position and size as needed
        zombiesPanel.setOpaque(false); // Make the panel transparent to see the background

        String[] zombieImages = {
            "resources/images/zombies/Basic/Basic.jpg",
            "resources/images/zombies/Conehead/Conehead.jpg",
            "resources/images/zombies/BucketHead/Buckethead.jpg",
            "resources/images/zombies/ECIZombie/ECIZombie.png",
            "resources/images/zombies/Brainstein/brainsteinGarden.jpeg"
        };

        // Create and add the JPanels with zombie images
        for (String imagePath : zombieImages) {
            ZombiePanel zombiePanel = new ZombiePanel(imagePath);
            zombiePanelsList.add(zombiePanel);
            zombiesPanel.add(zombiePanel);
        }
        panel.add(zombiesPanel);

        // JTextField for setting initial amount of suns for Player One
        setSunsField = new JTextField("Amount of suns");
        setSunsField.setFont(new Font("Arial", Font.BOLD, 12));
        setSunsField.setBounds(82, 613, 90, 20); // Same dimensions as the button
        setSunsField.setBackground(new Color(253, 210, 1)); // Custom background color
        setSunsField.setForeground(Color.WHITE); // Ensure the text is readable
        setSunsField.setBorder(null); // Remove border
        panel.add(setSunsField);
        // Add a FocusListener for the suns JTextField
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

        // JTextField for setting initial amount of brains for Player Two
        setBrainsField = new JTextField("Amount of brains");
        setBrainsField.setFont(new Font("Arial", Font.BOLD, 12)); // Bold text
        setBrainsField.setBounds(730, 613, 100, 20); // Same dimensions as the button
        setBrainsField.setBackground(new Color(240, 162, 198)); // Custom background color
        setBrainsField.setForeground(Color.WHITE); // White text
        setBrainsField.setBorder(null); // Remove border
        panel.add(setBrainsField);
        // Add a FocusListener for the brains JTextField
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

        // Initially disable the start button
        startButton.setEnabled(false);
    }

    // Method to enable the START button if all fields have values and at least one plant and zombie are selected
    private void checkFields() {
        boolean atLeastOnePlantSelected = false;
        for (PlantPanel plantPanel : plantPanelsList) {
            if (plantPanel.isSelected()) {
                atLeastOnePlantSelected = true;
                break;
            }
        }

        boolean atLeastOneZombieSelected = false;
        for (ZombiePanel zombiePanel : zombiePanelsList) {
            if (zombiePanel.isSelected()) {
                atLeastOneZombieSelected = true;
                break;
            }
        }

        boolean playerOneNameFilled = !playerOneName.getText().isEmpty() && !playerOneName.getText().equals("Name player one");
        boolean playerTwoNameFilled = !playerTwoName.getText().isEmpty() && !playerTwoName.getText().equals("Name player two");
        boolean matchTimeFilled = !matchTime.getText().isEmpty() && !matchTime.getText().equals("Time");
        boolean sunsFilled = !setSunsField.getText().isEmpty() && !setSunsField.getText().equals("Amount of suns");
        boolean brainsFilled = !setBrainsField.getText().isEmpty() && !setBrainsField.getText().equals("Amount of brains");

        startButton.setEnabled(atLeastOnePlantSelected && atLeastOneZombieSelected && playerOneNameFilled && playerTwoNameFilled && matchTimeFilled && sunsFilled && brainsFilled);
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
            MainMenu mainMenu = new MainMenu(); // Open the main menu
            mainMenu.setVisible(true);
        });
    }

        panel.add(button);
    }

    // Custom panel to paint the background image
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Load the image from the specified path
            backgroundImage = new ImageIcon("resources/images/menu/PlayerVsPlayerMenu.png").getImage();
            setLayout(null); // Configure the panel's layout
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the image in the background of the panel
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
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
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
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
            // Draw the background image
            if (plantImage != null) {
                g.drawImage(plantImage, 0, 0, getWidth(), getHeight(), this);
            }
            // Draw the translucent rectangle
            g.setColor(selected ? new Color(0, 255, 0, 128) : new Color(255, 0, 0, 128));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ZombiePanel class that represents each zombie panel and handles its selection
    class ZombiePanel extends JPanel {
        private boolean selected = false;
        private Image zombieImage;
        private String zombiePath;

        public ZombiePanel(String imagePath) {
            this.zombiePath = imagePath;
            zombieImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);

            // Change selection state on click
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selected = !selected; // Toggle state
                    repaint();
                    checkFields();
                }
            });
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
            // Draw the background image
            if (zombieImage != null) {
                g.drawImage(zombieImage, 0, 0, getWidth(), getHeight(), this);
            }
            // Draw the translucent rectangle
            g.setColor(selected ? new Color(0, 255, 0, 128) : new Color(255, 0, 0, 128));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        // Create and display the window
        SwingUtilities.invokeLater(() -> {
            PlayerVsPlayer frame = new PlayerVsPlayer();
            frame.setVisible(true);
        });
    }
}
