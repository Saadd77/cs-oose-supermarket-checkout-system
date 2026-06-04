package delivery;

import model.Cart;
import model.CartLine;

import java.util.HashMap;
import java.util.Map;

public class DeliveryService {
    private final Map<String, DeliverySlot> slots = new HashMap<>();

    public DeliveryService() {
        // Initialize default slots for the day (Requirement R10)
        slots.put("slot1", new DeliverySlot("slot1", "08:00-10:00", true, false, 3));  // Peak, capacity 3
        slots.put("slot2", new DeliverySlot("slot2", "10:00-12:00", false, true, 3));  // Eco-friendly, capacity 3
        slots.put("slot3", new DeliverySlot("slot3", "18:00-20:00", true, false, 3));  // Peak, capacity 3
    }

    public DeliverySlot getSlot(String slotId) {
        return slots.get(slotId);
    }

    public void displayAvailableSlots() {
        System.out.println("Available Delivery Slots:");
        for (DeliverySlot slot : slots.values()) {
            System.out.printf("- %s [%s] | Peak: %b | Eco: %b | Full: %b%n", 
                slot.getId(), slot.getTimeWindow(), slot.isPeak(), slot.isEcoFriendly(), !slot.hasCapacity());
        }
    }

    public double computeBaseDeliveryFee(Cart cart, DeliveryRequest request) {
        double totalWeight = computeTotalWeight(cart);

        if (totalWeight > 50.0) {
            throw new IllegalStateException("delivery refused: total weight exceeds 50 kg");
        }

        double baseFee = 20.0;
        
        if (totalWeight < 10.0 && request.getDistanceKm() <= 30.0) {
            baseFee = 15.0;
        } else if (totalWeight >= 10.0 && totalWeight <= 50.0) {
            baseFee = 15.0 + 0.05 * cart.computeTotal();
        }

        if (request.getSlot().isPeak()) {
            baseFee += 5.0; // Peak surcharge
        }
        if (request.getSlot().isEcoFriendly()) {
            baseFee -= 3.0; // Eco-friendly discount
        }

        // Ensure fee doesn't drop below 0
        return Math.max(0, baseFee); 
    }

    public double computeTotalWeight(Cart cart) {
        double totalWeight = 0.0;

        for (CartLine line : cart.getLines()) {
            totalWeight += line.getItem().getWeight() * line.getQuantity();
        }

        return totalWeight;
    }
}