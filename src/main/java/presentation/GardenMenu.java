package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container; // Add this import
import java.awt.Cursor;
import java.awt.Dimension; // Add this import
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator; // Add this import
import java.util.List;
import java.util.Map;

import javax.swing.Box; // Add this import
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.border.LineBorder;

import domain.Basic;
import domain.Brainstein;
import domain.Buckethead;
import domain.Conehead;
import domain.ECIPlant;
import domain.ECIZombie;
import domain.Entity;
import domain.POOBvsZombies;
import domain.Peashooter;
import domain.Plant;
import domain.Plants;
import domain.Player;
import domain.PotatoMine;
import domain.Resource;
import domain.ResourceGenerator;
import domain.Sunflower;
import domain.WallNut;
import domain.Zombie;

public class GardenMenu extends JFrame {
    private ArrayList<String> selectedPlants;
    private ArrayList<String> selectedZombies;
    private String modality; // "PlayerVsPlayer" or "PlayerVsMachine"
    private JLabel shovelLabel;
    private Point originalShovelPosition;
    private JPanel[][] gridCells = new JPanel[5][10];
    private boolean shovelSelected = false;
    private java.util.List<JLabel> movingZombies = new java.util.ArrayList<>();
    private POOBvsZombies poobvszombies;
    private JLabel playerOneNameLabel, playerTwoNameLabel;
    private JLabel playerOneSunsLabel, playerTwoBrainsLabel;
    private JLabel playerOneScoreLabel, playerTwoScoreLabel;
    private Timer resourceGenerationTimer; // Timer cada 20 segundos
    // Mapa para gestionar timers individuales por entidad
    private Map<Entity, Timer> entityTimers = new HashMap<>();

    public static final List<List<String>> ZOMBIES_VIEW = Arrays.asList(
            Arrays.asList(
                    "Basic",
                    "resources/images/zombies/Basic/Basic.jpg",
                    "resources/images/cards/Zombies/card_basic_zombie.png",
                    "resources/images/zombies/Basic/BasicDinamic.gif"),
            Arrays.asList(
                    "Brainstein",
                    "resources/images/zombies/Brainstein/brainsteinGarden.jpeg",
                    "resources/images/cards/Zombies/card_brainstein.png",
                    "resources/images/zombies/Brainstein/brainsteinAnimated.gif"),
            Arrays.asList(
                    "BucketHead",
                    "resources/images/zombies/BucketHead/Buckethead.jpg",
                    "resources/images/cards/Zombies/card_buckethead_zombie.png",
                    "resources/images/zombies/BucketHead/BucketheadAnimated.gif"),
            Arrays.asList(
                    "Conehead",
                    "resources/images/zombies/Conehead/Conehead.jpg",
                    "resources/images/cards/Zombies/card_conehead_zombie.png",
                    "resources/images/zombies/Conehead/ConeheadAnimated.gif"),
            Arrays.asList(
                    "ECIZombie",
                    "resources/images/zombies/ECIZombie/ECIZombie.png",
                    "resources/images/cards/Zombies/card_ECIZombie.png",
                    "resources/images/zombies/ECIZombie/ECIZombieAnimated.gif"));

    public static final List<List<String>> PLANTS_VIEW = Arrays.asList(
            Arrays.asList(
                    "Sunflower",
                    "resources/images/plants/Sunflower/Sunflower.jpg",
                    "resources/images/cards/Plants/card_sunflower.png",
                    "resources/images/plants/Sunflower/sunflowerAnimated.gif"),
            Arrays.asList(
                    "Peashooter",
                    "resources/images/plants/Peashooter/Peashooter.jpg",
                    "resources/images/cards/Plants/card_peashooter.png",
                    "resources/images/plants/Peashooter/peashooterAnimated.gif"),
            Arrays.asList(
                    "WallNut",
                    "resources/images/plants/WallNut/Wall-nutGrass.jpg",
                    "resources/images/cards/Plants/card_wallnut.png",
                    "resources/images/plants/WallNut/wall-nutAnimated.gif"),
            Arrays.asList(
                    "PotatoMine",
                    "resources/images/plants/PotatoMine/Potato_MineGrass.jpg",
                    "resources/images/cards/Plants/card_potatomine.png",
                    "resources/images/plants/PotatoMine/before-potato-mineAnimated.gif"),
            Arrays.asList(
                    "ECIPlant",
                    "resources/images/plants/ECIPlant/ECIPlant.png",
                    "resources/images/cards/Plants/card_ECIPlant.png",
                    "resources/images/plants/ECIPlant/ECIPlantAnimated.gif"));
    private List<TimerTask> timerTasks = new ArrayList<>();

    // Etiquetas para mostrar el mensaje y el tiempo
    private JLabel messageLabel;
    private JLabel timeLabel;
    // Añade este mapa en la declaración de variables de la clase GardenMenu
    private Map<Entity, Timer> imageChangeTimers = new HashMap<>();


