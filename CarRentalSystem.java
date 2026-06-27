import java.util.Scanner;

// Car class representing a car entity
class Car {
    String id;
    String model;
    boolean isAvailable;
    int perDayRent;

    // Constructor to initialize car details
    Car(String id, String model, int perDayRent) {
        this.id = id;
        this.model = model;
        this.isAvailable = true;
        this.perDayRent = perDayRent;
    }
    
    // Getter methods
    String getId() { return id; }
    String getModel() { return model; }
    boolean isAvailable() { return isAvailable; }
    int getPerDayRent() { return perDayRent; }

    // Setter method to update availability status
    void setAvailable(boolean available) {
        isAvailable = available;
    }
}

// Customer class representing a customer entity
class Customer {
    String name;
    String contact;

    // Constructor to initialize customer details
    Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }
}

// Main Car Rental System class
class CarRentalSystem {
    Car[] car = new Car[100]; // Array to store cars
    Customer[] customer = new Customer[100]; // Array to store customers
    int carCount = 0;
    int customerCount = 0;
    int totalRevenue = 0;

    // Method to add a car
    void addCar(String id, String model, int perDayRent) {
        if (carCount < car.length) {
            car[carCount++] = new Car(id, model, perDayRent);
            System.out.println("Car added successfully!");
        } else {
            System.out.println("Cannot add more cars. Storage full.");
        }
    }

    // Method to remove a car by ID
    void removeCar(String id) {
        for (int i = 0; i < carCount; i++) {
            if (car[i].getId().equals(id)) {
                for (int j = i; j < carCount - 1; j++) {
                    car[j] = car[j + 1];
                }
                car[--carCount] = null;
                System.out.println("Car removed successfully.");
                return;
            }
        }
        System.out.println("Car not found.");
    }

    // Method to add a customer
    void addCustomer(String name, String contact) {
        if (contact.length() == 10) {
            if (customerCount < customer.length) {
                customer[customerCount++] = new Customer(name, contact);
                System.out.println("Customer added successfully!");
            } else {
                System.out.println("Cannot add more customers. Storage full.");
            }
        } else {
            System.out.println("Invalid contact no.");
        }
    }

