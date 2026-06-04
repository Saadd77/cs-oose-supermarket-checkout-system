package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.inventory.ManagerNotifier;
import fr.cs.oose.inventory.SupplierNotifier;
import fr.cs.oose.model.Category;
import fr.cs.oose.model.Item;

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