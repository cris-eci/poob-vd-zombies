package presentation;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerVSMachine extends JFrame {

    // Domain Layer
    private POOBvsZombies poobvsZombies;

    // GUI Components
    private JTextField playerNameField, matchTimeField, hordeNumberField;
    private JButton startButton;
    private List<PlantPanel> plantPanelsList;

    public PlayerVSMachine() {
        prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        // Window Configuration
        setTitle("Player vs Machine");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Initialize Components
        plantPanelsList = new ArrayList<>();

        // Text Fields
        playerNameField = createTextField("Player Name", 225, 235, 150, 30);
        matchTimeField = createTextField("Time", 230, 370, 60, 14);
        hordeNumberField = createTextField("Number", 305, 370, 60, 14);

        // Buttons
        startButton = new JButton("¡START!");
        startButton.setBounds(485, 355, 160, 27);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.WHITE);
        startButton.setEnabled(false);

        // Add Components to Panel
        backgroundPanel.add(playerNameField);
        backgroundPanel.add(matchTimeField);
        backgroundPanel.add(hordeNumberField);
        backgroundPanel.add(startButton);

        // Labels
        addLabels(backgroundPanel);

        // Plant Panels
        addPlantPanels(backgroundPanel);

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
        addTextFieldListeners(playerNameField);
        addTextFieldListeners(matchTimeField);
        addTextFieldListeners(hordeNumberField);
    }

    // Action Methods
    private void actionStart() {
        try {
            // Collect Values
            String playerName = playerNameField.getText();
            int matchTime = Integer.parseInt(matchTimeField.getText());
            int hordeNumber = Integer.parseInt(hordeNumberField.getText());

            if (matchTime < 1 || hordeNumber < 1) {
                throw new POOBvsZombiesException(POOBvsZombiesException.TIME_LIMIT);
            }
            // Selected Plants
            ArrayList<String> selectedPlants = getSelectedItems(plantPanelsList);

            if (selectedPlants.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one plant.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create POOBvsZombies Instance
            poobvsZombies = new POOBvsZombies(
                    matchTime,
                    hordeNumber,
                    playerName,
                    selectedPlants);

            // Proceed to the next step (e.g., open GardenMenu)
            new GardenMenu(poobvsZombies).setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            try{
                throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
            }
            catch (POOBvsZombiesException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
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
        // Info Label
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
        panel.add(infoLabel);

        // "SELECT PLANTS" Label
        JLabel selectPlantLabel = new JLabel("<html>SELECT PLANTS</html>");
        selectPlantLabel.setBounds(385, 525, 500, 100);
        selectPlantLabel.setForeground(Color.WHITE);
        selectPlantLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        panel.add(selectPlantLabel);

        // Player Name Label
        JLabel playerNameLabel = new JLabel("PlayerName:");
        playerNameLabel.setBounds(260, 205, 150, 30);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(playerNameLabel);

        // Time Label
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setBounds(245, 345, 150, 30);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        panel.add(timeLabel);

        // Number Label
        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setBounds(320, 345, 150, 30);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 10));
        panel.add(numberLabel);
    }

    private void addPlantPanels(JPanel panel) {
        JPanel plantSelectionPanel = new JPanel();
        plantSelectionPanel.setBounds(115, 590, 600, 150);
        plantSelectionPanel.setLayout(new FlowLayout());
        plantSelectionPanel.setOpaque(false);

        for (List<String> plant : GardenMenu.PLANTS_VIEW) {
            String imagePath = plant.get(1);
            String plantName = plant.get(0);
            PlantPanel plantPanel = new PlantPanel(imagePath, plantName);
            plantPanelsList.add(plantPanel);
            plantSelectionPanel.add(plantPanel);
        }
        panel.add(plantSelectionPanel);
    }

    private void addTopRightButtons(JPanel panel) {
        String buttonImagePath = "resources/images/buttons/return-icon.png";
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
                // Back to main menu
                dispose(); // Close the current window
                new POOBvsZombiesGUI().setVisible(true);
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

        boolean playerNameFilled = !playerNameField.getText().isEmpty()
                && !playerNameField.getText().equals("Player Name");
        boolean matchTimeFilled = !matchTimeField.getText().isEmpty()
                && !matchTimeField.getText().equals("Time");
        boolean hordeNumberFilled = !hordeNumberField.getText().isEmpty()
                && !hordeNumberField.getText().equals("Number");

        startButton.setEnabled(atLeastOnePlantSelected && playerNameFilled && matchTimeFilled && hordeNumberFilled);
    }

    private ArrayList<String> getSelectedItems(List<PlantPanel> panels) {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (PlantPanel panel : panels) {
            if (panel.isSelected()) {
                selectedItems.add(panel.getPlantName());
            }
        }
        return selectedItems;
    }

    // Custom Classes
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon("resources/images/menu/PlayervsMachineMenu.png").getImage();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    class PlantPanel extends JPanel {
        private boolean selected = false;
        private Image plantImage;
        private String plantPath;
        private String plantName;

        public PlantPanel(String imagePath, String plantName) {
            this.plantPath = imagePath;
            this.plantName = plantName;
            plantImage = new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);

            // Change selection state on click
            addMouseListener(new java.awt.event.MouseAdapter() {
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

        public String getPlantName() {
            return plantName;
        }

        public String getPlantPath() {
            return plantPath;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(plantImage, 0, 0, getWidth(), getHeight(), this);
            if (selected) {
                g.setColor(new Color(0, 255, 0, 128)); // Semitransparente verde
            } else {
                g.setColor(new Color(255, 0, 0, 128)); // Semitransparente rojo
            }
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