    // Method to check if a customer is registered
    boolean Register(String name) {
        for (int i = 0; i < customerCount; i++) {
            if (customer[i].name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    // Method to view registered customers
    void viewCustomers() {
        System.out.println("Registered Customers:");
        if (customerCount >= 1) {
            for (int i = 0; i < customerCount; i++) {
                System.out.println("Name:" + customer[i].name + ", Contact:" + customer[i].contact);
            }
        } else {
            System.out.println("No Customer has Registered");
        }
    }

    // Method to view available cars
    void viewAvailableCars() {
        System.out.println("Available Cars:");
        for (int i = 0; i < carCount; i++) {
            if (car[i].isAvailable()) {
                System.out.println("Car Id:" + car[i].id + ", Model:" + car[i].model + ", Rent Per Day:" + car[i].perDayRent);
            }
        }
    }

    // Method to rent a car
    void rentCar(String id, int duration, String Cname) {
        if (!Register(Cname)) {
            System.out.println("Customer not registered. Please register first.");
            return;
        }
        for (int i = 0; i < carCount; i++) {
            if (car[i].getId().equals(id) && car[i].isAvailable()) {
                car[i].setAvailable(false);
                int cost = car[i].getPerDayRent() * duration;
                totalRevenue += cost;
                System.out.println("Car rented successfully for " + duration + " days. Total cost: " + cost);
                System.out.println("Rented by: " + Cname);
                return;
            }
        }
        System.out.println("Car not available or invalid ID.");
    }

    // Method to return a rented car
    void returnCar(String id) {
        for (int i = 0; i < carCount; i++) {
            if (car[i].getId().equals(id) && !car[i].isAvailable()) {
                car[i].setAvailable(true);
                System.out.println("Car returned successfully.");
                return;
            }
        }
        System.out.println("Invalid ID or the car is not currently rented.");
    }

    // Method to view total revenue
    void viewTotalRevenue() {
        System.out.println("Total Revenue: " + totalRevenue);
    }
}

// Main class to run the Car Rental System
class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CarRentalSystem c = new CarRentalSystem();
        String pass = "car123"; // Manager password

        while (true) {
            System.out.println("\nCar Rental System:");
            System.out.println("1. Manager");
            System.out.println("2. Customer");
            System.out.println("3. Exit System");
            int ch = sc.nextInt();
            sc.nextLine();

            if (ch == 1) {
                System.out.print("Enter password : ");
                String entPass = sc.nextLine();

                if (entPass.equals(pass)) {
                    while (true) {
                        System.out.println("----------------------");
                        System.out.println("1. Add Car ");
                        System.out.println("2. Remove Car ");
                        System.out.println("3. View total revenue ");
                        System.out.println("4. View all customers ");
                        System.out.println("5. Exit to main menu ");
                        System.out.println("----------------------");
                        System.out.print("Enter your choice: ");
                        int eChoice = sc.nextInt();
                        sc.nextLine();

                        switch (eChoice) {
                            case 1:
                                System.out.print("Enter Car ID: ");
                                String id = sc.nextLine();
                                System.out.print("Enter Car Model: ");
                                String model = sc.nextLine();
                                System.out.print("Enter Rent per Day: ");
                                int perDayRent = sc.nextInt();
                                sc.nextLine();
                                c.addCar(id, model, perDayRent);
                                break;
                            case 2:
                                System.out.print("Enter Car ID to remove: ");
                                String removeId = sc.nextLine();
                                c.removeCar(removeId);
                                break;
                            case 3:
                                c.viewTotalRevenue();
                                break;
                            case 4:
                                c.viewCustomers();
                                break;
                            case 5:
                                System.out.println("Returning to main menu.");
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }

                        if (eChoice == 5) {
                            break;
                        }
                    }
                } 
				else {
                    System.out.println("Invalid password. Access denied.");
                }
            } else if (ch == 2) {
                while (true) {
                    System.out.println("-----------------------------");
                    System.out.println("Enter 1 to register as a customer");
                    System.out.println("Enter 2 to view available cars");
                    System.out.println("Enter 3 to rent a car");
                    System.out.println("Enter 4 to return a car");
                    System.out.println("Enter 5 Exit to main menu");
                    System.out.println("-----------------------------");
                    System.out.print("Enter your choice: ");
                    int tChoice = sc.nextInt();
                    sc.nextLine();

                    switch (tChoice) {
                        case 1:
                            System.out.print("Enter your name: ");
                            String name = sc.nextLine();
                            System.out.print("Enter your contact (only 10 digit): ");
                            String contact = sc.nextLine();
                            c.addCustomer(name, contact);
                            break;
                        case 2:
                            System.out.print("Enter your name (to verify registration): ");
                            String cname = sc.nextLine();
                            if (!c.Register(cname)) {
                                System.out.println("You need to register first.");
                                break;
                            }
                            c.viewAvailableCars();
                            break;
                        case 3:
                            System.out.print("Enter your name (to verify registration): ");
                            String Cname = sc.nextLine();
                            if (!c.Register(Cname)) {
                                System.out.println("You need to register first.");
                                break;
                            }
                            System.out.print("Enter Car ID to rent: ");
                            String rentId = sc.nextLine();
                            System.out.print("Enter How many days rent a car: ");
                            int duration = sc.nextInt();
                            sc.nextLine();
                            c.rentCar(rentId, duration, Cname);
                            break;
                        case 4:
                            System.out.print("Enter your name (to verify registration): ");
                            String Name = sc.nextLine();
                            if (!c.Register(Name)) {
                                System.out.println("You need to register first.");
                                break;
                            }
                            System.out.print("Enter Car ID to return: ");
                            String returnId = sc.nextLine();
                            c.returnCar(returnId);
                            break;
                        case 5:
                            System.out.println("Returning to main menu.");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }
                    if (tChoice == 5) {
                        break;
                    }
                }
            }
			else if (ch == 3) {
                System.out.println("Exiting system...");
                break; 
			}
			else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
