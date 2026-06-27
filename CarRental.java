import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Car class representing a car entity
class Car implements Serializable {
    private String id;
    private String model;
    private boolean isAvailable;
    private int perDayRent;
    private String rentedByContact;

    // Constructor
    public Car(String id, String model, int perDayRent) {
        this.id = id;
        this.model = model;
        this.isAvailable = true;
        this.perDayRent = perDayRent;
        this.rentedByContact = null;
    }
    
    // Getters
    public String getId() { return id; }
    public String getModel() { return model; }
    public boolean isAvailable() { return isAvailable; }
    public int getPerDayRent() { return perDayRent; }
    public String getRentedByContact() { return rentedByContact; }

    // Setters
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setPerDayRent(int perDayRent) { this.perDayRent = perDayRent; }
    public void setRentedByContact(String contact) { this.rentedByContact = contact; }
    
    @Override
    public String toString() {
        return id + "," + model + "," + perDayRent + "," + isAvailable + "," + (rentedByContact == null ? "None" : rentedByContact);
    }
    
    public static Car fromString(String line) {
        String[] parts = line.split(",");
        Car c = new Car(parts[0], parts[1], Integer.parseInt(parts[2]));
        c.setAvailable(Boolean.parseBoolean(parts[3]));
        String renter = parts[4];
        if (!renter.equals("None")) {
            c.setRentedByContact(renter);
        }
        return c;
    }
}

// Customer class
class Customer {
    private String name;
    private String contact;

    public Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }
    
    public String getName() { return name; }
    public String getContact() { return contact; }
    
    @Override
    public String toString() {
        return name + "," + contact;
    }
    
    public static Customer fromString(String line) {
        String[] parts = line.split(",");
        return new Customer(parts[0], parts[1]);
    }
}

