
public class Treatment {
    private final String type;   
    private final double price;  

    // Constructor
    public Treatment(String type, double price) {
        this.type = type;   
        this.price = price; 
    }

    // Getter for treatment name
    public String getName() {
        return type; 
    }

    // Getter for price
    public double getPrice() {
        return price;
    }
}
