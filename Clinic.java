import java.text.SimpleDateFormat;
import java.util.*;

public class Clinic {
    private final List<Appointment> appointments;
    private static final double REGISTRATION_FEE = 500.00;
    private static final Scanner scanner = new Scanner(System.in);
    private final Map<String, List<String>> scheduleMap;

    // Generate 15-minute time slots between start and end times
    private static List<String> generateTimeSlots(String startTime, String endTime) {
        List<String> slots = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);

            while (calendar.getTime().before(end)) {
                String time = sdf.format(calendar.getTime());
                slots.add(time);
                calendar.add(Calendar.MINUTE, 15);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slots;
    }

    // Define available slots for each day
    private static final Map<String, List<String>> AVAILABLE_SLOTS = Map.of(
        "Monday", generateTimeSlots("10:00 AM", "01:00 PM"),
        "Wednesday", generateTimeSlots("02:00 PM", "05:00 PM"),
        "Friday", generateTimeSlots("04:00 PM", "08:00 PM"),
        "Saturday", generateTimeSlots("09:00 AM", "01:00 PM")
    );

    // Constructor
    public Clinic() {
        appointments = new ArrayList<>();
        scheduleMap = new HashMap<>();
    }

    // Check if the time slot is available for the dermatologist
    public boolean isTimeSlotAvailable(Dermatologist dermatologist, String dayOfWeek, String time) {
        String key = dermatologist.getName() + "@" + dayOfWeek;
        List<String> bookedTimes = scheduleMap.getOrDefault(key, new ArrayList<>());

        if (bookedTimes.contains(time)) {
            return false;
        }
        bookedTimes.add(time);
        scheduleMap.put(key, bookedTimes);
        return true;
    }

    // Make an appointment
    public void makeAppointment(Patient patient, Dermatologist dermatologist, String date, String time, String dayOfWeek, Treatment treatment) {
        if (!AVAILABLE_SLOTS.containsKey(dayOfWeek) || !AVAILABLE_SLOTS.get(dayOfWeek).contains(time)) {
            System.out.println("Invalid appointment time. Please select from the available slots.");
            return;
        }

        if (!isTimeSlotAvailable(dermatologist, dayOfWeek, time)) {
            System.out.println("The selected time slot is not available for " + dermatologist.getName() + ". Please choose another time.");
            return;
        }

        //  registration fee payment confirmation
        System.out.println("A registration fee of LKR " + REGISTRATION_FEE + " is required to book this appointment.");
        System.out.print("Do you accept the registration payment? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes")) {
           System.out.println("Appointment not booked as the payment was not accepted.");
           return;
        }

        // Prompt for payment amount
        System.out.print("Enter the payment amount: ");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (paymentAmount < REGISTRATION_FEE) {
            System.out.println("Insufficient payment. Appointment not booked.");
            return;
        } else if (paymentAmount > REGISTRATION_FEE) {
            System.out.println("Payment received. Change: LKR " + (paymentAmount - REGISTRATION_FEE));
        } else {
            System.out.println("Payment received. Exact amount.");
        }

        
        // Proceed with booking the appointment after successful payment
        Appointment appointment = new Appointment(patient, dermatologist, date, time, treatment);
        appointments.add(appointment);

        System.out.println("Appointment booked successfully!");