// Main System Class
class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private double totalRevenue = 0;
    private Scanner sc = new Scanner(System.in);
    
    public CarRentalSystem() {
        loadData();
    }
    
    // ----------- Data Persistence -----------
    
    private void loadData() {
        try {
            File carFile = new File("cars.txt");
            if (carFile.exists()) {
                Scanner fileSc = new Scanner(carFile);
                while (fileSc.hasNextLine()) {
                    cars.add(Car.fromString(fileSc.nextLine()));
                }
                fileSc.close();
            }
            
            File custFile = new File("customers.txt");
            if (custFile.exists()) {
                Scanner fileSc = new Scanner(custFile);
                while (fileSc.hasNextLine()) {
                    customers.add(Customer.fromString(fileSc.nextLine()));
                }
                fileSc.close();
            }
            
            File revFile = new File("revenue.txt");
            if (revFile.exists()) {
                Scanner fileSc = new Scanner(revFile);
                if (fileSc.hasNextDouble()) {
                    totalRevenue = fileSc.nextDouble();
                }
                fileSc.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    
    private void saveData() {
        try {
            FileWriter carWriter = new FileWriter("cars.txt");
            for (Car c : cars) {
                carWriter.write(c.toString() + "\n");
            }
            carWriter.close();
            
            FileWriter custWriter = new FileWriter("customers.txt");
            for (Customer c : customers) {
                custWriter.write(c.toString() + "\n");
            }
            custWriter.close();
            
            FileWriter revWriter = new FileWriter("revenue.txt");
            revWriter.write(String.valueOf(totalRevenue));
            revWriter.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    // ----------- Manager Methods -----------
    
    public void addCar(String id, String model, int perDayRent) {
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                System.out.println("[!] Car ID already exists.");
                return;
            }
        }
        cars.add(new Car(id, model, perDayRent));
        saveData();
        System.out.println("[+] Car added successfully!");
    }
    
    public void removeCar(String id) {
        Iterator<Car> it = cars.iterator();
        while (it.hasNext()) {
            Car c = it.next();
            if (c.getId().equals(id)) {
                if (!c.isAvailable()) {
                    System.out.println("[!] Cannot remove car. It is currently rented by someone.");
                    return;
                }
                it.remove();
                saveData();
                System.out.println("[-] Car removed successfully.");
                return;
            }
        }
        System.out.println("[!] Car not found.");
    }
    
    public void editCarRent() {
        System.out.print("Enter Car ID to edit rent: ");
        String cId = sc.nextLine();
        for (Car c : cars) {
            if (c.getId().equals(cId)) {
                System.out.print("Enter new Rent per day: Rs. ");
                try {
                    int newRent = Integer.parseInt(sc.nextLine());
                    c.setPerDayRent(newRent);
                    saveData();
                    System.out.println("[+] Rent updated successfully!");
                } catch (NumberFormatException e) {
                    System.out.println("[!] Invalid input for rent.");
                }
                return;
            }
        }
        System.out.println("[!] Invalid Car ID.");
    }
    
    public void viewAllCars() {
        System.out.println("\n--- All Cars Inventory ---");
        if (cars.isEmpty()) {
            System.out.println("No cars available in inventory.");
            return;
        }
        for (Car c : cars) {
            String status = "Available";
            if (!c.isAvailable()) {
                String renterName = getCustomerNameByContact(c.getRentedByContact());
                status = "Rented by " + renterName;
            }
            System.out.printf("ID: %-4s | Model: %-15s | Rent: Rs.%-5d | Status: %s\n", c.getId(), c.getModel(), c.getPerDayRent(), status);
        }
        System.out.println("--------------------------");
    }
    
    public void viewCustomers() {
        System.out.println("\n--- Registered Customers ---");
        if (customers.isEmpty()) {
            System.out.println("No Customers have Registered.");
            return;
        }
        for (Customer c : customers) {
            System.out.println("Name: " + c.getName() + " | Contact: " + c.getContact());
        }
        System.out.println("----------------------------");
    }
    
    public void removeCustomer() {
        System.out.print("Enter Customer Contact Number to remove: ");
        String contact = sc.nextLine();
        
        // Check if they currently have a car rented
        for (Car c : cars) {
            if (!c.isAvailable() && contact.equals(c.getRentedByContact())) {
                System.out.println("[!] Cannot remove customer. They still have a rented car to return.");
                return;
            }
        }
        
        Iterator<Customer> it = customers.iterator();
        while (it.hasNext()) {
            Customer c = it.next();
            if (c.getContact().equals(contact)) {
                it.remove();
                saveData();
                System.out.println("[-] Customer removed successfully.");
                return;
            }
        }
        System.out.println("[!] Customer not found.");
    }
    
    public void viewTotalRevenue() {
        System.out.println("\n==========================");
        System.out.printf("Total Revenue: Rs. %.2f\n", totalRevenue);
        System.out.println("==========================");
    }
    
    // ----------- Customer Methods -----------
    
    public void addCustomer(String name, String contact) {
        if (contact.length() == 10 && contact.matches("\\d+")) {
            for (Customer c : customers) {
                if (c.getContact().equals(contact)) {
                    System.out.println("[!] A user with this contact number is already registered.");
                    return;
                }
            }
            customers.add(new Customer(name, contact));
            saveData();
            System.out.println("[+] Registration Successful! Welcome, " + name + "!");
        } else {
            System.out.println("[!] Invalid contact number. Must be exactly 10 digits.");
        }
    }
    
    public boolean isRegistered(String contact) {
        for (Customer c : customers) {
            if (c.getContact().equals(contact)) {
                return true;
            }
        }
        return false;
    }
    
    public String getCustomerNameByContact(String contact) {
        for (Customer c : customers) {
            if (c.getContact().equals(contact)) {
                return c.getName();
            }
        }
        return "Unknown";
    }
    
    public void viewAvailableCars() {
        System.out.println("\n--- Available Cars to Rent ---");
        boolean found = false;
        for (Car c : cars) {
            if (c.isAvailable()) {
                System.out.printf("ID: %-4s | Model: %-15s | Rent: Rs.%-5d/day\n", c.getId(), c.getModel(), c.getPerDayRent());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Sorry, no cars are currently available.");
        }
        System.out.println("------------------------------");
    }
    
    public void rentCar(String contact) {
        viewAvailableCars();
        System.out.print("Enter Car ID you want to rent: ");
        String id = sc.nextLine();
        
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                if (!c.isAvailable()) {
                    System.out.println("[!] Sorry, this car is already rented.");
                    return;
                }
                c.setAvailable(false);
                c.setRentedByContact(contact);
                saveData();
                System.out.println("\n[+] Success! You have rented the " + c.getModel() + ".");
                System.out.println("    (Billing will be calculated when you return the car).");
                return;
            }
        }
        System.out.println("[!] Invalid Car ID.");
    }
    
    public void returnCar(String contact) {
        System.out.print("Enter Car ID you are returning: ");
        String id = sc.nextLine();
        
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                if (c.isAvailable() || !contact.equals(c.getRentedByContact())) {
                    System.out.println("[!] You cannot return this car. It wasn't rented by you.");
                    return;
                }
                
                System.out.print("How many days did you rent the car for?: ");
                int days = 0;
                try {
                    days = Integer.parseInt(sc.nextLine());
                    if (days <= 0) {
                        System.out.println("Invalid days.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Invalid input for days.");
                    return;
                }
                
                c.setAvailable(true);
                c.setRentedByContact(null);
                
                int cost = c.getPerDayRent() * days;
                totalRevenue += cost;
                saveData();
                
                String customerName = getCustomerNameByContact(contact);
                viewBill(c, customerName, days, cost);
                return;
            }
        }
        System.out.println("[!] Car ID not found.");
    }
    
    private void viewBill(Car c, String customerName, int days, int totalCost) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        System.out.println("\n========================================");
        System.out.println("          CAR RENTAL RECEIPT          ");
        System.out.println("========================================");
        System.out.println("Date: " + now.format(formatter));
        System.out.println("Customer Name: " + customerName);
        System.out.println("----------------------------------------");
        System.out.printf("Car Model      : %s\n", c.getModel());
        System.out.printf("Car ID         : %s\n", c.getId());
        System.out.printf("Rent per day   : Rs. %d\n", c.getPerDayRent());
        System.out.printf("Duration       : %d days\n", days);
        System.out.println("----------------------------------------");
        System.out.printf("Total Amount   : Rs. %d\n", totalCost);
        System.out.println("========================================");
        System.out.println("     Thank you for driving with us!     ");
        System.out.println("========================================\n");
    }
}

