package inventory;

import model.Item;

public class SupplierNotifier implements StockObserver {
    @Override
    public void update(Item item) {
        System.out.println(
                "LOW STOCK ALERT for supplier: please restock " + item.getName()
                        + ". Current stock: " + item.getStock()
        );
    }
}