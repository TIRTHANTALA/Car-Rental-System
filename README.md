# Advanced Car Rental System 🚗

A console-based application built with **Core Java** that allows users to manage a car rental business. It features separate portals for Managers and Customers, Object-Oriented design, and a File I/O system that acts as a simple database so no data is lost between sessions.

## 🌟 Key Features

- **Data Persistence:** Automatically saves all cars, customers, and revenue to local `.txt` files (`cars.txt`, `customers.txt`, `revenue.txt`).
- **Manager Dashboard:** 
  - Add and remove cars from the inventory.
  - Update the rent per day for any vehicle.
  - View all registered customers and delete them if necessary.
  - Track total business revenue.
- **Customer Portal:**
  - Secure registration using a 10-digit unique contact number.
  - Browse available cars to rent.
  - Rent a car securely (which updates its status in real-time).
  - Return a car and receive a fully formatted **Bill Receipt** with date, time, and calculated total cost.
- **Robust Input Handling:** The system uses `try-catch` blocks extensively to prevent the program from crashing if a user enters incorrect data types (like letters instead of numbers).

## 🚀 How to Run

**Prerequisites:** You must have Java (JDK) installed on your system.

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/TIRTHANTALA/Car-Rental-System.git
   ```
2. Open a terminal and navigate to the project directory.
3. Compile the Java file:
   ```bash
   javac CarRental.java
   ```
4. Run the program:
   ```bash
   java -cp . Main
   ```

## 🔒 Credentials

To access the Manager Dashboard, use the default admin password:
- **Password:** `car123`

## 🛠️ Built With

* **Language:** Java (Core)
* **Storage:** File I/O (Plain text files)
* **Architecture:** Object-Oriented Programming (Classes, Collections, Encapsulation)
