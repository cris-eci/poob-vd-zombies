package presentation;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class EntityTransferable implements Transferable {
    public static final DataFlavor ENTITY_FLAVOR = new DataFlavor(EntityData.class, "EntityData");
    private static final DataFlavor[] SUPPORTED_FLAVORS = { ENTITY_FLAVOR };

    private EntityData data;

    public EntityTransferable(EntityData data) {
        this.data = data;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(ENTITY_FLAVOR);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return data;
    }
}
