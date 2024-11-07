
public class Patient {
    private final String name;
    private final String nic;
    private final String email;
    private final String phone;

    // Constructor
    public Patient(String name, String nic, String email, String phone) {
        this.name = name;
        this.nic = nic;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getNic() {
        return nic;
    }
    
    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
