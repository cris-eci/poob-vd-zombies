package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.border.LineBorder;
import javax.swing.Timer;

import domain.POOBvsZombies;
import domain.Player;
import domain.Plants;
import domain.Zombies;
import domain.Team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




/**
 * Clase GardenMenu que representa la interfaz de juego donde se colocan las plantas y zombies en el tablero.
 * Integra la lógica de dominio a través de la instancia de POOBvsZombies y la lista de jugadores.
 */
public class GardenMenu extends JFrame implements POOBvsZombies.GameListener {
    private String[] selectedPlants;
    private String[] selectedZombies;
    private String state; // "PlayerVsPlayer" o "PlayerVsMachine" o "MachineVsMachine"
    private JLabel shovelLabel;
    private Point originalShovelPosition;
    private JPanel[][] gridCells = new JPanel[5][10];
    private List<JLabel> movingZombies = new ArrayList<>();

    // Referencia al objeto de juego y a los jugadores
    private POOBvsZombies game;
    private List<Player> players;
    

    // Componentes de la interfaz para mostrar recursos y puntajes
    private JLabel timerLabel;
    private JLabel playerOneNameLabel, playerTwoNameLabel;
    private JLabel playerOneSunsLabel, playerTwoBrainsLabel;
    private JLabel playerOneScoreLabel, playerTwoScoreLabel;
    private JLabel hordeLabel;


    // Sistema de puntaje
    private double playerOneScore = 0;
    private double playerTwoScore = 0;

    // Declarar a nivel de clase
    private boolean shovelSelected = false;

    
    public GardenMenu(String[] selectedPlants, String[] selectedZombies, String state, POOBvsZombies game, List<Player> players) {
        // Validación de la lista de jugadores
    
        
        this.selectedPlants = selectedPlants;
        this.selectedZombies = selectedZombies;
        this.state = state;
        this.game = game;
        this.players = players;

        setTitle("Garden Menu");
        setSize(900, 700); // Aumentado el tamaño para mejor visibilidad
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
        addPlantCards(panel);

        // Add the shovel at the top right corner
        addShovel(panel);

        // Add the 5x10 GridLayout for cells
        addGridLayout(panel);

        // Add the "Use Shovel" button below the shovel
        addUseShovelButton(panel);

        // Add icons for returning to menus, saving, and exporting
        addTopRightButtons(panel);

        // Agregar etiquetas para mostrar el tiempo restante
        addTimerLabel(panel);

        // Agregar etiquetas para mostrar los nombres de los jugadores y sus recursos iniciales
        addPlayerInfo(panel);

        // Agregar etiquetas para mostrar los puntajes
        addScoreLabels(panel);

        if ("PlayerVsPlayer".equals(state)) {
            addPlayerInfo(panel);
            addScoreLabels(panel);
            addBrainIcon(panel);
            addZombieCards(panel);
            addZombieTable(panel);
        } else if ("PlayerVsMachine".equals(state)) {
            addPlayerInfo(panel); // Solo mostrar información del jugador
            addHordeLabel(panel);
        } else if ("MachineVsMachine".equals(state)) {
            addHordeLabel(panel);
            addBrainIcon(panel);
            addZombieCards(panel);
            addZombieTable(panel);
        }

        add(panel);
        startZombieMovement();
        if (game != null) {
            game.setGameListener(this);
        }
    }