        appointment.generateInvoice(REGISTRATION_FEE);
    }


    

    // Update an appointment by ID
    public void updateAppointment(int appointmentID) {
    Appointment appointmentToUpdate = null;
    for (Appointment appointment : appointments) {
        if (appointment.getAppointmentID() == appointmentID) {
            appointmentToUpdate = appointment;
            break;
        }
    }

    if (appointmentToUpdate == null) {
        System.out.println("Appointment not found.");
        return;
    }

    System.out.println("Updating appointment for: " + appointmentToUpdate.getPatient().getName());

    // Select new date
    System.out.println("\nAvailable Days: Monday, Wednesday, Friday, Saturday");
    System.out.print("Enter new day of the week (or press Enter to keep existing): ");
    String newDayOfWeek = scanner.nextLine();
    if (newDayOfWeek.isEmpty()) {
        newDayOfWeek = dateFromDay(appointmentToUpdate.getDate()); // Keep the existing day
    } else {
        if (!AVAILABLE_SLOTS.containsKey(newDayOfWeek)) {
            System.out.println("Invalid day. Keeping existing day.");
            newDayOfWeek = dateFromDay(appointmentToUpdate.getDate()); // Revert to existing day
        }
    }

    // Display available time slots for the selected day
    System.out.println("\nAvailable Time Slots for " + newDayOfWeek + ":");
    for (String slot : AVAILABLE_SLOTS.get(newDayOfWeek)) {
        System.out.print(slot + " ");
    }
    System.out.println();

    // Select new time slot
    System.out.print("\nEnter new appointment time (or press Enter to keep existing): ");
    String newTime = scanner.nextLine();
    if (newTime.isEmpty()) {
        newTime = appointmentToUpdate.getTime(); // Keep the existing time
    } else {
        if (!AVAILABLE_SLOTS.get(newDayOfWeek).contains(newTime)) {
            System.out.println("Invalid time slot. Keeping existing time.");
            newTime = appointmentToUpdate.getTime(); // Revert to existing time
        } else if (!isTimeSlotAvailable(appointmentToUpdate.getDermatologist(), newDayOfWeek, newTime)) {
            System.out.println("The selected time slot is not available. Keeping existing time.");
            newTime = appointmentToUpdate.getTime(); // Revert to existing time
        }
    }

    // Select new dermatologist if necessary
    System.out.println("Select new dermatologist (or press Enter to keep existing):");
    System.out.println("1. Dr. Alwis");
    System.out.println("2. Dr. John");
    System.out.print("Select a dermatologist (1-2): ");
    int dermatologistChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    Dermatologist newDermatologist = null;
    if (dermatologistChoice == 1) {
        newDermatologist = new Dermatologist("Dr. Alwis", "Dermatology", "0123456789");
    } else if (dermatologistChoice == 2) {
        newDermatologist = new Dermatologist("Dr. John", "Dermatology", "0987654321");
    } else {
        System.out.println("Invalid selection. Keeping existing dermatologist.");
        newDermatologist = appointmentToUpdate.getDermatologist(); // Keep existing dermatologist
    }

    // Update appointment details
    appointmentToUpdate.setDate(newDayOfWeek);
    appointmentToUpdate.setTime(newTime);
    appointmentToUpdate.setDermatologist(newDermatologist);

    System.out.println("Appointment updated successfully!");

   }
   

   // View all appointments filtered by date
    public void viewAppointmentsByDate() {
    System.out.print("Enter the date to view appointments (e.g., Monday, Wednesday, Friday, Saturday): ");
    String inputDate = scanner.nextLine();

    boolean found = false;
    for (Appointment appointment : appointments) {
        if (appointment.getDate().equalsIgnoreCase(inputDate)) {
            found = true;
            System.out.println("\n-- Appointment Details --");
            System.out.println("Appointment ID: " + appointment.getAppointmentID());
            System.out.println("Patient Name: " + appointment.getPatient().getName());
            System.out.println("Date: " + appointment.getDate());
            System.out.println("Time: " + appointment.getTime());
            System.out.println("Dermatologist: " + appointment.getDermatologist().getName());
            System.out.println("Treatment: " + appointment.getTreatment().getName());
            System.out.println("Appointment Duration: 15 minutes");
            System.out.println("-----------------------------");
        }
    }

    if (!found) {
        System.out.println("No appointments found for the specified date.");
    }
}

    // Search for an appointment by patient name or appointment ID
    public void searchAppointment(String query) {
        boolean found = false;
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getName().equalsIgnoreCase(query) ||
                String.valueOf(appointment.getAppointmentID()).equals(query)) {
                found = true;
                System.out.println("\n-- Appointment Details --");
                System.out.println("Appointment ID: " + appointment.getAppointmentID());
                System.out.println("Patient Name: " + appointment.getPatient().getName());
                System.out.println("Date: " + appointment.getDate());
                System.out.println("Time: " + appointment.getTime());
                System.out.println("Dermatologist: " + appointment.getDermatologist().getName());
                System.out.println("Treatment: " + appointment.getTreatment().getName());
                System.out.println("Appointment Duration: 15 minutes");
                System.out.println("-----------------------------");
            }
        }
        if (!found) {
            System.out.println("Appointment not found.");
        }
    }

    // Main method
    public static void main(String[] args) {
        Clinic clinic = new Clinic();

        // Define two dermatologists with specified consultation days and times
        Dermatologist dermatologist1 = new Dermatologist(
            "Dr. Alwis",
            "Monday, Wednesday, Friday, Saturday",
            "Monday: 10:00 AM - 01:00 PM\nWednesday: 02:00 PM - 05:00 PM\nFriday: 04:00 PM - 08:00 PM\nSaturday: 09:00 AM - 01:00 PM"
        );
        Dermatologist dermatologist2 = new Dermatologist(
            "Dr. John",
            "Monday, Wednesday, Friday, Saturday",
            "Monday: 10:00 AM - 01:00 PM\nWednesday: 02:00 PM - 05:00 PM\nFriday: 04:00 PM - 08:00 PM\nSaturday: 09:00 AM - 01:00 PM"
        );

        // Define treatments
        Treatment acneTreatment = new Treatment("Acne Treatment", 2750.00);
        Treatment skinWhitening = new Treatment("Skin Whitening", 7650.00);
        Treatment moleRemoval = new Treatment("Mole Removal", 3850.00);
        Treatment laserTreatment = new Treatment("Laser Treatment", 12500.00);

        System.out.println("Welcome to Aurora Skin Care Clinic");
        System.out.println("Each appointment is 15 minutes.");

        while (true) {
            System.out.println("\n1. Make Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Search Appointment");
            System.out.println("4. Update Appointment");
            System.out.println("5. Exit");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1 -> {
                    // Gather patient information
                    System.out.print("Enter patient name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter NIC: ");
                    String nic = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();
                    Patient patient = new Patient(name, nic, email, phone);

                    // Select treatment
                    System.out.println("\nAvailable Treatments:");
                    System.out.println("1. Acne Treatment - LKR 2750.00");
                    System.out.println("2. Skin Whitening - LKR 7650.00");
                    System.out.println("3. Mole Removal - LKR 3850.00");
                    System.out.println("4. Laser Treatment - LKR 12500.00");
                    System.out.print("Select a treatment (1-4): ");
                    int treatmentChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Treatment selectedTreatment = switch (treatmentChoice) {
                        case 1 -> acneTreatment;
                        case 2 -> skinWhitening;
                        case 3 -> moleRemoval;
                        case 4 -> laserTreatment;
                        default -> null;
                    };

                    if (selectedTreatment == null) {
                        System.out.println("Invalid choice, please try again.");
                        continue;
                    }

                    // Select dermatologist
                    System.out.println("\nAvailable Dermatologists:");
                    System.out.println("1. Dr. Alwis");
                    System.out.println("2. Dr. John");
                    System.out.print("Select a dermatologist (1-2): ");
                    int dermaChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Dermatologist selectedDermatologist = switch (dermaChoice) {
                        case 1 -> dermatologist1;
                        case 2 -> dermatologist2;
                        default -> null;
                    };

                    if (selectedDermatologist == null) {
                        System.out.println("Invalid choice, please try again.");
                        continue;
                    }

                    // Select day of the week
                    System.out.println("\nAvailable Days: Monday, Wednesday, Friday, Saturday");
                    System.out.print("Enter day of the week: ");
                    String dayOfWeek = scanner.nextLine();

                    if (!AVAILABLE_SLOTS.containsKey(dayOfWeek)) {
                        System.out.println("Invalid day. Please select from Monday, Wednesday, Friday, Saturday.");
                        continue;
                    }

                    // Display available time slots for the selected day
                    System.out.println("\nAvailable Time Slots for " + dayOfWeek + ":");
                    for (String slot : AVAILABLE_SLOTS.get(dayOfWeek)) {
                        System.out.print(slot + " ");
                    }
                    System.out.println();

                    // Select time slot
                    System.out.print("\nEnter appointment time (e.g., 10:15 AM): ");
                    String time = scanner.nextLine();

                    if (!AVAILABLE_SLOTS.get(dayOfWeek).contains(time)) {
                        System.out.println("Invalid time slot. Please choose a time from the available slots.");
                        continue;
                    }

                    // Generate date from day (simplified for this example)
                    String date = dateFromDay(dayOfWeek);

                    // Make the appointment
                    clinic.makeAppointment(patient, selectedDermatologist, date, time, dayOfWeek, selectedTreatment);
                }

                case 2 -> clinic.viewAppointmentsByDate();

                case 3 -> {
                    System.out.print("Enter patient name or appointment ID to search: ");
                    String searchQuery = scanner.nextLine();
                    clinic.searchAppointment(searchQuery);
                }

                case 4 -> {
                    System.out.print("Enter Appointment ID to update: ");
                    int appointmentID = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    clinic.updateAppointment(appointmentID);
                }
                

                case 5 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }

                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    // Placeholder method to convert day of week to date (for simplicity)
    private static String dateFromDay(String dayOfWeek) {
        // In a real application, you would convert the day of the week to an actual date.
        // For this example, we'll return the day as the date.
        return dayOfWeek;
    }
}
