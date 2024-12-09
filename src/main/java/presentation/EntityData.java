package presentation;

import java.awt.Image;

public class EntityData {
    private String type;
    private String name;
    private Image image;

    public EntityData(String type, String name, Image image) {
        this.type = type;
        this.name = name;
        this.image = image;
    }

    public String getType() { return type; }
    public String getName() { return name; }
    public Image getImage() { return image; }
}

// public class EntityTransferable implements Transferable {
//     public static final DataFlavor ENTITY_FLAVOR = new DataFlavor(EntityData.class, "EntityData");
//     private static final DataFlavor[] SUPPORTED_FLAVORS = { ENTITY_FLAVOR };

//     private EntityData data;s

//     public EntityTransferable(EntityData data) {
//         this.data = data;
//     }

//     @Override
//     public DataFlavor[] getTransferDataFlavors() {
//         return SUPPORTED_FLAVORS;
//     }

//     @Override
//     public boolean isDataFlavorSupported(DataFlavor flavor) {
//         return flavor.equals(ENTITY_FLAVOR);
//     }

//     @Override
//     public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
//         if (!isDataFlavorSupported(flavor)) {
//             throw new UnsupportedFlavorException(flavor);
//         }
//         return data;
//     }
// }
