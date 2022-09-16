public class Record {
    private final int id;
    private final int entrance;
    private final String carSize;
    private final int floor;
    private final int parkingSpot;
    private final int durationInDays;
    private final String payment;

    public Record(int id, int entrance, String carSize, int floor, int parkingSpot, int durationInDays, String payment) {
        this.id = id;
        this.entrance = entrance;
        this.carSize = carSize;
        this.floor = floor;
        this.parkingSpot = parkingSpot;
        this.durationInDays = durationInDays;
        this.payment = payment;
    }

    public int getId() {
        return id;
    }

    public int getEntrance() {
        return entrance;
    }

    public String getCarSize() {
        return carSize;
    }

    public int getFloor() {
        return floor;
    }

    public int getParkingSpot() {
        return parkingSpot;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public String getPayment() {
        return payment;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append(";").append(entrance).append(";").append(carSize).append(";");
        stringBuilder.append(floor).append(";").append(parkingSpot).append(";").append(durationInDays).append(";");
        stringBuilder.append(payment);
        return stringBuilder.toString();
    }
}