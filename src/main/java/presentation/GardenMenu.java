package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

public class GardenMenu extends JFrame {
    private String[] selectedPlants;
    private String[] selectedZombies;
    private String state; // "PlayerVsPlayer" or "PlayerVsMachine"
    private JLabel shovelLabel;
    private Point originalShovelPosition;
    private JPanel[][] gridCells = new JPanel[5][10];

    public GardenMenu(String[] selectedPlants, String[] selectedZombies, String state) {
        this.selectedPlants = selectedPlants;
        this.selectedZombies = selectedZombies;
        this.state = state;
        setTitle("Garden Menu");
        setSize(900, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel with custom background
        JPanel panel = new JPanel() {
            Image backgroundImage = new ImageIcon("resources/images/gardenPvsP.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Display plant cards based on selection and make them draggable
        addCards(panel);

        // Add the shovel at the top right corner
        addShovel(panel);

        // Add the 5x10 GridLayout for cells
        addGridLayout(panel);

        // Add the "Use Shovel" button below the shovel
        addUseShovelButton(panel);

        // Add icons for returning to menus, saving, and exporting
        addTopRightButtons(panel);

        // Add zombie components only if in "PlayerVsPlayer" mode
        if ("PlayerVsPlayer".equals(state) || "MachineVsMachine".equals(state)) {
            addBrainIcon(panel);
            addZombieCards(panel);
            addZombieTable(panel);
        }

        add(panel);
    }

    private void addCards(JPanel panel) {
        // Paths of the cards corresponding to the selected plants
        String[] plantCards = {
            "resources/images/cards/Plants/card_sunflower.png",
            "resources/images/cards/Plants/card_peashooter.png",
            "resources/images/cards/Plants/card_wallnut.png",
            "resources/images/cards/Plants/card_potatomine.png",
            "resources/images/cards/Plants/card_ECIPlant.png"
        };

        // Paths of the GIFs or PNGs of the plants to drag
        String[] plantDragImages = {
            "resources/images/plants/Sunflower/sunflowerAnimated.gif",
            "resources/images/plants/Peashooter/peashooterAnimated.gif",
            "resources/images/plants/WallNut/wall-nutAnimated.gif",
            "resources/images/plants/PotatoMine/before-potato-mineAnimated.gif",
            "resources/images/plants/ECIPlant/ECIPlantAnimated.gif"
        };

        int x = 75; // Initial X position
        int y = -25; // Initial Y position
        for (int i = 0; i < selectedPlants.length; i++) {
            if (selectedPlants[i] != null) { // Only if it's a valid plant
                int plantIndex = getPlantIndex(selectedPlants[i]);
                if (plantIndex != -1) {
                    // Display the card
                    ImageIcon icon = new ImageIcon(plantCards[plantIndex]);
                    JLabel cardLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                    cardLabel.setBounds(x, y, 100, 150);
                    panel.add(cardLabel);

                    // Add drag functionality
                    String dragImagePath = plantDragImages[plantIndex];
                    cardLabel.setTransferHandler(new TransferHandler("icon") {
                        @Override
                        protected Transferable createTransferable(JComponent c) {
                            ImageIcon icon = new ImageIcon(dragImagePath);
                            return new ImageTransferable(icon.getImage(), "plant"); // specify type as "plant"
                        }

                        @Override
                        public int getSourceActions(JComponent c) {
                            return COPY;
                        }
                    });

                    cardLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            JComponent comp = (JComponent) e.getSource();
                            TransferHandler handler = comp.getTransferHandler();
                            if (handler != null) {
                                handler.exportAsDrag(comp, e, TransferHandler.COPY);
                            }
                        }
                    });

                    x += 70; // Move X position for the next card
                }
            }
        }
    }

    private int getPlantIndex(String plantPath) {
        // Map plants to their indices
        if (plantPath.contains("Sunflower")) return 0;
        if (plantPath.contains("Peashooter")) return 1;
        if (plantPath.contains("WallNut")) return 2;
        if (plantPath.contains("PotatoMine")) return 3;
        if (plantPath.contains("ECIPlant")) return 4;
        return -1; // Not found
    }

    private void addShovel(JPanel panel) {
        // Path of the shovel
        String shovelImagePath = "resources/images/shovel.png";

        // Load and scale the shovel
        ImageIcon shovelIcon = new ImageIcon(shovelImagePath);
        Image scaledShovelImage = shovelIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);