    /**
     * Método para agregar las tarjetas de plantas seleccionadas.
     */
    private void addPlantCards(JPanel panel) {
        // Rutas de las tarjetas correspondientes a las plantas seleccionadas
        String[] plantCards = {
            "resources/images/cards/Plants/card_sunflower.png",
            "resources/images/cards/Plants/card_peashooter.png",
            "resources/images/cards/Plants/card_wallnut.png",
            "resources/images/cards/Plants/card_potatomine.png",
            "resources/images/cards/Plants/card_ECIPlant.png"
        };

        // Rutas de los GIFs o PNGs de las plantas para arrastrar
        String[] plantDragImages = {
            "resources/images/plants/Sunflower/sunflowerAnimated.gif",
            "resources/images/plants/Peashooter/peashooterAnimated.gif",
            "resources/images/plants/WallNut/wall-nutAnimated.gif",
            "resources/images/plants/PotatoMine/before-potato-mineAnimated.gif",
            "resources/images/plants/ECIPlant/ECIPlantAnimated.gif"
        };

        int x = 75; // Posición X inicial
        int y = -25; // Posición Y inicial
        for (int i = 0; i < selectedPlants.length; i++) {
            if (selectedPlants[i] != null && !selectedPlants[i].isEmpty()) { // Solo si es una planta válida
                int plantIndex = getPlantIndex(selectedPlants[i]);
                if (plantIndex != -1) {
                    // Mostrar la tarjeta de la planta
                    ImageIcon icon = new ImageIcon(plantCards[plantIndex]);
                    JLabel cardLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                    cardLabel.setBounds(x, y, 100, 150);
                    panel.add(cardLabel);

                    // Hacer la tarjeta arrastrable
                    String dragImagePath = plantDragImages[plantIndex];
                    String plantName = selectedPlants[i];
                    cardLabel.setTransferHandler(new TransferHandler("icon") {
                        @Override
                        protected Transferable createTransferable(JComponent c) {
                            ImageIcon icon = new ImageIcon(dragImagePath);
                            return new ImageTransferable(icon.getImage(), "plant",plantName); // especificar tipo y nombre
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

                    x += 70; // Mover posición X para la siguiente tarjeta
                }
            }
        }
    }

    /**
     * Método para mapear la ruta de la planta a su índice correspondiente.
     */
    private int getPlantIndex(String plantName) {
        // Mapea las plantas a sus índices
        if (plantName.contains("Sunflower")) return 0;
        if (plantName.contains("Peashooter")) return 1;
        if (plantName.contains("WallNut")) return 2;
        if (plantName.contains("PotatoMine")) return 3;
        if (plantName.contains("ECIPlant")) return 4;
        return -1; // No encontrado
    }

    /**
     * Método para agregar la pala en la interfaz.
     */
    private void addShovel(JPanel panel) {
        // Ruta de la imagen de la pala
        String shovelImagePath = "resources/images/shovel.png";

        // Cargar y escalar la imagen de la pala
        ImageIcon shovelIcon = new ImageIcon(shovelImagePath);
        Image scaledShovelImage = shovelIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);

        // Crear un JLabel para mostrar la pala
        shovelLabel = new JLabel(new ImageIcon(scaledShovelImage));
        shovelLabel.setBounds(520, 28, 50, 50); // Colocar en la esquina superior derecha
        originalShovelPosition = shovelLabel.getLocation();

        shovelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shovelSelected = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambiar el cursor
                // JOptionPane.showMessageDialog(null, "Pala seleccionada. Haz clic en una planta para removerla.");
            }
        });
        

