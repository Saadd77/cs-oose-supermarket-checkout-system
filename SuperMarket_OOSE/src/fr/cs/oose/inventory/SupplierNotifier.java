package fr.cs.oose.inventory;

import fr.cs.oose.model.Item;

public class SupplierNotifier implements StockObserver {
    @Override
    public void update(Item item) {
        System.out.println(
                "LOW STOCK ALERT for supplier: please restock " + item.getName()
                        + ". Current stock: " + item.getStock()
        );
    }
}