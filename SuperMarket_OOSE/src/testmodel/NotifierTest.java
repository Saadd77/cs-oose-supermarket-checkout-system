package testmodel;

import inventory.ManagerNotifier;
import inventory.SupplierNotifier;
import model.Category;
import model.Item;
import org.junit.Test;

public class NotifierTest {

    @Test
    public void managerNotifierCanReceiveLowStockUpdate() {
        Category category = new Category("meat");
        Item item = new Item("kafta", category, 10.0, 0.5, 2);

        ManagerNotifier notifier = new ManagerNotifier();

        notifier.update(item);
    }

    @Test
    public void supplierNotifierCanReceiveLowStockUpdate() {
        Category category = new Category("dairy");
        Item item = new Item("labneh", category, 3.0, 0.5, 2);

        SupplierNotifier notifier = new SupplierNotifier();

        notifier.update(item);
    }
}