package fr.cs.oose.delivery;

public class DeliverySlot {
    private final String id;
    private final String timeWindow; // e.g., "10:00-12:00"
    private final boolean isPeak;
    private final boolean isEcoFriendly;
    private int currentBookings = 0;
    private final int maxCapacity; // e.g., max 3 deliveries per slot

    public DeliverySlot(String id, String timeWindow, boolean isPeak, boolean isEcoFriendly, int maxCapacity) {
        this.id = id;
        this.timeWindow = timeWindow;
        this.isPeak = isPeak;
        this.isEcoFriendly = isEcoFriendly;
        this.maxCapacity = maxCapacity;
    }

    public String getId() {
        return id;
    }

    public String getTimeWindow() {
        return timeWindow;
    }

    public boolean isPeak() {
        return isPeak;
    }

    public boolean isEcoFriendly() {
        return isEcoFriendly;
    }
    
    public boolean hasCapacity() {
        return currentBookings < maxCapacity;
    }

    public void book() {
        if (!hasCapacity()) {
            throw new IllegalStateException("Slot is fully booked");
        }
        currentBookings++;
    }
}