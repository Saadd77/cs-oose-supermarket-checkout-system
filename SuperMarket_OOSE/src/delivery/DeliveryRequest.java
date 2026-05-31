package delivery;

public class DeliveryRequest {
    private final String address;
    private final double distanceKm;

    public DeliveryRequest(String address, double distanceKm) {
        this.address = address;
        this.distanceKm = distanceKm;
    }

    public String getAddress() {
        return address;
    }

    public double getDistanceKm() {
        return distanceKm;
    }
}