        panel.add(shovelLabel);
    }

    /**
     * Método para agregar el tablero de 5x10 con celdas.
     */
    private void addGridLayout(JPanel panel) {
        // Crear un panel con un GridLayout de 5 filas x 10 columnas
        JPanel gridPanel = new JPanel(new GridLayout(5, 10, 5, 5)); // 5 filas, 10 columnas, 5px de espacio
        gridPanel.setBounds(40, 80, 800, 500); // Ajustar posición y tamaño
        gridPanel.setOpaque(false); // Transparente para ver el fondo

        // Ruta de la imagen de la podadora
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
                                ImageTransferable transferable = (ImageTransferable) support.getTransferable()
                                        .getTransferData(ImageTransferable.IMAGE_FLAVOR);
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

                    
                    // Añadir MouseListener a la celda
                        cellPanel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                if (shovelSelected) {
                                    if (cellPanel.getComponentCount() > 0) {
                                        // Remover la planta
                                        cellPanel.removeAll();
                                        cellPanel.revalidate();
                                        cellPanel.repaint();
                                        shovelSelected = false;
                                        setCursor(Cursor.getDefaultCursor()); // Restablecer el cursor
                                    } else {
                                        JOptionPane.showMessageDialog(null, "No hay planta en esta celda.");
                                    }
                                }
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
                                    String plantName = transferable.getName(); // Obtener el nombre de la planta
                                    JLabel label = new JLabel(new ImageIcon(image));
                                    label.setHorizontalAlignment(JLabel.CENTER);
                                    label.putClientProperty("name", plantName); // Establecer la propiedad "name"
                                    JPanel targetPanel = (JPanel) support.getComponent();
                                    targetPanel.add(label);
                                    targetPanel.revalidate();
                                    targetPanel.repaint();

                                    // Actualizar los recursos (soles) al colocar una planta
                                    updateSuns(-getPlantCost(plantName));

                                    return true;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                return false;
                            }

                        });
                    }
                }

                gridCells[row][col] = cellPanel; // Guardar la referencia de la celda
                gridPanel.add(cellPanel);
            }
        }

        panel.add(gridPanel);
    }

    /**
     * Método para agregar el botón "Use Shovel".
     */
    private void addUseShovelButton(JPanel panel) {
        JButton useShovelButton = new JButton("Use Shovel");
        useShovelButton.setBounds(580, 55, 100, 30); // Ajustar posición y tamaño
        // useShovelButton.setBackground(new Color(240, 162, 198));
        // useShovelButton.setForeground(Color.WHITE);
        // useShovelButton.setFocusPainted(false);
        // useShovelButton.setBorder(new LineBorder(Color.BLACK));

        useShovelButton.addActionListener(e -> {
            // Mostrar un diálogo para ingresar la fila y columna de la planta a remover
            JTextField rowField = new JTextField(2);
            JTextField colField = new JTextField(2);
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Row (0-4):"));
            inputPanel.add(rowField);
            inputPanel.add(Box.createHorizontalStrut(15)); // Espacio entre campos
            inputPanel.add(new JLabel("Column (1-8):"));
            inputPanel.add(colField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Enter Row and Column to Remove Plant", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int row = Integer.parseInt(rowField.getText());
                    int col = Integer.parseInt(colField.getText());

                    // Validar límites y condiciones
                    if (row < 0 || row > 4 || col < 1 || col > 8) {
                        JOptionPane.showMessageDialog(this, "Invalid row or column. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JPanel targetPanel = gridCells[row][col];
                        if (targetPanel.getComponentCount() == 0) {
                            JOptionPane.showMessageDialog(this, "No plant to remove in the selected cell.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Remover la planta
                            targetPanel.removeAll();
                            targetPanel.revalidate();
                            targetPanel.repaint();

                            // No se recuperan los soles invertidos al remover una planta
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numeric values for row and column.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(useShovelButton);
    }

    /**
     * Método para agregar las etiquetas que muestran el tiempo restante.
     */
    private void addTimerLabel(JPanel panel) {
        timerLabel = new JLabel("");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setBounds(670, 600, 200, 30); // Ajustar posición y tamaño
        panel.add(timerLabel);
    }

    /**
     * Método para agregar las etiquetas que muestran información de los jugadores.
     */
    private void addPlayerInfo(JPanel panel) {
        if (("PlayerVsPlayer".equals(state) || "PlayerVsMachine".equals(state)) || "MachineVsMachine".equals(state) && players != null && !players.isEmpty() ) {
            Player playerOne = players.get(0);
    
            // Etiqueta para el nombre del Jugador 1
            playerOneNameLabel = new JLabel("" + playerOne.getName());
            playerOneNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneNameLabel.setForeground(Color.YELLOW);
            playerOneNameLabel.setBounds(480, 595, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneNameLabel);
    
            // Etiqueta para los soles iniciales del Jugador 1
            playerOneSunsLabel = new JLabel(""+playerOne.getTeam().getResourceCounterAmount());
            playerOneSunsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneSunsLabel.setForeground(Color.ORANGE);
            playerOneSunsLabel.setBounds(30, 60, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneSunsLabel);
    
            if ("PlayerVsPlayer".equals(state) || "MachineVsMachine".equals(state) && players.size() >= 2) {
                Player playerTwo = players.get(1);
    
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
        }
    }
    
    

    /**
     * Método para agregar las etiquetas que muestran los puntajes de los jugadores.
     */
    private void addScoreLabels(JPanel panel) {
        // Etiqueta para el puntaje del Jugador 1
        if ("PlayerVsPlayer".equals(state) || "PlayerVsMachine".equals(state)){
            playerOneScoreLabel = new JLabel("Score: 0");
            playerOneScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneScoreLabel.setForeground(Color.YELLOW);
            playerOneScoreLabel.setBounds(560, 595, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneScoreLabel);
        

            if("PlayerVsPlayer".equals(state)){
                // Etiqueta para el puntaje del Jugador 2
                playerTwoScoreLabel = new JLabel("Score: 0");
                playerTwoScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoScoreLabel.setForeground(Color.RED);
                playerTwoScoreLabel.setBounds(560, 625, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoScoreLabel);
            }
        }
    }

    /**
     * Método para agregar las tarjetas de zombies seleccionados.
     */
    private void addZombieCards(JPanel panel) {
        if ("PlayerVsPlayer".equals(state) || "MachineVsMachine".equals(state) && selectedZombies != null) {
            // Rutas de las tarjetas correspondientes a los zombies seleccionados
            String[] zombieCards = {
                "resources/images/cards/Zombies/card_basic_zombie.png",
                "resources/images/cards/Zombies/card_conehead_zombie.png",
                "resources/images/cards/Zombies/card_buckethead_zombie.png",
                "resources/images/cards/Zombies/card_brainstein.png",
                "resources/images/cards/Zombies/card_ECIZombie.png"
            };

            // Rutas de los GIFs o PNGs de los zombies para arrastrar
            String[] zombieDragImages = {
                "resources/images/zombies/Basic/BasicDinamic.gif",
                "resources/images/zombies/Conehead/ConeheadAnimated.gif",
                "resources/images/zombies/BucketHead/BucketheadAnimated.gif",
                "resources/images/zombies/Brainstein/brainsteinAnimated.gif",
                "resources/images/zombies/ECIZombie/ECIZombieAnimated.gif"
            };

            int x = 85; // Posición X inicial
            int y = 625; // Posición Y inicial
            for (int i = 0; i < selectedZombies.length; i++) {
                if (selectedZombies[i] != null && !selectedZombies[i].isEmpty()) {
                    int zombieIndex = getZombieIndex(selectedZombies[i]);
                    if (zombieIndex != -1) {
                        // Mostrar la tarjeta del zombie
                        ImageIcon icon = new ImageIcon(zombieCards[zombieIndex]);
                        JLabel cardLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                        cardLabel.setBounds(x, y - 85, 100, 150); // Ajustar posición y tamaño
                        panel.add(cardLabel);

                        // Hacer la tarjeta arrastrable
                        String dragImagePath = zombieDragImages[zombieIndex];
                        String zombieName = selectedZombies[i];
                        cardLabel.setTransferHandler(new TransferHandler("icon") {
                            @Override
                            protected Transferable createTransferable(JComponent c) {
                                ImageIcon icon = new ImageIcon(dragImagePath);
                                return new ImageTransferable(icon.getImage(), "zombie",zombieName); 
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

                        x += 70; // Mover posición X para la siguiente tarjeta
                    }
                
                }
            }
        }
    }
    /**
     * Método para mapear la ruta del zombie a su índice correspondiente.
     */
    private int getZombieIndex(String zombieName) {
        // Mapea los zombies a sus índices
        if (zombieName.contains("Basic")) return 0;
        if (zombieName.contains("Conehead")) return 1;
        if (zombieName.contains("BucketHead")) return 2;
        if (zombieName.contains("Brainstein")) return 3;
        if (zombieName.contains("ECIZombie")) return 4;
        return -1; // No encontrado
    }

    /**
     * Método para agregar la tabla de zombies.
     */
    private void addZombieTable(JPanel panel) {
            if ("PlayerVsPlayer".equals(state) || "MachineVsMachine".equals(state)) {
                // Ruta de la imagen de la tabla de zombies
                String zombieTableImagePath = "resources/images/zombie-table.png";

                // Cargar y escalar la imagen
                ImageIcon zombieTableIcon = new ImageIcon(zombieTableImagePath);
                Image scaledZombieTableImage = zombieTableIcon.getImage().getScaledInstance(460, 80, Image.SCALE_SMOOTH);

                // Crear un JLabel para mostrar la tabla de zombies
                JLabel zombieTableLabel = new JLabel(new ImageIcon(scaledZombieTableImage));
                zombieTableLabel.setBounds(10, 575, 460, 80); // Ajustar posición y tamaño

                panel.add(zombieTableLabel);
            }
    }

    /**
     * Método para agregar el icono de cerebro y su contador.
     */
    private void addBrainIcon(JPanel panel) {
        if ("PlayerVsPlayer".equals(state) || "MachineVsMachine".equals(state)) {
            // Ruta de la imagen del cerebro
            String brainImagePath = "resources/images/brain.png";

            // Cargar y escalar la imagen
            ImageIcon brainIcon = new ImageIcon(brainImagePath);
            Image scaledBrainImage = brainIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

            // Crear un JLabel para mostrar el cerebro
            JLabel brainLabel = new JLabel(new ImageIcon(scaledBrainImage));
            brainLabel.setBounds(50, 585, 40, 40); // Ajustar posición y tamaño
            panel.add(brainLabel);
        }
    }

    /**
     * Método para agregar los botones de la esquina superior derecha.
     */
    private void addTopRightButtons(JPanel panel) {
        String[] buttonImagePaths = {
            "resources/images/buttons/export-icon.png",     // Exportar
            "resources/images/buttons/save-icon.png",       // Guardar
            "resources/images/buttons/return-icon.png",     // Retornar
            "resources/images/buttons/home-icon.png"        // Volver al menú principal
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

            // Agregar eventos de acción a los botones
            if (imagePath.contains("export-icon")) {
                button.addActionListener(e -> {
                    // Implementar funcionalidad para exportar el estado del juego
                    JOptionPane.showMessageDialog(this, "Funcionalidad de exportar aún no implementada.", "Exportar", JOptionPane.INFORMATION_MESSAGE);
                });
            }

            if (imagePath.contains("save-icon")) {
                button.addActionListener(e -> {
                    // Implementar funcionalidad para guardar el estado del juego
                    JOptionPane.showMessageDialog(this, "Funcionalidad de guardar aún no implementada.", "Guardar", JOptionPane.INFORMATION_MESSAGE);
                });
            }

            if (imagePath.contains("return-icon")) {
                button.addActionListener(e -> {
                    // Retornar al menú anterior según el estado del juego
                    dispose(); // Cerrar la ventana actual
                    if ("PlayerVsMachine".equals(state)) {
                        PlayerVSMachine pvmMenu = new PlayerVSMachine();
                        pvmMenu.setVisible(true);
                    } else if ("PlayerVsPlayer".equals(state)) {
                        PlayerVsPlayer pvpMenu = new PlayerVsPlayer();
                        pvpMenu.setVisible(true);
                    } else {
                        MachineVSMachine mvsmMenu = new MachineVSMachine();
                        mvsmMenu.setVisible(true);
                    }
                });
            }

            if (imagePath.contains("home-icon")) {
                button.addActionListener(e -> {
                    // Volver al menú principal
                    dispose(); // Cerrar la ventana actual
                    POOBvsZombiesGUI mainMenu = new POOBvsZombiesGUI();
                    mainMenu.setVisible(true);
                });
            }

            panel.add(button);
            x += 60; // Ajustar posición X para el siguiente botón
        }
            // Forzar la actualización del panel
            panel.revalidate();
            panel.repaint();
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
    
    /**
     * Método para actualizar la cantidad de soles en la interfaz.
     * @param delta Valor a sumar o restar a los soles.
     */
    private void updateSuns(int delta) {
        // Asumimos que el Jugador 1 es el de las plantas
        Player playerOne = players.get(0);
        int currentSuns = playerOne.getTeam().getResourceCounterAmount();
        currentSuns += delta;
        playerOne.getTeam().setResourceCounter(currentSuns);
        playerOneSunsLabel.setText(""+ currentSuns);
    }

    /**
     * Método para actualizar la cantidad de cerebros en la interfaz.
     * @param delta Valor a sumar o restar a los cerebros.
     */
    private void updateBrains(int delta) {
        // Asumimos que el Jugador 2 es el de los zombies
        if (players.size() >= 2) {
            Player playerTwo = players.get(1);
            int currentBrains = playerTwo.getTeam().getResourceCounterAmount();
            currentBrains += delta;
            playerTwo.getTeam().setResourceCounter(currentBrains);
            playerTwoBrainsLabel.setText("" + currentBrains);
        }
    }

    /**
     * Método para calcular el puntaje de los jugadores al finalizar el juego.
     */
    private void calculateScores() {
        if (players != null && !players.isEmpty()) {
            Player playerOne = players.get(0);
    
            // Calcular puntaje para las plantas (Jugador 1)
            int remainingSuns = playerOne.getTeam().getResourceCounterAmount();
            double plantsValue = getPlantsValue();
            playerOneScore = remainingSuns + (plantsValue * 1.5);
            playerOneScoreLabel.setText("Score Player 1: " + playerOneScore);
    
            if ("PlayerVsPlayer".equals(state) && players.size() >= 2) {
                Player playerTwo = players.get(1);
    
                // Calcular puntaje para los zombies (Jugador 2)
                int remainingBrains = playerTwo.getTeam().getResourceCounterAmount();
                double zombiesValue = getZombiesValue();
                playerTwoScore = remainingBrains + zombiesValue;
                playerTwoScoreLabel.setText("Score Player 2: " + playerTwoScore);
            }
        }
    }
    
    

        /**
     * Método para obtener el valor total de las plantas en el tablero.
     * @return Suma de los costos de las plantas en soles.
     */
    private double getPlantsValue() {
        double total = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 1; col <= 8; col++) {
                JPanel cell = gridCells[row][col];
                if (cell.getComponentCount() > 0) {
                    JLabel plantLabel = (JLabel) cell.getComponent(0);
                    String plantName = (String) plantLabel.getClientProperty("name");
                    total += getPlantCost(plantName);
                }
            }
        }
        return total;
    }

    /**
     * Método para obtener el valor total de los zombies en el tablero.
     * @return Suma de los costos de los zombies en cerebros.
     */
    private double getZombiesValue() {
        double total = 0;
        for (int row = 0; row < 5; row++) {
            int col = 9; // Solo la última columna para zombies
            JPanel cell = gridCells[row][col];
            if (cell.getComponentCount() > 0) {
                JLabel zombieLabel = (JLabel) cell.getComponent(0);
                String zombieName = (String) zombieLabel.getClientProperty("name");
                total += getZombieCost(zombieName);
            }
        }
        return total;
    }


    /**
     * Método para obtener el costo de una planta basado en su nombre.
     * @param plantName Nombre de la planta.
     * @return Costo en soles de la planta.
     */
    private int getPlantCost(String plantName) {
        if (plantName.contains("Sunflower")) return 50;
        if (plantName.contains("Peashooter")) return 100;
        if (plantName.contains("WallNut")) return 50;
        if (plantName.contains("PotatoMine")) return 25;
        if (plantName.contains("ECIPlant")) return 75;
        return 0; // Por defecto
    }

    /**
     * Método para obtener el costo de un zombie basado en su nombre.
     * @param zombieName Nombre del zombie.
     * @return Costo en cerebros del zombie.
     */
    private int getZombieCost(String zombieName) {
        if (zombieName.contains("Basic")) return 100;
        if (zombieName.contains("Conehead")) return 150;
        if (zombieName.contains("BucketHead")) return 200;
        if (zombieName.contains("Brainstein")) return 50;
        if (zombieName.contains("ECIZombie")) return 250;
        return 0; // Por defecto
    }

    private void addHordeLabel(JPanel panel) {
        hordeLabel = new JLabel(""); // Etiqueta inicialmente vacía
        hordeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        hordeLabel.setForeground(Color.BLACK);
        hordeLabel.setBounds(570, 630, 200, 30); // Ajusta la posición según sea necesario
        panel.add(hordeLabel);
    }
    
    
    
    @Override
public void onTimeUpdate(int timeRemaining) {
    SwingUtilities.invokeLater(() -> {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText("Remaining time: " + minutes + "m " + seconds + "s");
    });
}

@Override
public void onInitialSetupTimeUpdate(int timeRemaining) {
    SwingUtilities.invokeLater(() -> {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText("Planting Time: " + minutes + "m " + seconds + "s");
    });
}

@Override
public void onRoundStart(int roundNumber) {
    SwingUtilities.invokeLater(() -> {
        JOptionPane.showMessageDialog(this, "Starts the round " + roundNumber + ".");
    });
}

@Override
public void onHordeChange(int hordeNumber) {
    SwingUtilities.invokeLater(() -> {
        hordeLabel.setText("Horda " + hordeNumber);
    });
}


@Override
public void onGameEnd(String result) {
    SwingUtilities.invokeLater(() -> {
        timerLabel.setText("Tiempo Restante: 0m 0s");
        calculateScores(); // Calcular los puntajes al final del juego
        JOptionPane.showMessageDialog(this, "Resultado del Juego: " + result);

        // Determinar y mostrar el ganador basado en los puntajes
        if ("PlayerVsPlayer".equals(state)) {
            if (playerOneScore > playerTwoScore) {
                JOptionPane.showMessageDialog(this, "¡" + players.get(0).getName() + " ha ganado!", "Ganador", JOptionPane.INFORMATION_MESSAGE);
            } else if (playerTwoScore > playerOneScore) {
                JOptionPane.showMessageDialog(this, "¡" + players.get(1).getName() + " ha ganado!", "Ganador", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡La partida ha terminado en empate!", "Empate", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if ("PlayerVsMachine".equals(state)) {
            // Mostrar mensaje de victoria o derrota
            JOptionPane.showMessageDialog(this, result, "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Para MachineVsMachine, mostrar resultados basados en puntajes
            if (playerOneScore > playerTwoScore) {
                JOptionPane.showMessageDialog(this, "¡Las plantas han ganado!", "Ganador", JOptionPane.INFORMATION_MESSAGE);
            } else if (playerTwoScore > playerOneScore) {
                JOptionPane.showMessageDialog(this, "¡Los zombies han ganado!", "Ganador", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡La partida ha terminado en empate!", "Empate", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // Cerrar la ventana y regresar al menú principal
        dispose();
        POOBvsZombiesGUI mainMenu = new POOBvsZombiesGUI();
        mainMenu.setVisible(true);
    });
}


    private static class ImageTransferable implements Transferable {
        public static final DataFlavor IMAGE_FLAVOR = new DataFlavor(ImageTransferable.class, "ImageTransferable");
        private Image image;
        private String type; // "plant" or "zombie"
        private String name; // Nombre de la planta o zombie
    
        public ImageTransferable(Image image, String type, String name) {
            this.image = image;
            this.type = type;
            this.name = name;
        }
    
        public String getType() {
            return type;
        }
    
        public String getName() {
            return name;
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
    

    
}
