package inventory;

import model.Item;

public class ManagerNotifier implements StockObserver {
    @Override
    public void update(Item item) {
        System.out.println(
                "LOW STOCK ALERT for manager: " + item.getName()
                        + " has only " + item.getStock() + " units left."
        );
    }
}