        // Create a JLabel to display the shovel
        shovelLabel = new JLabel(new ImageIcon(scaledShovelImage));
        shovelLabel.setBounds(520, 28, 50, 50); // Place at the top right corner
        originalShovelPosition = shovelLabel.getLocation();

        panel.add(shovelLabel);
    }

    private void addGridLayout(JPanel panel) {
        // Create a panel with a 5x10 GridLayout
        JPanel gridPanel = new JPanel(new GridLayout(5, 10, 5, 5)); // 5 rows, 10 columns, 5px spacing
        gridPanel.setBounds(40, 80, 800, 500); // Adjust position and size
        gridPanel.setOpaque(false); // Transparent to make the background visible

        // Path of the lawn mower
        String lawnMowerImagePath = "resources/images/Lawnmower.jpg";

        // Add cells to the grid with specific restrictions for plants and zombies
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                JPanel cellPanel = new JPanel(new BorderLayout());
                cellPanel.setPreferredSize(new Dimension(50, 50));
                cellPanel.setOpaque(false);
                cellPanel.setBorder(new LineBorder(new Color(0, 0, 0, 0), 1)); // Add transparent border to each cell

                if (col == 0) {
                    // Add the lawn mower to the first column of each row
                    ImageIcon mowerIcon = new ImageIcon(lawnMowerImagePath);
                    Image scaledMowerImage = mowerIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    JLabel mowerLabel = new JLabel(new ImageIcon(scaledMowerImage));
                    cellPanel.add(mowerLabel, BorderLayout.CENTER);
                } else if (col >= 1 && col <= 8) {
                    // Allow dragging and dropping plants only in columns 1 to 8
                    cellPanel.setTransferHandler(new TransferHandler("icon") {
                        @Override
                        public boolean canImport(TransferSupport support) {
                            if (!support.isDrop()) {
                                return false;
                            }
                            if (!support.isDataFlavorSupported(ImageTransferable.IMAGE_FLAVOR)) {
                                return false;
                            }

                            // Validate if the cell already has a plant
                            JPanel targetPanel = (JPanel) support.getComponent();
                            if (targetPanel.getComponentCount() > 0) {
                                return false; // There's already a plant in this cell
                            }

                            try {
                                ImageTransferable transferable = (ImageTransferable) support.getTransferable().getTransferData(ImageTransferable.IMAGE_FLAVOR);
                                return "plant".equals(transferable.getType()); // Only accept plants
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }

                        @Override
                        public boolean importData(TransferSupport support) {
                            if (!canImport(support)) {
                                return false;
                            }

                            try {
                                ImageTransferable transferable = (ImageTransferable) support.getTransferable().getTransferData(ImageTransferable.IMAGE_FLAVOR);
                                Image image = transferable.getImage();
                                JLabel label = new JLabel(new ImageIcon(image));
                                label.setHorizontalAlignment(JLabel.CENTER);
                                JPanel targetPanel = (JPanel) support.getComponent();
                                targetPanel.add(label);
                                targetPanel.revalidate();
                                targetPanel.repaint();
                                return true;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return false;
                        }
                    });
                } else if (col == 9) {
                    if ("PlayerVsPlayer".equals(state)) {
                        // Allow dragging and dropping zombies only in the last column
                        cellPanel.setTransferHandler(new TransferHandler("icon") {
                            @Override
                            public boolean canImport(TransferSupport support) {
                                if (!support.isDrop()) {
                                    return false;
                                }
                                if (!support.isDataFlavorSupported(ImageTransferable.IMAGE_FLAVOR)) {
                                    return false;
                                }

                                // Validate if the cell already has a zombie
                                JPanel targetPanel = (JPanel) support.getComponent();
                                if (targetPanel.getComponentCount() > 0) {
                                    return false; // There's already a zombie in this cell
                                }

                                try {
                                    ImageTransferable transferable = (ImageTransferable) support.getTransferable().getTransferData(ImageTransferable.IMAGE_FLAVOR);
                                    return "zombie".equals(transferable.getType()); // Only accept zombies
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            @Override
                            public boolean importData(TransferSupport support) {
                                if (!canImport(support)) {
                                    return false;
                                }

                                try {
                                    ImageTransferable transferable = (ImageTransferable) support.getTransferable().getTransferData(ImageTransferable.IMAGE_FLAVOR);
                                    Image image = transferable.getImage();
                                    JLabel label = new JLabel(new ImageIcon(image));
                                    label.setHorizontalAlignment(JLabel.CENTER);
                                    JPanel targetPanel = (JPanel) support.getComponent();
                                    targetPanel.add(label);
                                    targetPanel.revalidate();
                                    targetPanel.repaint();
                                    return true;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                return false;
                            }
                        });
                    }
                }

                gridCells[row][col] = cellPanel; // Save the cell reference
                gridPanel.add(cellPanel);
            }
        }

        panel.add(gridPanel);
    }

    private void addUseShovelButton(JPanel panel) {
        JButton useShovelButton = new JButton("Use Shovel");
        useShovelButton.setBounds(580, 55, 100, 30);
        useShovelButton.addActionListener(e -> {
            // Show JOptionPane to enter row and column
            JTextField rowField = new JTextField(2);
            JTextField colField = new JTextField(2);
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Row (0-4):"));
            inputPanel.add(rowField);
            inputPanel.add(Box.createHorizontalStrut(15)); // Space between fields
            inputPanel.add(new JLabel("Column (1-9):"));
            inputPanel.add(colField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Enter Row and Column to Remove Plant", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int row = Integer.parseInt(rowField.getText());
                    int col = Integer.parseInt(colField.getText());

                    // Validate limits and conditions
                    if (row < 0 || row > 4 || col < 1 || col > 9) {
                        JOptionPane.showMessageDialog(this, "Invalid row or column. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (col == 0) {
                        JOptionPane.showMessageDialog(this, "Cannot remove lawnmower.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JPanel targetPanel = gridCells[row][col];
                        if (targetPanel.getComponentCount() == 0) {
                            JOptionPane.showMessageDialog(this, "No plant to remove in the selected cell.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Remove the plant
                            targetPanel.removeAll();
                            targetPanel.revalidate();
                            targetPanel.repaint();
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numeric values for row and column.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(useShovelButton);
    }

    private void addTopRightButtons(JPanel panel) {
        String[] buttonImagePaths = {
            "resources/images/buttons/export-icon.png",     // Export
            "resources/images/buttons/save-icon.png",       // Save
            "resources/images/buttons/return-icon.png",     // Return
            "resources/images/buttons/home-icon.png"        // Back to main menu
        };

        int x = 660;
        int y = 5;
        int buttonSize = 40;

        for (String imagePath : buttonImagePaths) {
            ImageIcon icon = new ImageIcon(imagePath);
            JButton button = new JButton(new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
            button.setBounds(x, y, buttonSize, buttonSize);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            // Add action events to buttons
            if (imagePath.contains("return-icon")) {
                button.addActionListener(e -> {
                    dispose(); // Close the current window
                    if ("PlayerVsMachine".equals(state)) {
                        PlayerVSMachine pvmMenu = new PlayerVSMachine();
                        pvmMenu.setVisible(true);
                    } else if ("PlayerVsPlayer".equals(state)) {
                        PlayerVsPlayer pvpMenu = new PlayerVsPlayer();
                        pvpMenu.setVisible(true);
                    }
                    else{
                        MachineVSMachine mvsmMenu = new MachineVSMachine();
                        mvsmMenu.setVisible(true);
                    }
                });
            }

            if (imagePath.contains("home-icon")) {
                button.addActionListener(e -> {
                    // Back to main menu
                    dispose(); // Close the current window
                    MainMenu mainMenu = new MainMenu(); // Open the main menu
                    mainMenu.setVisible(true);
                });
            }

            panel.add(button);
            x += 60; // Adjust X position for the next button
        }
    }

    // Auxiliary class to handle the Transferable object of image type with type (plant or zombie)
    private static class ImageTransferable implements Transferable {
        public static final DataFlavor IMAGE_FLAVOR = new DataFlavor(ImageTransferable.class, "ImageTransferable");
        private Image image;
        private String type; // "plant" or "zombie"

        public ImageTransferable(Image image, String type) {
            this.image = image;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public Image getImage() {
            return image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{IMAGE_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(IMAGE_FLAVOR);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(IMAGE_FLAVOR)) {
                return this;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }

    private void addZombieCards(JPanel panel) {
        if ("PlayerVsPlayer".equals(state) || "MachineVsMachine".equals(state) && selectedZombies != null) {
            // Paths of the cards corresponding to the zombies
            String[] zombieCards = {
                "resources/images/cards/Zombies/card_basic_zombie.png",
                "resources/images/cards/Zombies/card_brainstein.png",
                "resources/images/cards/Zombies/card_buckethead_zombie.png",
                "resources/images/cards/Zombies/card_conehead_zombie.png",
                "resources/images/cards/Zombies/card_ECIZombie.png"
            };

            // Paths of the GIFs or PNGs of the zombies to drag
            String[] zombieDragImages = {
                "resources/images/zombies/Basic/BasicDinamic.gif",
                "resources/images/zombies/Brainstein/brainsteinAnimated.gif",
                "resources/images/zombies/BucketHead/BucketheadAnimated.gif",
                "resources/images/zombies/Conehead/ConeheadAnimated.gif",
                "resources/images/zombies/ECIZombie/ECIZombieAnimated.gif"
            };

            int x = 85; // Initial X position
            int y = 625; // Initial Y position
            for (int i = 0; i < selectedZombies.length; i++) {
                if (selectedZombies[i] != null) {
                    int zombieIndex = getZombieIndex(selectedZombies[i]);
                    if (zombieIndex != -1) {
                        // Display the card
                        ImageIcon icon = new ImageIcon(zombieCards[zombieIndex]);
                        JLabel cardLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                        cardLabel.setBounds(x, y - 85, 100, 150); // Adjust y position to paint over the table
                        panel.add(cardLabel);

                        // Add drag functionality
                        String dragImagePath = zombieDragImages[zombieIndex];
                        cardLabel.setTransferHandler(new TransferHandler("icon") {
                            @Override
                            protected Transferable createTransferable(JComponent c) {
                                ImageIcon icon = new ImageIcon(dragImagePath);
                                return new ImageTransferable(icon.getImage(), "zombie"); // specify type as "zombie"
                            }

                            @Override
                            public int getSourceActions(JComponent c) {
                                return COPY;
                            }
                        });

                        cardLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                JComponent comp = (JComponent) e.getSource();
                                TransferHandler handler = comp.getTransferHandler();
                                if (handler != null) {
                                    handler.exportAsDrag(comp, e, TransferHandler.COPY);
                                }
                            }
                        });

                        x += 70; // Move X position for the next card
                    }
                }
            }
        }
    }

    private int getZombieIndex(String zombiePath) {
        // Map zombies to their indices
        if (zombiePath.contains("Basic")) return 0;
        if (zombiePath.contains("Brainstein")) return 1;
        if (zombiePath.contains("BucketHead")) return 2;
        if (zombiePath.contains("Conehead")) return 3;
        if (zombiePath.contains("ECIZombie")) return 4;
        return -1; // Not found
    }

    private void addZombieTable(JPanel panel) {
        if ("PlayerVsPlayer".equals(state) || "MachineVsMachine".equals(state)) {
            // Path of the zombie table image
            String zombieTableImagePath = "resources/images/zombie-table.png";

            // Load and scale the image
            ImageIcon zombieTableIcon = new ImageIcon(zombieTableImagePath);
            Image scaledZombieTableImage = zombieTableIcon.getImage().getScaledInstance(460, 80, Image.SCALE_SMOOTH);

            // Create a JLabel to display the zombie table
            JLabel zombieTableLabel = new JLabel(new ImageIcon(scaledZombieTableImage));
            zombieTableLabel.setBounds(10, 575, 460, 80); // Adjust position and size

            panel.add(zombieTableLabel);
        }
    }

    private void addBrainIcon(JPanel panel) {
        if ("PlayerVsPlayer".equals(state) || "MachineVsMachine".equals(state)) {
            // Path of the brain image
            String brainImagePath = "resources/images/brain.png";

            // Load and scale the image
            ImageIcon brainIcon = new ImageIcon(brainImagePath);
            Image scaledBrainImage = brainIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

            // Create a JLabel to display the brain
            JLabel brainLabel = new JLabel(new ImageIcon(scaledBrainImage));
            brainLabel.setBounds(50, 585, 40, 40); // Adjust position and size

            // Create a JLabel to display the text "100"
            JLabel textLabel = new JLabel("100");
            textLabel.setForeground(Color.WHITE);
            textLabel.setFont(textLabel.getFont().deriveFont(20f));
            textLabel.setBounds(50, 625, 80, 30); // Adjust position and size

            panel.add(brainLabel);
            panel.add(textLabel);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] selectedPlants = {
                "Sunflower",
                "Peashooter",
                "WallNut",
                "PotatoMine",
                "ECIPlant"
            };
            String[] selectedZombies = {
                "Basic",
                "Conehead",
                "BucketHead",
                "ECIZombie",
                "Brainstein"
            };
            GardenMenu frame = new GardenMenu(selectedPlants, selectedZombies, "PlayerVsPlayer");
            frame.setVisible(true);
        });
    }
}
