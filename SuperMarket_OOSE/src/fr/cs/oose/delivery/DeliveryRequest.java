package fr.cs.oose.delivery;

public class DeliveryRequest {
    private final String address;
    private final double distanceKm;
    private final DeliverySlot slot;

    public DeliveryRequest(String address, double distanceKm, DeliverySlot slot) {
        this.address = address;
        this.distanceKm = distanceKm;
        this.slot = slot;
    }

    public String getAddress() {
        return address;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public DeliverySlot getSlot() {
        return slot;
    }
}