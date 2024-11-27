package presentation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;

public class GardenMenu extends JFrame {
    private String[] selectedPlants;
    private JLabel shovelLabel;
    private Point originalShovelPosition;
    private JPanel[][] gridCells = new JPanel[5][10];

    public GardenMenu(String[] selectedPlants) {
        this.selectedPlants = selectedPlants;
        setTitle("Garden Menu");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel con fondo personalizado
        JPanel panel = new JPanel() {
            Image backgroundImage = new ImageIcon("resources/images/gardenPvsM.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Mostrar las cartas correspondientes a las plantas seleccionadas y hacerlas arrastrables
        addCards(panel);

        // Añadir la pala en la esquina superior derecha
        addShovel(panel);

        // Añadir el GridLayout de 5x10 para las celdas
        addGridLayout(panel);

        // Añadir el botón "Use Shovel" debajo de la pala
        addUseShovelButton(panel);

        add(panel);
    }

    private void addCards(JPanel panel) {
        // Rutas de las cartas correspondientes a las plantas
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
            "resources/images/plants/WallNut/Wall-nut.png",
            "resources/images/plants/PotatoMine/beforePotatoMine.png",
            "resources/images/plants/ECIPlant/ECIPlantAnimated.gif"
        };

        int x = 75; // Posición inicial en X
        int y = -25; // Posición inicial en Y
        for (int i = 0; i < selectedPlants.length; i++) {
            if (selectedPlants[i] != null) { // Solo si es una planta válida
                // Mostrar la carta
                ImageIcon icon = new ImageIcon(plantCards[getPlantIndex(selectedPlants[i])]);
                JLabel cardLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                cardLabel.setBounds(x, y, 100, 150);
                panel.add(cardLabel);

                // Añadir funcionalidad de arrastrar
                String dragImagePath = plantDragImages[getPlantIndex(selectedPlants[i])];
                cardLabel.setTransferHandler(new TransferHandler("icon") {
                    @Override
                    protected Transferable createTransferable(JComponent c) {
                        ImageIcon icon = new ImageIcon(dragImagePath);
                        return new ImageSelection(icon);
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

                x += 70; // Mover la posición en X para la siguiente carta
            }
        }
    }

    private int getPlantIndex(String plantPath) {
        // Mapa de las plantas a sus índices
        if (plantPath.contains("Sunflower")) return 0;
        if (plantPath.contains("Peashooter")) return 1;
        if (plantPath.contains("WallNut")) return 2;
        if (plantPath.contains("PotatoMine")) return 3;
        if (plantPath.contains("ECIPlant")) return 4;
        return -1; // No encontrado
    }

    private void addShovel(JPanel panel) {
        // Ruta de la pala
        String shovelImagePath = "resources/images/shovel.png";

        // Cargar la pala y escalarla
        ImageIcon shovelIcon = new ImageIcon(shovelImagePath);
        Image scaledShovelImage = shovelIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);

        // Crear un JLabel para mostrar la pala
        shovelLabel = new JLabel(new ImageIcon(scaledShovelImage));
        shovelLabel.setBounds(520, 28, 50, 50); // Colocar en la esquina superior derecha
        originalShovelPosition = shovelLabel.getLocation();

        panel.add(shovelLabel);
    }

    private void addGridLayout(JPanel panel) {
        // Crear un panel con GridLayout de 5x10
        JPanel gridPanel = new JPanel(new GridLayout(5, 10, 5, 5)); // 5 filas, 10 columnas, espacio de 5px
        gridPanel.setBounds(40, 100, 800, 530); // Ajustar posición y tamaño
        gridPanel.setOpaque(false); // Transparente para que el fondo sea visible

        // Ruta de la podadora
        String lawnMowerImagePath = "resources/images/Lawnmower.jpg";

        // Añadir celdas al grid con la podadora en la primera columna
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                JPanel cellPanel = new JPanel(new BorderLayout());
                cellPanel.setPreferredSize(new Dimension(80, 80));
                cellPanel.setOpaque(false);
                cellPanel.setBorder(new LineBorder(Color.BLACK, 1)); // Añadir borde negro a cada celda

                if (col == 0) {
                    // Añadir la podadora a la primera columna de cada fila
                    ImageIcon mowerIcon = new ImageIcon(lawnMowerImagePath);
                    Image scaledMowerImage = mowerIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    JLabel mowerLabel = new JLabel(new ImageIcon(scaledMowerImage));
                    cellPanel.add(mowerLabel, BorderLayout.CENTER);
                } else if (col > 0 && col < 9) {
                    // Añadir funcionalidad para permitir arrastrar y soltar en las celdas del GridLayout
                    cellPanel.setTransferHandler(new TransferHandler("icon") {
                        @Override
                        public boolean canImport(TransferSupport support) {
                            // Validar si la celda ya tiene una planta
                            JPanel targetPanel = (JPanel) support.getComponent();
                            if (targetPanel.getComponentCount() > 0) {
                                return false; // Ya hay una planta en esta celda
                            }
                            return support.isDataFlavorSupported(DataFlavor.imageFlavor);
                        }

                        @Override
                        public boolean importData(TransferSupport support) {
                            if (!canImport(support)) {
                                return false;
                            }

                            try {
                                Image image = (Image) support.getTransferable().getTransferData(DataFlavor.imageFlavor);
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

                gridCells[row][col] = cellPanel; // Guardar la referencia de la celda
                gridPanel.add(cellPanel);
            }
        }

        panel.add(gridPanel);
    }

    private void addUseShovelButton(JPanel panel) {
        JButton useShovelButton = new JButton("Use Shovel");
        useShovelButton.setBounds(580, 28, 100, 50);
        useShovelButton.addActionListener(e -> {
            // Mostrar el JOptionPane para ingresar fila y columna
            JTextField rowField = new JTextField(2);
            JTextField colField = new JTextField(2);
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Row (0-4):"));
            inputPanel.add(rowField);
            inputPanel.add(Box.createHorizontalStrut(15)); // Espacio entre campos
            inputPanel.add(new JLabel("Column (1-9):"));
            inputPanel.add(colField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Enter Row and Column to Remove Plant", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int row = Integer.parseInt(rowField.getText());
                    int col = Integer.parseInt(colField.getText());

                    // Validar los límites y condiciones
                    if (row < 0 || row > 4 || col < 1 || col > 9) {
                        JOptionPane.showMessageDialog(this, "Invalid row or column. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (col == 0) {
                        JOptionPane.showMessageDialog(this, "Cannot remove lawnmower.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JPanel targetPanel = gridCells[row][col];
                        if (targetPanel.getComponentCount() == 0) {
                            JOptionPane.showMessageDialog(this, "No plant to remove in the selected cell.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Remover la planta
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

    // Clase auxiliar para manejar el objeto Transferable de tipo imagen
    private class ImageSelection implements Transferable {
        private ImageIcon image;

        public ImageSelection(ImageIcon image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image.getImage();
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
            GardenMenu frame = new GardenMenu(selectedPlants);
            frame.setVisible(true);
        });
    }
}
