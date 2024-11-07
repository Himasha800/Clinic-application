
public class Dermatologist {
    private final String name;
    private final String availableDays;
    private final String availableTime;

    // Constructor
    public Dermatologist(String name, String availableDays, String availableTime) {
        this.name = name;
        this.availableDays = availableDays;
        this.availableTime = availableTime;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public String getAvailableTime() {
        return availableTime;
    }
}
