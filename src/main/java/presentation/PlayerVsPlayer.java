package presentation;

import domain.POOBvsZombies;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerVsPlayer extends JFrame {

    // Domain Layer
    private POOBvsZombies poobvsZombies;

    // GUI Components
    private JTextField playerOneName, playerTwoName, matchTime, setSunsField, setBrainsField;
    private JButton startButton;
    private List<PlantPanel> plantPanelsList;
    private List<ZombiePanel> zombiePanelsList;

    public PlayerVsPlayer() {
        prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        // Window Configuration
        setTitle("Player vs Player");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Initialize Components
        plantPanelsList = new ArrayList<>();
        zombiePanelsList = new ArrayList<>();

        // Text Fields
        playerOneName = createTextField("Name player one", 270, 380, 150, 30);
        playerTwoName = createTextField("Name player two", 465, 380, 150, 30);
        matchTime = createTextField("Time", 350, 455, 40, 20);
        setSunsField = createTextField("Amount of suns", 82, 613, 90, 20);
        setBrainsField = createTextField("Amount of brains", 730, 613, 100, 20);

        // Buttons
        startButton = new JButton("¡START!");
        startButton.setBounds(462, 449, 160, 30);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.WHITE);
        startButton.setEnabled(false);

        // Add Components to Panel
        backgroundPanel.add(playerOneName);
        backgroundPanel.add(playerTwoName);
        backgroundPanel.add(matchTime);
        backgroundPanel.add(setSunsField);
        backgroundPanel.add(setBrainsField);
        backgroundPanel.add(startButton);
        

        // Labels
        addLabels(backgroundPanel);

        // Plant and Zombie Panels
        addPlantPanels(backgroundPanel);
        addZombiePanels(backgroundPanel);

        // Top Right Buttons
        addTopRightButtons(backgroundPanel);
    }

    private void prepareActions() {
        // Window Closing Action
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                setVisible(false);
                System.exit(0);
            }
        });

        // Start Button Action
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                actionStart();
            }
        });


        // Text Fields Document Listeners
        addTextFieldListeners(playerOneName);
        addTextFieldListeners(playerTwoName);
        addTextFieldListeners(matchTime);
        addTextFieldListeners(setSunsField);
        addTextFieldListeners(setBrainsField);
    }

    // Action Methods
    private void actionStart() {
        try {
            // Collect Values
            String namePlayerOne = playerOneName.getText();
            String namePlayerTwo = playerTwoName.getText();
            int matchTimer = Integer.parseInt(matchTime.getText());
            int sunAmount = Integer.parseInt(setSunsField.getText());
            int brainAmount = Integer.parseInt(setBrainsField.getText());

            // Selected Plants and Zombies
            ArrayList<String> selectedPlants = getSelectedItems(plantPanelsList);
            ArrayList<String> selectedZombies = getSelectedItems(zombiePanelsList);

            // Create POOBvsZombies Instance
            poobvsZombies = new POOBvsZombies(
                    matchTimer,
                    namePlayerOne,
                    selectedPlants,
                    sunAmount,
                    namePlayerTwo,
                    brainAmount,
                    selectedZombies
            );

            // Proceed to the next step (e.g., open GardenMenu)
            // For example:
            // new GardenMenu(selectedPlants, selectedZombies, "PlayerVsPlayer").setVisible(true);
            // dispose();

            // Display Success Message
            JOptionPane.showMessageDialog(this, "Game initialized successfully!"  + " " + namePlayerOne + " " + namePlayerTwo + " " + matchTimer + " " + sunAmount + " " + brainAmount + " " + selectedPlants + " " + selectedZombies, "Success", JOptionPane.INFORMATION_MESSAGE);
            

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric value. Please check the inputs.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Helper Methods
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
        // Label "Select your plants"
        JLabel selectPlantsLabel = new JLabel("Select your plants");
        selectPlantsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectPlantsLabel.setForeground(Color.WHITE);
        selectPlantsLabel.setBounds(46, 405, 150, 30);
        panel.add(selectPlantsLabel);

        // Label "Select your zombies"
        JLabel selectZombiesLabel = new JLabel("Select your zombies");
        selectZombiesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectZombiesLabel.setForeground(Color.WHITE);
        selectZombiesLabel.setBounds(690, 405, 170, 30);
        panel.add(selectZombiesLabel);

        // Game Mode Description
        JLabel gameModeLabel = new JLabel("<html><div style='width:385px;'>"
                + "In this mode, players control plants and zombies, defining strategies."
                + " <br> The plant team has 2 minutes to set up and must withstand zombie "
                + "<br>waves configured by the zombie team. Each player decides their "
                + "<br>team's starting resources."
                + "</div></html>");
        gameModeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gameModeLabel.setForeground(Color.WHITE);
        gameModeLabel.setBounds(245, 520, 380, 100);
        panel.add(gameModeLabel);
    }

    private void addPlantPanels(JPanel panel) {
        JPanel plantsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        plantsPanel.setBounds(65, 440, 100, 160);
        plantsPanel.setOpaque(false);

        String[] plantImages = {
                "resources/images/plants/Sunflower/Sunflower.jpg",
                "resources/images/plants/Peashooter/Peashooter.jpg",
                "resources/images/plants/WallNut/Wall-nutGrass.jpg",
                "resources/images/plants/PotatoMine/Potato_MineGrass.jpg",
                "resources/images/plants/ECIPlant/ECIPlant.png"
        };

        for (String imagePath : plantImages) {
            PlantPanel plantPanel = new PlantPanel(imagePath);
            plantPanelsList.add(plantPanel);
            plantsPanel.add(plantPanel);
        }
        panel.add(plantsPanel);
    }

    private void addZombiePanels(JPanel panel) {
        JPanel zombiesPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        zombiesPanel.setBounds(710, 440, 100, 160);
        zombiesPanel.setOpaque(false);

        String[] zombieImages = {
                "resources/images/zombies/Basic/Basic.jpg",
                "resources/images/zombies/Conehead/Conehead.jpg",
                "resources/images/zombies/BucketHead/Buckethead.jpg",
                "resources/images/zombies/ECIZombie/ECIZombie.png",
                "resources/images/zombies/Brainstein/brainsteinGarden.jpeg"
        };

        for (String imagePath : zombieImages) {
            ZombiePanel zombiePanel = new ZombiePanel(imagePath);
            zombiePanelsList.add(zombiePanel);
            zombiesPanel.add(zombiePanel);
        }
        panel.add(zombiesPanel);
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

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Back to main menu
                dispose(); // Close the current window
                // Open the main menu (uncomment if POOBvsZombiesGUI exists)
                // new POOBvsZombiesGUI().setVisible(true);
            }
        });

        panel.add(button);
    }

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

    private ArrayList<String> getSelectedItems(List<? extends SelectablePanel> panels) {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (SelectablePanel panel : panels) {
            if (panel.isSelected()) {
                selectedItems.add(panel.getItemPath());
            }
        }
        return selectedItems;
    }

    // Custom Classes
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon("resources/images/menu/PlayerVsPlayerMenu.png").getImage();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    abstract class SelectablePanel extends JPanel {
        private boolean selected = false;
        private Image itemImage;
        private String itemPath;

        public SelectablePanel(String imagePath) {
            this.itemPath = imagePath;
            this.itemImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
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

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        public String getItemPath() {
            return itemPath;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (itemImage != null) {
                g.drawImage(itemImage, 0, 0, getWidth(), getHeight(), this);
            }
            if (selected) {
                g.setColor(new Color(0, 255, 0, 128));
                g.fillRect(0, 0, getWidth(), getHeight());
            } else {
                g.setColor(new Color(255, 0, 0, 128));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    class PlantPanel extends SelectablePanel {
        public PlantPanel(String imagePath) {
            super(imagePath);
        }
    }

    class ZombiePanel extends SelectablePanel {
        public ZombiePanel(String imagePath) {
            super(imagePath);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerVsPlayer frame = new PlayerVsPlayer();
            frame.setVisible(true);
        });
    }
}