    public GardenMenu(POOBvsZombies poobvszombies) {
        setTitle("Garden Menu");
        setSize(900, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.poobvszombies = poobvszombies;
        this.modality = poobvszombies.getModality();
        Player playerOne = poobvszombies.getPlayerOne();
        Player playerTwo = poobvszombies.getPlayerTwo();
        this.selectedPlants = playerOne.getTeam().getCharacters();
        this.selectedZombies = playerTwo.getTeam().getCharacters();

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
        addPlantsCards(panel);

        // Add the shovel at the top right corner
        addShovel(panel);

        // Add the 5x10 GridLayout for cells
        addGridLayout(panel);

        // Add the "Use Shovel" button below the shovel
        addUseShovelButton(panel);

        // Add icons for returning to menus, saving, and exporting
        addTopRightButtons(panel);

        // Add zombie components only if in "PlayerVsPlayer" mode

        if ("PlayerVsPlayer".equals(modality)) {
            addPlayerInfo(panel);
            addScoreLabels(panel);
            addBrainIcon(panel);
            addZombieCards(panel);
            addZombieTable(panel);
        } else if ("PlayerVsMachine".equals(modality)) {
            addPlayerInfo(panel); // Solo mostrar información del jugador
            addScoreLabels(panel);
        } else if ("MachineVsMachine".equals(modality)) {
            addPlayerInfo(panel);
            addScoreLabels(panel);
            addBrainIcon(panel);
            addZombieCards(panel);
            addZombieTable(panel);
            
            
            
        }

        // addTimerSection(panel, poobvszombies);
        initializeTimers(poobvszombies);
        setupTimerLabels(panel);
        startSequentialTimers(0);
        
        add(panel);
        startZombieMovement();

        
        poobvszombies.setGardenMenu(this);
    }

    private void addPlantsCards(JPanel panel) {

        int x = 75; // Initial X position
        int y = -25; // Initial Y position
        for (String plantName : selectedPlants) {
            if (plantName != null) { // Only if it's a valid plant
                // int plantIndex = getPlantIndex(selectedPlants[i]);
                // if (plantIndex != -1) {
                // Display the card
                List<String> plant = PLANTS_VIEW.get(0);

                // Buscar la planta en la lista de plantas y asignarla a la variable plant para
                // tener todas sus posibles representaciones graficas
                for (List<String> plants : PLANTS_VIEW) {
                    if (plants.get(0).equals(plantName)) {
                        plant = plants;
                        break;
                    }
                }

                if (plant == null)
                    continue;

                ImageIcon icon = new ImageIcon(plant.get(2));
                JLabel cardLabel = new JLabel(
                        new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                cardLabel.setBounds(x, y, 100, 150);
                panel.add(cardLabel);

                // Add drag functionality
                String dragImagePath = plant.get(3);

                cardLabel.setTransferHandler(new TransferHandler("icon") {
                    @Override
                    protected Transferable createTransferable(JComponent c) {
                        ImageIcon dragIcon = new ImageIcon(dragImagePath);
                        EntityData entityData = new EntityData("plant", plantName, dragIcon.getImage());
                        return new EntityTransferable(entityData);
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
                // }
            }
        }
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

        shovelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shovelSelected = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambiar el cursor
                // planta para removerla.");
                // JOptionPane.showMessageDialog(null, "Pala seleccionada. Haz clic en una
                // planta para removerla.");
            }
        });

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
                final int finalRow = row;
                final int finalCol = col;

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

                    poobvszombies.addEntity(finalRow, finalCol, "LownMover");
                } else if (col >= 1 && col <= 8) {
                    // Allow dragging and dropping plants only in columns 1 to 8
                    cellPanel.setTransferHandler(new TransferHandler("icon") {
                        @Override
                        public boolean canImport(TransferSupport support) {
                            if (!support.isDrop()) {
                                return false;
                            }
                            if (!support.isDataFlavorSupported(EntityTransferable.ENTITY_FLAVOR)) {
                                return false;
                            }

                            // Validate if the cell already has a plant
                            JPanel targetPanel = (JPanel) support.getComponent();
                            if (targetPanel.getComponentCount() > 0) {
                                return false; // There's already a plant in this cell
                            }

                            try {

                                // El transfaerable es un objeto de tipo EntityTransferable en donde esta
                                // guardado entre otras cosas, imagen, hicimos que tambien tenga el string del
                                // nombre de la entidad
                                // EntityTransferable transferable = (EntityTransferable)
                                // support.getTransferable()
                                // .getTransferData(EntityTransferable.IMAGE_FLAVOR);

                                // vamos a hacer que al añadir una imagen, se mande a dominio como addEntity
                                EntityData entityData = (EntityData) support.getTransferable()
                                        .getTransferData(EntityTransferable.ENTITY_FLAVOR);

                                // Aquí identificamos el tipo de planta a colocar
                                // Supongamos que tienes un método para obtener el nombre de la carta
                                // arrastrada.
                                // Este podría ser pasado en el Transferable o tener un mapeo global.
                                String plantName = entityData.getName(); // Necesitas implementar este método

                                // Crear instancia de la planta correspondiente
                                Plant plant = createPlantInstance(plantName);
                                if (plant == null)
                                    return false;

                                // Verificar recursos
                                Player playerOne = poobvszombies.getPlayerOne();
                                if (playerOne.getTeam().getResourceCounterAmount() < plant.getCost()) {
                                    // JOptionPane.showMessageDialog(null, "No tienes suficientes soles para colocar
                                    // esta planta.");
                                    return false;
                                }

                                return "plant".equals(entityData.getType()); // Only accept plants

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
                                EntityData entityData = (EntityData) support.getTransferable()
                                        .getTransferData(EntityTransferable.ENTITY_FLAVOR);
                                // poobvszombies.addEntity(finalRow, finalCol, entityData.getName());

                                // Añadir a dominio
                                poobvszombies.addEntity(finalRow, finalCol, entityData.getName());
                                //showEntityMatrix();

                                Image image = entityData.getImage();

                                JLabel label = new JLabel(new ImageIcon(image));
                                label.setHorizontalAlignment(JLabel.CENTER);
                                JPanel targetPanel = (JPanel) support.getComponent();
                                targetPanel.add(label);
                                targetPanel.revalidate();
                                targetPanel.repaint();

                                // NUEVO: Deducir recursos
                                String plantName = entityData.getName();
                                Plant plant = createPlantInstance(plantName);
                                if (plant != null) {
                                    Player playerOne = poobvszombies.getPlayerOne();
                                    // Deducir el costo de la planta
                                    playerOne.getTeam().deductResource(plant.getCost());
                                    // Actualizar la etiqueta de soles
                                    playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
                                }

                                // Iniciar Timer si la entidad es un generador de recursos
                                if (plant instanceof ResourceGenerator) {
                                    Timer plantTimer = createEntityTimer((ResourceGenerator) plant, finalRow, finalCol);
                                    entityTimers.put(plant, plantTimer);
                                    plantTimer.start();
                                }
                                if (plant instanceof PotatoMine) {
                                    Timer imageChangeTimer = new Timer(14000, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            System.out.println("Timer disparado para cambiar la imagen de PotatoMine.");
                                            
                                            // Crear el ImageIcon directamente con el GIF animado
                                            ImageIcon newIcon = new ImageIcon("resources/images/plants/PotatoMine/potato-mineAnimated.gif");
                                            
                                            // Verificar que la imagen se haya cargado correctamente
                                            if (newIcon.getIconWidth() == -1) {
                                                System.err.println("No se pudo cargar la imagen: resources/images/plants/PotatoMine/potato-mineAnimated.gif");
                                                return;
                                            } else {
                                                System.out.println("Imagen potato-mineAnimated.gif cargada correctamente.");
                                            }
                                
                                            // Asignar el ImageIcon directamente sin escalar
                                            label.setIcon(newIcon);
                                            
                                            // Ajustar el tamaño del JLabel si es necesario
                                            label.setPreferredSize(new Dimension(newIcon.getIconWidth()-10, newIcon.getIconHeight()-10));
                                            label.revalidate();
                                            label.repaint();
                                            System.out.println("Icono del JLabel actualizado con el GIF animado.");
                                
                                            // Traer el JLabel al frente
                                            targetPanel.setComponentZOrder(label, 0);
                                            targetPanel.revalidate();
                                            targetPanel.repaint();
                                            System.out.println("JLabel traído al frente.");
                                
                                            // Detener el Timer ya que es una acción única
                                            ((Timer) e.getSource()).stop();
                                            System.out.println("Timer de cambio de imagen detenido.");
                                        }
                                    });
                                    imageChangeTimer.setRepeats(false); // Ejecutar solo una vez
                                    imageChangeTimer.start();
                                    imageChangeTimers.put(plant, imageChangeTimer);
                                    System.out.println("Timer de cambio de imagen iniciado para PotatoMine.");
                                }
                                
                                
                                return true;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return false;
                        }
                    });

                   // Dentro del MouseListener para usar la pala en la clase GardenMenu
                    cellPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (shovelSelected) {
                                if (cellPanel.getComponentCount() > 0) {
                                    // Obtener la entidad de dominio
                                    Entity entity = poobvszombies.getEntitiesMatrix().get(finalRow).get(finalCol);  
                                    if (entity != null) {
                                        // Detener y remover el Timer asociado (generación de recursos)
                                        Timer resourceTimer = entityTimers.get(entity);
                                        if (resourceTimer != null) {
                                            resourceTimer.stop();
                                            entityTimers.remove(entity);
                                            System.out.println("Timer de recursos detenido y removido para la entidad: " + entity.getName());
                                        }

                                        // Detener y remover el Timer de cambio de imagen si es PotatoMine
                                        if (entity instanceof PotatoMine) {
                                            Timer imageChangeTimer = imageChangeTimers.get(entity);
                                            if (imageChangeTimer != null) {
                                                imageChangeTimer.stop();
                                                imageChangeTimers.remove(entity);
                                                System.out.println("Timer de cambio de imagen detenido y removido para PotatoMine.");
                                            }
                                        }
                                    }

                                    // Remover la planta visualmente
                                    cellPanel.removeAll();
                                    cellPanel.revalidate();
                                    cellPanel.repaint();
                                    poobvszombies.deleteEntity(finalRow, finalCol); // Implementa este método en POOBvsZombies
                                    shovelSelected = false;
                                    setCursor(Cursor.getDefaultCursor()); // Restablecer el cursor
                                    System.out.println("Planta eliminada en la celda (" + finalRow + ", " + finalCol + ").");
                                } else {
                                    JOptionPane.showMessageDialog(null, "No hay planta en esta celda.");
                                }
                            }
                        }
                    });


                } else if (col == 9) {
                    if ("PlayerVsPlayer".equals(modality)) {
                        // Allow dragging and dropping zombies only in the last column
                        cellPanel.setTransferHandler(new TransferHandler("icon") {
                            @Override
                            public boolean canImport(TransferSupport support) {
                                if (!support.isDrop()) {
                                    return false;
                                }
                                if (!support.isDataFlavorSupported(EntityTransferable.ENTITY_FLAVOR)) {
                                    return false;
                                }

                                // Validate if the cell already has a zombie
                                JPanel targetPanel = (JPanel) support.getComponent();
                                if (targetPanel.getComponentCount() > 0) {
                                    return false; // There's already a zombie in this cell
                                }

                                try {
                                    EntityData entityData = (EntityData) support.getTransferable()
                                            .getTransferData(EntityTransferable.ENTITY_FLAVOR);

                                    String zombieName = entityData.getName();
                                    Zombie zombie = createZombieInstance(zombieName);
                                    if (zombie == null)
                                        return false;

                                    Player playerTwo = poobvszombies.getPlayerTwo();
                                    if (playerTwo.getTeam().getResourceCounterAmount() < zombie.getCost()) {
                                        // JOptionPane.showMessageDialog(null, "No tienes suficientes cerebros para
                                        // colocar este zombie.");
                                        return false;
                                    }

                                    return "zombie".equals(entityData.getType());
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
                                    EntityData entityData = (EntityData) support.getTransferable()
                                            .getTransferData(EntityTransferable.ENTITY_FLAVOR);
                                    // Añadir a dominio
                                    poobvszombies.addEntity(finalRow, finalCol, entityData.getName());
                                    //showEntityMatrix();
                                    Image image = entityData.getImage();
                                    JLabel zombieLabel = new JLabel(new ImageIcon(image));
                                    zombieLabel.setHorizontalAlignment(JLabel.CENTER);
                                    // Get the cell's position relative to the main panel
                                    JPanel targetCell = (JPanel) support.getComponent();
                                    Point cellLocation = targetCell.getLocation();
                                    int x = gridPanel.getX() + cellLocation.x;
                                    int y = gridPanel.getY() + cellLocation.y;
                                    // Set the zombie's bounds
                                    zombieLabel.setBounds(x, y, targetCell.getWidth(), targetCell.getHeight());
                                    // Add the zombie to the main panel
                                    panel.add(zombieLabel);
                                    panel.setComponentZOrder(zombieLabel, 0); // Bring to front if necessary
                                    panel.revalidate();
                                    panel.repaint();

                                    // NUEVO: Deducir recursos
                                    String zombieName = entityData.getName();
                                    Zombie zombie = createZombieInstance(zombieName);
                                    if (zombie != null) {
                                        Player playerTwo = poobvszombies.getPlayerTwo();
                                        playerTwo.getTeam().deductResource(zombie.getCost());
                                        playerTwoBrainsLabel
                                                .setText("" + playerTwo.getTeam().getResourceCounterAmount());
                                    }
                                    // Add the zombie to the movingZombies list
                                    movingZombies.add(zombieLabel);
                                    // Iniciar Timer si la entidad es un generador de recursos
                                    if (zombie instanceof ResourceGenerator) {
                                        Timer zombieTimer = createEntityTimer((ResourceGenerator) zombie, finalRow, finalCol);
                                        entityTimers.put(zombie, zombieTimer);
                                        zombieTimer.start();
                                    }
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

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Enter Row and Column to Remove Plant",
                    JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int row = Integer.parseInt(rowField.getText());
                    int col = Integer.parseInt(colField.getText());

                    // Validate limits and conditions
                    if (row < 0 || row > 4 || col < 1 || col > 9) {
                        JOptionPane.showMessageDialog(this, "Invalid row or column. Please enter valid numbers.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (col == 0) {
                        JOptionPane.showMessageDialog(this, "Cannot remove lawnmower.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        JPanel targetPanel = gridCells[row][col];
                        if (targetPanel.getComponentCount() == 0) {
                            JOptionPane.showMessageDialog(this, "No plant to remove in the selected cell.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Remove the plant
                            // Obtener la entidad de dominio
                            Entity entity = poobvszombies.getEntitiesMatrix().get(row).get(col);    
                            if (entity != null) {
                                // Detener y remover el Timer asociado
                                Timer timer = entityTimers.get(entity);
                                if (timer != null) {
                                    timer.stop();
                                    entityTimers.remove(entity);
                                }
                            }

                            // Remover la planta visualmente
                            targetPanel.removeAll();
                            targetPanel.revalidate();
                            targetPanel.repaint();
                            poobvszombies.deleteEntity(row, col); // Implementa este método en POOBvsZombies
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numeric values for row and column.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(useShovelButton);
    }

    private void addTopRightButtons(JPanel panel) {
        String[] buttonImagePaths = {
                "resources/images/buttons/export-icon.png", // Export
                "resources/images/buttons/save-icon.png", // Save
                "resources/images/buttons/return-icon.png", // Return
                "resources/images/buttons/home-icon.png" // Back to main menu
        };

        int x = 660;
        int y = 5;
        int buttonSize = 40;

        for (String imagePath : buttonImagePaths) {
            ImageIcon icon = new ImageIcon(imagePath);
            JButton button = new JButton(
                    new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
            button.setBounds(x, y, buttonSize, buttonSize);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            // Agregar eventos de acción a los botones
            if (imagePath.contains("export-icon")) {
                button.addActionListener(e -> {
                    // Implementar funcionalidad para exportar el estado del juego
                    JOptionPane.showMessageDialog(this, "Funcionalidad de exportar aún no implementada.", "Exportar",
                            JOptionPane.INFORMATION_MESSAGE);
                });
            }

            if (imagePath.contains("save-icon")) {
                button.addActionListener(e -> {
                    // Implementar funcionalidad para guardar el estado del juego
                    JOptionPane.showMessageDialog(this, "Funcionalidad de guardar aún no implementada.", "Guardar",
                            JOptionPane.INFORMATION_MESSAGE);
                });
            }

            // Add action events to buttons
            if (imagePath.contains("return-icon")) {
                button.addActionListener(e -> {
                    dispose(); // Close the current window
                    if ("PlayerVsMachine".equals(modality)) {
                        PlayerVSMachine pvmMenu = new PlayerVSMachine();
                        pvmMenu.setVisible(true);
                    } else if ("PlayerVsPlayer".equals(modality)) {
                        PlayerVsPlayer pvpMenu = new PlayerVsPlayer();
                        pvpMenu.setVisible(true);
                    } else {
                        MachineVSMachine mvsmMenu = new MachineVSMachine();
                        //mvsmMenu.setVisible(true);
                    }
                });
            }

            if (imagePath.contains("home-icon")) {
                button.addActionListener(e -> {
                    // Back to main menu
                    dispose(); // Close the current window
                    POOBvsZombiesGUI POOBvsZombiesGUI = new POOBvsZombiesGUI(); // Open the main menu
                    POOBvsZombiesGUI.setVisible(true);
                });
            }

            panel.add(button);
            x += 60; // Adjust X position for the next button
        }
    }

    private void addPlayerInfo(JPanel panel) {
        if (("PlayerVsPlayer".equals(modality) || "PlayerVsMachine".equals(modality))
                || "MachineVsMachine".equals(modality)) {
            Player playerOne = poobvszombies.getPlayerOne();

            // Etiqueta para el nombre del Jugador 1
            playerOneNameLabel = new JLabel("" + playerOne.getName());
            playerOneNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneNameLabel.setForeground(Color.YELLOW);
            playerOneNameLabel.setBounds(480, 595, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneNameLabel);

            // Etiqueta para los soles iniciales del Jugador 1
            playerOneSunsLabel = new JLabel("" + playerOne.getTeam().getResourceCounterAmount());
            playerOneSunsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneSunsLabel.setForeground(Color.ORANGE);
            playerOneSunsLabel.setBounds(30, 60, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneSunsLabel);
    
            if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)) {
                Player playerTwo = poobvszombies.getPlayerTwo();

                // Etiqueta para el nombre del Jugador 2
                playerTwoNameLabel = new JLabel("" + playerTwo.getName());
                playerTwoNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoNameLabel.setForeground(Color.RED);
                playerTwoNameLabel.setBounds(480, 625, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoNameLabel);

                // Etiqueta para los cerebros iniciales del Jugador 2
                playerTwoBrainsLabel = new JLabel("" + playerTwo.getTeam().getResourceCounterAmount());
                playerTwoBrainsLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoBrainsLabel.setForeground(Color.MAGENTA);
                playerTwoBrainsLabel.setBounds(50, 620, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoBrainsLabel);
            }

            if ("PlayerVsMachine".equals(modality)) {
                Player playerTwo = poobvszombies.getPlayerTwo();
    
                // Etiqueta para el nombre del Jugador 2
                playerTwoNameLabel = new JLabel("" + playerTwo.getName());
                playerTwoNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoNameLabel.setForeground(Color.RED);
                playerTwoNameLabel.setBounds(480, 625, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoNameLabel);
            }
        }
    }

    /**
     * Método para agregar las etiquetas que muestran los puntajes de los jugadores.
     */
    private void addScoreLabels(JPanel panel) {
        // Etiqueta para el puntaje del Jugador 1
        if ("PlayerVsPlayer".equals(modality) || "PlayerVsMachine".equals(modality)
                || "MachineVsMachine".equals(modality)) {
            playerOneScoreLabel = new JLabel("Score: 0");
            playerOneScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneScoreLabel.setForeground(Color.YELLOW);
            playerOneScoreLabel.setBounds(560, 595, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneScoreLabel);

            if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)
                    || "PlayerVsMachine".equals(modality)) {
                // Etiqueta para el puntaje del Jugador 2
                playerTwoScoreLabel = new JLabel("Score: 0");
                playerTwoScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoScoreLabel.setForeground(Color.RED);
                playerTwoScoreLabel.setBounds(560, 625, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoScoreLabel);
            }
        }
    }
    // Auxiliary class to handle the Transferable object of image type with type
    // (plant or zombie)
    // private static class EntityTransferable implements Transferable {
    // public static final DataFlavor IMAGE_FLAVOR = new
    // DataFlavor(EntityTransferable.class, "ImageTransferable");
    // private Image image;
    // private String type; // "plant" or "zombie"
    // private String stringDefnitionName;

    // public ImageTransferable(Image image, String type, String
    // stringDefnitionName) {
    // this.image = image;
    // this.type = type;
    // this.stringDefnitionName = stringDefnitionName;
    // }

    // public String getType() {
    // return type;
    // }

    // public Image getImage() {
    // return image;
    // }

    // public String getStringDefnitionName() {
    // return stringDefnitionName;
    // }

    // @Override
    // public DataFlavor[] getTransferDataFlavors() {
    // return new DataFlavor[] { IMAGE_FLAVOR };
    // }

    // @Override
    // public boolean isDataFlavorSupported(DataFlavor flavor) {
    // return flavor.equals(IMAGE_FLAVOR);
    // }

    // @Override
    // public Object getTransferData(DataFlavor flavor) throws
    // UnsupportedFlavorException {
    // if (flavor.equals(IMAGE_FLAVOR)) {
    // return this;
    // } else {
    // throw new UnsupportedFlavorException(flavor);
    // }
    // }
    // }

    private void addZombieCards(JPanel panel) {
        if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality) && selectedZombies != null) {

            int x = 85; // Initial X position
            int y = 625; // Initial Y position
            for (String zombieName : selectedZombies) {
                if (zombieName != null) {
                    // int zombieIndex = getZombieIndex(zombieName);
                    // if (zombieIndex != -1) {
                    // Display the card

                    List<String> zombie = null;
                    for (List<String> zombies : ZOMBIES_VIEW) {
                        // Buscar el zombie en la lista de zombies y asignarlo a la variable zombie para
                        // tener todas sus posibles representaciones graficas
                        if (zombies.get(0).equals(zombieName)) {
                            // zombie ahora será un arreglo con todas las representaciones graficas del
                            // zombie
                            zombie = zombies;
                            break;
                        }
                    }

                    if (zombie == null)
                        continue;

                    // Add drag functionality
                    String dragImagePath = zombie.get(3);
                    ImageIcon icon = new ImageIcon(zombie.get(2));
                    JLabel cardLabel = new JLabel(
                            new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                    cardLabel.setBounds(x, y - 85, 100, 150);
                    panel.add(cardLabel);
                    cardLabel.setTransferHandler(new TransferHandler("icon") {
                        @Override
                        protected Transferable createTransferable(JComponent c) {
                            ImageIcon dragIcon = new ImageIcon(dragImagePath);
                            EntityData entityData = new EntityData("zombie", zombieName, dragIcon.getImage());
                            return new EntityTransferable(entityData);
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
                    // }
                }
            }
        }
    }

    private void addZombieTable(JPanel panel) {
        if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)) {
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
        if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)) {
            // Path of the brain image
            String brainImagePath = "resources/images/brain.png";

            // Load and scale the image
            ImageIcon brainIcon = new ImageIcon(brainImagePath);
            Image scaledBrainImage = brainIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

            // Create a JLabel to display the brain
            JLabel brainLabel = new JLabel(new ImageIcon(scaledBrainImage));
            brainLabel.setBounds(50, 585, 40, 40); // Adjust position and size

            // Create a JLabel to display the text "100"
            JLabel textLabel = new JLabel("");
            textLabel.setForeground(Color.WHITE);
            textLabel.setFont(textLabel.getFont().deriveFont(20f));
            textLabel.setBounds(50, 625, 80, 30); // Adjust position and size

            panel.add(brainLabel);
            panel.add(textLabel);
        }
    }

    private void startZombieMovement() {
        Timer timer = new Timer(100, e -> {
            Iterator<JLabel> iterator = movingZombies.iterator();
            while (iterator.hasNext()) {
                JLabel zombie = iterator.next();
                Point location = zombie.getLocation();
                if (location.x > 0) {
                    // Move zombie to the left
                    zombie.setLocation(location.x - 5, location.y);
                } else {
                    // Remove zombie safely
                    Container parent = zombie.getParent();
                    if (parent != null) {
                        parent.remove(zombie);
                        parent.revalidate();
                        parent.repaint();
                    }
                    iterator.remove();
                }
            }
        });
        timer.start();
    }

    // Clase interna para representar cada temporizador
    private class TimerTask {
        private String message;
        private int durationInSeconds;

        public TimerTask(String message, int durationInSeconds) {
            this.message = message;
            this.durationInSeconds = durationInSeconds;
        }

        public String getMessage() {
            return message;
        }

        public int getDurationInSeconds() {
            return durationInSeconds;
        }
    }

    // Método para crear y mostrar el recurso en el tablero
    private void spawnResource(Resource resource) {
        // Seleccionar una celda aleatoria (5 filas, columnas de 1 a 9)
        int row = (int) (Math.random() * 5);
        int col = 1 + (int) (Math.random() * 9);

        final JPanel targetCell = gridCells[row][col];
        if (targetCell.getComponentCount() > 0) {
            // Si la celda está ocupada, intentar nuevamente (una única reintento por
            // simplicidad)
            row = (int) (Math.random() * 5);
            col = 1 + (int) (Math.random() * 9);
            JPanel newTargetCell = gridCells[row][col];
        }

        // Crear JLabel con la imagen del recurso
        String imagePath = (resource.getType().equals(Resource.SOL)) ? "resources/images/sun.png"
                : "resources/images/brain.png";
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel resourceLabel = new JLabel(new ImageIcon(scaledImage));
        resourceLabel.setHorizontalAlignment(JLabel.CENTER);
        targetCell.add(resourceLabel);
        targetCell.revalidate();
        targetCell.repaint();

        // Timer de 3 segundos para recoger el recurso automáticamente
        Timer collectTimer = new Timer(3000, (ActionEvent e) -> {
            // Remover el recurso del tablero
            targetCell.remove(resourceLabel);
            targetCell.revalidate();
            targetCell.repaint();

            // Llamar al método collect del recurso
            resource.collect();

            // Sumar valor al equipo correspondiente
            // Determinar a quién sumar:
            // Asumiendo: PlayerOne = Plantas, PlayerTwo = Zombies
            // Si es SOL -> plantas (PlayerOne), si es BRAIN -> zombies (PlayerTwo)
            Player playerOne = poobvszombies.getPlayerOne();
            Player playerTwo = poobvszombies.getPlayerTwo();

            if (Resource.SOL.equals(resource.getType())) {
                // Sumar al equipo de plantas
                playerOne.getTeam().addResource(resource);
                playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
            } else if (Resource.BRAIN.equals(resource.getType())) {
                // Sumar al equipo de zombies
                playerTwo.getTeam().addResource(resource);
                playerTwoBrainsLabel.setText("" + playerTwo.getTeam().getResourceCounterAmount());
            }

            ((Timer) e.getSource()).stop();
        });
        collectTimer.setRepeats(false);
        collectTimer.start();
    }

    // Método para inicializar los temporizadores
    private void initializeTimers(POOBvsZombies poobvsZombies) {
        if ("PlayerVsPlayer".equals(modality)) {
            timerTasks.add(new TimerTask("Planting time 1", Plants.PLANTING_TIME));
            timerTasks.add(new TimerTask("Round time", (int) poobvsZombies.getRoundTime()));
            timerTasks.add(new TimerTask("Planting time 2", Plants.PLANTING_TIME));
            timerTasks.add(new TimerTask("Last round", (int) poobvsZombies.getRoundTime()));

            // Timer para generar recursos cada 10s en PlayerVsPlayer
            Timer resourceTimer = new Timer(10000, e -> {
                String currentPhase = messageLabel.getText();
                if (currentPhase.contains("Planting time")) {
                    // Planting time -> soles
                    spawnResource(new Resource(Resource.SOL));
                } else if (currentPhase.contains("Round") || currentPhase.contains("Last round")) {
                    // Round time/Last round -> cerebros
                    spawnResource(new Resource(Resource.BRAIN));
                }
            });
            resourceTimer.start();

        } else if ("PlayerVsMachine".equals(modality) || "MachineVsMachine".equals(modality)) {
            int matchTimeInSeconds = (int) poobvsZombies.getMatchTime();
            int hordersNumber = poobvsZombies.getHordersNumber();

            // Primero, 20 segundos para colocar plantas
            timerTasks.add(new TimerTask("Planting time", 20));

            // Validar que el tiempo total sea al menos 20 segundos y haya al menos una
            // horda
            if (matchTimeInSeconds < 20 || hordersNumber <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El tiempo total debe ser al menos 20 segundos y debe haber al menos una horda.",
                        "Error de Configuración", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            // Calcular el tiempo restante después de la fase de plantación
            int remainingTime = matchTimeInSeconds;

            // Dividir el tiempo restante entre el número de hordas
            int hordeDuration = remainingTime / hordersNumber;
            int extraSeconds = remainingTime % hordersNumber; // Para segundos sobrantes

            for (int i = 1; i <= hordersNumber; i++) {
                int duration = hordeDuration;
                if (i <= extraSeconds) {
                    duration += 1; // Distribuir segundos sobrantes
                }
                timerTasks.add(new TimerTask("Horda " + i, duration));
            }

            // Timers para generar recursos cada 10s en PlayerVsMachine o MachineVsMachine
            if ("PlayerVsMachine".equals(modality)) {
                // En PlayerVsMachine: Soles cada 10 segundos
                Timer resourceTimer = new Timer(10000, e -> {
                    spawnResource(new Resource(Resource.SOL));
                });
                resourceTimer.start();

            } else if ("MachineVsMachine".equals(modality)) {
                // En MachineVsMachine: Soles y Cerebros cada 10 segundos
                Timer resourceTimer = new Timer(10000, e -> {
                    String currentPhase = messageLabel.getText();
                    if (currentPhase.contains("Planting time")) {
                        spawnResource(new Resource(Resource.SOL));
                    }

                    else if (currentPhase.contains("Horda"))
                        spawnResource(new Resource(Resource.SOL));
                    spawnResource(new Resource(Resource.BRAIN));
                });
                resourceTimer.start();
            }
        }
    }

    // Método para configurar las etiquetas en la interfaz
    private void setupTimerLabels(JPanel panel) {
        int startX = 670;
        int startY = 600;

        // Crear la etiqueta para el mensaje
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setBounds(startX, startY, 200, 30);

        // Crear la etiqueta para el tiempo
        timeLabel = new JLabel();
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setBounds(startX + 120, startY, 200, 30);

        // Agregar las etiquetas al panel
        panel.add(messageLabel);
        panel.add(timeLabel);

        // Ocultar las etiquetas inicialmente
        messageLabel.setVisible(false);
        timeLabel.setVisible(false);
    }

    // Método para iniciar los temporizadores secuencialmente
    private void startSequentialTimers(int index) {
        if (index >= timerTasks.size()) {
            // Todos los temporizadores han finalizado
            // Ocultar las etiquetas
            messageLabel.setVisible(false);
            timeLabel.setVisible(false);
            return;
        }

        TimerTask currentTask = timerTasks.get(index);
        int duration = currentTask.durationInSeconds;

        // Actualizar las etiquetas y hacerlas visibles
        messageLabel.setText(currentTask.message);
        timeLabel.setText(formatTime(duration));
        messageLabel.setVisible(true);
        timeLabel.setVisible(true);

        Timer timer = new Timer(1000, null);
        timer.addActionListener(new ActionListener() {
            int remainingTime = duration;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingTime > 0) {
                    remainingTime--;
                    timeLabel.setText(formatTime(remainingTime));
                } else {
                    timer.stop();
                    // Ocultar las etiquetas
                    messageLabel.setVisible(false);
                    timeLabel.setVisible(false);
                    // Iniciar el siguiente temporizador
                    startSequentialTimers(index + 1);
                }
            }
        });

        timer.setInitialDelay(0);
        timer.start();
    }

    // Método para formatear el tiempo
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    // Method to show the matrix of entities each time an entity is added
    private void showEntityMatrix() {
        StringBuilder matrixString = new StringBuilder();

        // Assuming the grid is 5 rows by 10 columns
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                String entity = poobvszombies.getEntity(row, col);
                matrixString.append(entity != null ? entity : "-");
                if (col < 9) {
                    matrixString.append(" ");
                }
            }
            matrixString.append("\n");
        }

        JOptionPane.showMessageDialog(this, matrixString.toString(), "Entity Matrix", JOptionPane.INFORMATION_MESSAGE);
    }

    private Plant createPlantInstance(String plantName) {
        switch (plantName) {
            case "Sunflower":
                return new Sunflower();
            case "Peashooter":
                return new Peashooter();
            case "WallNut":
                return new WallNut();
            case "PotatoMine":
                return new PotatoMine();
            case "ECIPlant":
                return new ECIPlant();
            default:
                return null;
        }
    }

    private Zombie createZombieInstance(String zombieName) {
        switch (zombieName) {
            case "Basic":
                return new Basic();
            case "Brainstein":
                return new Brainstein();
            case "BucketHead":
                return new Buckethead();
            case "Conehead":
                return new Conehead();
            case "ECIZombie":
                return new ECIZombie();
            default:
                return null;
        }
    }

    // // Método para inicializar el timer de generación de recursos de las entidades
    // private void startResourceGeneration() {
    //     // Cada 20 segundos (20000 ms), se generan recursos de Sunflower, ECIPlant, Brainstein
    //     resourceGenerationTimer = new Timer(20000, e -> {
    //         ArrayList<ArrayList<Entity>> matrix = poobvszombies.getEntitiesMatrix();
    //         for (int r = 0; r < 5; r++) {
    //             for (int c = 0; c < 10; c++) {
    //                 Entity ent = matrix.get(r).get(c);
    //                 if (ent instanceof ResourceGenerator) {
    //                     Resource res = ((ResourceGenerator) ent).generateResource(r);
    //                     if (res != null) {
    //                         // Colocar el recurso en la misma posición que la entidad
    //                         spawnSpecificResource(r, c, res);
    //                     }
    //                 }
    //             }
    //         }

    //         // Aquí también se podría manejar la lógica de ECIPlant para los 3 soles extras, si se guardaron en POOBvsZombies.
    //     });
    //     resourceGenerationTimer.setRepeats(true);
    //     resourceGenerationTimer.start();
    // }

    public void spawnSpecificResource(int row, int col, Resource resource) {
        JPanel targetCell = gridCells[row][col];
        // Determinar la ruta de la imagen basada en el tipo de recurso
        String imagePath;
        if (Resource.SOL.equals(resource.getType())) {
            imagePath = "resources/images/sun.png"; // Imagen regular del sol
        } else if (Resource.BIG_SOL.equals(resource.getType())) {
            imagePath = "resources/images/big-sun.png"; // Imagen para Big Sun
        } else if (Resource.BRAIN.equals(resource.getType())) {
            imagePath = "resources/images/brain.png"; // Imagen del cerebro
        } else if (Resource.THREE_BIG_SOL.equals(resource.getType())){
            imagePath = "resources/images/three-big-sun.png"; // Imagen para THREE_BIG_SOL
        }    
        else {
            imagePath = "resources/images/default.png"; // Imagen por defecto
        }
    
        ImageIcon icon = new ImageIcon(imagePath);
        // Verificar si la imagen existe
        if (icon.getIconWidth() == -1) {
            // Imagen no encontrada, usar una imagen de placeholder
            System.err.println("Imagen no encontrada: " + imagePath);
            imagePath = "resources/images/default.png"; // Ruta alternativa
            icon = new ImageIcon(imagePath);
        }
    
        Image scaledImage;
    
        if (Resource.THREE_BIG_SOL.equals(resource.getType())) {
            scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Reducir tamaño para THREE_BIG_SOL
        } else {
            scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        }
    
        JLabel resourceLabel = new JLabel(new ImageIcon(scaledImage));
        resourceLabel.setHorizontalAlignment(JLabel.CENTER);
        targetCell.add(resourceLabel);
        targetCell.revalidate();
        targetCell.repaint();
        targetCell.setComponentZOrder(resourceLabel, 0);
    
        if (Resource.THREE_BIG_SOL.equals(resource.getType())) {
            // Directamente sumar 150 soles al contador del jugador sin esperar a la recolección
            Player playerOne = poobvszombies.getPlayerOne();
            playerOne.getTeam().addResource(new Resource(Resource.SOL, 150));
            playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
            System.out.println("Se han sumado 150 soles al contador del jugador debido a THREE_BIG_SOL.");
    
            // Iniciar Timer para remover THREE_BIG_SOL después de 3 segundos
            Timer collectTimer = new Timer(3000, (ActionEvent ev) -> {
                targetCell.remove(resourceLabel);
                targetCell.revalidate();
                targetCell.repaint();
                resource.collect();
    
                ((Timer) ev.getSource()).stop();
                System.out.println("THREE_BIG_SOL removido después de 3 segundos.");
            });
            collectTimer.setRepeats(false);
            collectTimer.start();
        } else {
            // Iniciar Timer de recolección automática para SOL, BIG_SOL y BRAIN
            Timer collectTimer = new Timer(3000, (ActionEvent ev) -> {
                // Sumar el valor al jugador correspondiente
                Player playerOne = poobvszombies.getPlayerOne();
                Player playerTwo = poobvszombies.getPlayerTwo();
                if (Resource.SOL.equals(resource.getType()) || Resource.BIG_SOL.equals(resource.getType())) {
                    playerOne.getTeam().addResource(resource);
                    playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
                } else if (Resource.BRAIN.equals(resource.getType())) {
                    playerTwo.getTeam().addResource(resource);
                    playerTwoBrainsLabel.setText("" + playerTwo.getTeam().getResourceCounterAmount());
                }
    
                // Remover el recurso del tablero
                targetCell.remove(resourceLabel);
                targetCell.revalidate();
                targetCell.repaint();
                resource.collect();
    
                ((Timer) ev.getSource()).stop();
                System.out.println("Recurso " + resource.getType() + " removido después de 3 segundos.");
            });
            collectTimer.setRepeats(false);
            collectTimer.start();
        }
    }
    
    
    
    private Timer createEntityTimer(ResourceGenerator generator, int row, int col) {
        return new Timer(20000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Resource res = generator.generateResource(row);
                if (res != null) {
                    spawnSpecificResource(row, col, res);
                    System.out.println("Generando recurso: " + res.getType() + " en (" + row + ", " + col + ")");
    
                    // Manejar condiciones especiales para ECIPlant
                    if (generator instanceof ECIPlant) {
                        ECIPlant eciPlant = (ECIPlant) generator;
                        Player playerOne = poobvszombies.getPlayerOne();
                        System.out.println("Contador de soles antes de verificar: " + playerOne.getTeam().getResourceCounterAmount());
    
                        // Verificar si el contador es <= 0 y si aún no se han generado los soles extras
                        if (playerOne.getTeam().getResourceCounterAmount() <= 0 && !eciPlant.hasGeneratedExtraSuns()) {
                            eciPlant.setExtraSunsGenerated(true);
                            System.out.println("ECIPlant detectó contador de soles <= 0. Generando THREE_BIG_SOL.");
    
                            // Generar THREE_BIG_SOL
                            Resource threeBigSol = new Resource(Resource.THREE_BIG_SOL, Resource.THREE_BIG_VALUE);
                            spawnSpecificResource(row, col, threeBigSol);
    
                            // Sumar 150 soles directamente al contador
                            playerOne.getTeam().addResource(new Resource(Resource.SOL, 150));
                            playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
                            System.out.println("ECIPlant ha sumado 150 soles al contador.");
                        }
                    }
                }
            }
        });
    }
    
    
    


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArrayList<String> selectedPlants = new ArrayList<>(Arrays.asList(
                    "Sunflower",
                    "Peashooter",
                    "WallNut",
                    "PotatoMine",
                    "ECIPlant"));
            ArrayList<String> selectedZombies = new ArrayList<>(Arrays.asList(
                    "Basic",
                    "Conehead",
                    "BucketHead",
                    "ECIZombie",
                    "Brainstein"));

            // GardenMenu frame = new GardenMenu(selectedPlants, selectedZombies,
            // "PlayerVsPlayer");s
            int matchTimer = 1;
            String namePlayerOne = "Player1";
            // ArrayList<String> plants = new ArrayList<>(Arrays.asList(selectedPlants));
            int sunAmount = 50;
            String namePlayerTwo = "Player2";
            int brainAmount = 100;
            // ArrayList<String> zombies = new ArrayList<>(Arrays.asList(selectedZombies));
            POOBvsZombies poobvszombies = new POOBvsZombies(matchTimer, namePlayerOne, selectedPlants, sunAmount,
                    namePlayerTwo, brainAmount, selectedZombies);
            GardenMenu frame = new GardenMenu(poobvszombies);
            frame.setVisible(true);
        });
    }
}