// Main class to run the Car Rental System
class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CarRentalSystem system = new CarRentalSystem();
        String adminPass = "car123";

        System.out.println("===========================================");
        System.out.println("       WELCOME TO CAR RENTAL SYSTEM        ");
        System.out.println("===========================================");

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Manager Portal");
            System.out.println("2. Customer Portal");
            System.out.println("3. Exit System");
            System.out.print("Select your portal (1-3): ");
            
            int ch = 0;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid number.");
                continue;
            }

            if (ch == 1) { // MANAGER PORTAL
                System.out.print("Enter Manager Password: ");
                String entPass = sc.nextLine();

                if (entPass.equals(adminPass)) {
                    while (true) {
                        System.out.println("\n--- MANAGER DASHBOARD ---");
                        System.out.println("1. Add a New Car");
                        System.out.println("2. Remove a Car");
                        System.out.println("3. View All Cars");
                        System.out.println("4. Update Rent Per Day");
                        System.out.println("5. View All Customers");
                        System.out.println("6. Remove a Customer");
                        System.out.println("7. View Total Revenue");
                        System.out.println("8. Logout to Main Menu");
                        System.out.print("Enter your choice: ");
                        
                        int eChoice = 0;
                        try {
                            eChoice = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("[!] Invalid input.");
                            continue;
                        }

                        if (eChoice == 8) {
                            System.out.println("Logging out manager...");
                            break;
                        }

                        switch (eChoice) {
                            case 1:
                                System.out.print("Enter Car ID: ");
                                String id = sc.nextLine();
                                System.out.print("Enter Car Model: ");
                                String model = sc.nextLine();
                                System.out.print("Enter Rent per Day: Rs. ");
                                try {
                                    int perDayRent = Integer.parseInt(sc.nextLine());
                                    system.addCar(id, model, perDayRent);
                                } catch (NumberFormatException e) {
                                    System.out.println("[!] Invalid price.");
                                }
                                break;
                            case 2:
                                System.out.print("Enter Car ID to remove: ");
                                String removeId = sc.nextLine();
                                system.removeCar(removeId);
                                break;
                            case 3:
                                system.viewAllCars();
                                break;
                            case 4:
                                system.editCarRent();
                                break;
                            case 5:
                                system.viewCustomers();
                                break;
                            case 6:
                                system.removeCustomer();
                                break;
                            case 7:
                                system.viewTotalRevenue();
                                break;
                            default:
                                System.out.println("[!] Invalid choice.");
                        }
                    }
                } else {
                    System.out.println("[!] Invalid password. Access denied.");
                }
            } else if (ch == 2) { // CUSTOMER PORTAL
                while (true) {
                    System.out.println("\n--- CUSTOMER PORTAL ---");
                    System.out.println("1. Register as New Customer");
                    System.out.println("2. View Available Cars");
                    System.out.println("3. Rent a Car");
                    System.out.println("4. Return a Car & Pay Bill");
                    System.out.println("5. Exit to Main Menu");
                    System.out.print("Enter your choice: ");
                    
                    int tChoice = 0;
                    try {
                        tChoice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Invalid input.");
                        continue;
                    }

                    if (tChoice == 5) {
                        break;
                    }

                    switch (tChoice) {
                        case 1:
                            System.out.print("Enter your Full Name: ");
                            String name = sc.nextLine();
                            System.out.print("Enter your Contact (10 digits): ");
                            String contact = sc.nextLine();
                            system.addCustomer(name, contact);
                            break;
                        case 2:
                            system.viewAvailableCars();
                            break;
                        case 3:
                            System.out.print("Enter your Registered Contact Number: ");
                            String cContact = sc.nextLine();
                            if (!system.isRegistered(cContact)) {
                                System.out.println("[!] Contact number not found. Please register first.");
                                break;
                            }
                            system.rentCar(cContact);
                            break;
                        case 4:
                            System.out.print("Enter your Registered Contact Number: ");
                            String returnContact = sc.nextLine();
                            if (!system.isRegistered(returnContact)) {
                                System.out.println("[!] Contact number not found. Please register first.");
                                break;
                            }
                            system.returnCar(returnContact);
                            break;
                        default:
                            System.out.println("[!] Invalid choice.");
                    }
                }
            } else if (ch == 3) {
                System.out.println("\nExiting system... Thank you!");
                break;
            } else {
                System.out.println("[!] Invalid choice.");
            }
        }
    }
}
