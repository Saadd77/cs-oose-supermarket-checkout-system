package delivery;

import model.Cart;
import model.CartLine;

public class DeliveryService {

    public double computeBaseDeliveryFee(Cart cart, DeliveryRequest request) {
        double totalWeight = computeTotalWeight(cart);

        if (totalWeight > 50.0) {
            throw new IllegalStateException("delivery refused: total weight exceeds 50 kg");
        }

        if (totalWeight < 10.0 && request.getDistanceKm() <= 30.0) {
            return 15.0;
        }

        if (totalWeight >= 10.0 && totalWeight <= 50.0) {
            return 15.0 + 0.05 * cart.computeTotal();
        }

        return 20.0;
    }

    public double computeTotalWeight(Cart cart) {
        double totalWeight = 0.0;

        for (CartLine line : cart.getLines()) {
            totalWeight += line.getItem().getWeight() * line.getQuantity();
        }

        return totalWeight;
    }
}