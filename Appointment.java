public class Appointment {
    private static int appointmentIDCounter = 1;
    private final int appointmentID;
    private final Patient patient;
        private Dermatologist dermatologist;
                private String date;
                    private String time;
                            private Treatment treatment;
                                            public static final double TAX_RATE = 0.025;
                                        
                                            // Constructor
                                            public Appointment(Patient patient, Dermatologist dermatologist, String date, String time, Treatment treatment) {
                                                this.appointmentID = appointmentIDCounter++;
                                                this.patient = patient;
                                                this.dermatologist = dermatologist;
                                                this.date = date;
                                                this.time = time;
                                                this.treatment = treatment;
                                            }
                                        
                                            // Getters
                                            public int getAppointmentID() {
                                                return appointmentID;
                                            }
                                        
                                            public Patient getPatient() {
                                                return patient;
                                            }
                                            
                                        
                                            public String getDate() {
                                                return date;
                                            }
                                        
                                            public String getTime() {
                                                return time;
                                            }
                                        
                                            public Treatment getTreatment() {
                                                return treatment;
                                            }
                                        
                                            public Dermatologist getDermatologist() {
                                                return dermatologist;
                                            }
                                        
                                            // Setters to update appointment details
                                            public void setDate(String date) {
                                                this.date = date;
                                        }
            
        
                                    public void setDermatologist(Dermatologist dermatologist) {
                                        this.dermatologist = dermatologist;
                            }
                        
                            public void setTime(String time) {
                                this.time = time;
                    }
                
                    public void setTreatment(Treatment treatment) {
                        this.treatment = treatment;
    }

    // Calculate total cost with tax
    public double calculateTotalCost(double registrationFee) {
        double treatmentCost = treatment.getPrice();
        double taxAmount = treatmentCost * TAX_RATE;
        double total = treatmentCost + registrationFee + taxAmount;
        return Math.round(total * 100.0) / 100.0;
    }

   
    // Generate Invoice
    public void generateInvoice(double registrationFee) {
        double taxAmount = treatment.getPrice() * TAX_RATE;
        double totalCost = calculateTotalCost(registrationFee);

        System.out.println("\n--- Appointment Invoice ---");
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Patient Name: " + patient.getName());
        System.out.println("Dermatologist: " + dermatologist.getName());
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("Treatment: " + treatment.getName());
        System.out.println("Registration Fee: LKR " + registrationFee);
        System.out.println("Treatment Price: LKR " + treatment.getPrice());
        System.out.println("Tax Amount (2.5%): LKR " + Math.round(taxAmount * 100.0) / 100.0);
        System.out.println("Appointment Duration: 15 minutes");
        System.out.println("Total Cost (incl. tax): LKR " + totalCost);
        System.out.println("----------------------------");
    }

    
}

