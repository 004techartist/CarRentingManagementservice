import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

class Car {
    private static final Scanner scanner = new Scanner(System.in);
    private String carId;
    private String carType;
    private String carModel;
    private boolean isRented;
    private double hourlyRate;
    private static final List<String> rentedCarIds = new ArrayList<>();

    public void inputCarDetails() {
        System.out.println("Enter the car ID: ");
        carId = scanner.nextLine();
        System.out.println("Enter the Car type: ");
        carType = scanner.nextLine();
        System.out.println("Enter the car Model: ");
        carModel = scanner.nextLine();
        System.out.println("Enter the hourly rate for the car: ");
        hourlyRate = scanner.nextDouble();
        scanner.nextLine();
    }

    public void saveToFile() {
        String filename = "car_details.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(carId + "," + carType + "," + carModel + "," + isRented + "," + hourlyRate);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Car> readFromFile() {
        List<Car> cars = new ArrayList<>();
        String filename = "car_details.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Car car = new Car();
                    car.carId = parts[0];
                    car.carType = parts[1];
                    car.carModel = parts[2];
                    car.isRented = Boolean.parseBoolean(parts[3]);
                    car.hourlyRate = Double.parseDouble(parts[4]);
                    cars.add(car);
                    if (car.isRented) {
                        rentedCarIds.add(car.carId);
                    }
                } else {
                    System.err.println("Skipping invalid car entry: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static void viewAllCars(List<Car> cars) {
        for (Car car : cars) {
            System.out.println("Car ID: " + car.carId);
            System.out.println("Car Type: " + car.carType);
            System.out.println("Car Model: " + car.carModel);
            System.out.println("Is Rented: " + (car.isRented ? "Yes" : "No"));
            System.out.println("Hourly Rate: $" + car.hourlyRate);
            System.out.println();
        }
    }


    public static void addRentedCarId(String carId) {
        rentedCarIds.add(carId);
    }

    public static void removeRentedCarId(String carId) {
        rentedCarIds.remove(carId);
    }


    public String getCarId() {
        return carId;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }
}

class Customer {
    private static final Scanner scanner = new Scanner(System.in);
    private String name;
    private String nationality;
    private int age;
    private String idNo;

    public void inputCustomerDetails() {

        System.out.println("Enter customer name: ");
        name = scanner.nextLine();
        System.out.println("Enter the customer nationality: ");
        nationality = scanner.nextLine();
        System.out.println("Enter the customer age: ");
        age = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter the Identification number/ passport number: ");
        idNo = scanner.nextLine();
    }

    public void saveToFile() {
        String filename = "customer_details.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(name + "," + nationality+ "," + age + ","+ idNo);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Customer> readFromFile() {
        List<Customer> customers = new ArrayList<>( );
        String filename = "customer_details.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename) )) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Customer customer = new Customer();
                    customer.name = parts[0];
                    customer.nationality = parts[1];
                    customer.age = Integer.parseInt(parts[2]);
                    customer.idNo = parts[3];
                    customers.add(customer);
                } else {
                    System.err.println("Skipping invalid customer entry: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static void viewAllCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            System.out.println("Customer Name: " + customer.name);
            System.out.println("Customer Nationality: " + customer.nationality);
            System.out.println("Customer Age: " + customer.age);
            System.out.println("Customer ID/Passport No: " + customer.idNo);
            System.out.println();
        }
    }

    
    public String getIdNo() {
        return idNo;
    }

    public String getName() {
        return name;
    }
}
class Rental {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void rentCar(List<Car> cars) {
        System.out.println("Enter customer ID: ");
        String customerId = scanner.nextLine();

        System.out.println("Enter car ID to rent: ");
        String carId = scanner.nextLine();

        Car car = cars.stream().filter(c -> c.getCarId().equals(carId) && !c.isRented()).findFirst().orElse(null);
        if (car != null) {
            car.setRented(true);
            Car.addRentedCarId(carId);  
            car.saveToFile();
            System.out.println("Enter return date and time (yyyy-MM-dd HH:mm:ss): ");
            String returnDate = scanner.nextLine();
            String rentalDate = dateFormat.format(new Date());

            saveRentalToFile(customerId, carId, rentalDate, returnDate);
            double rentalCost = calculateRentalCost(car, rentalDate, returnDate);
            System.out.println("Rental cost: $" + rentalCost);

            System.out.println("Car rented successfully.");
        } else {
            System.out.println("Car not found or already rented.");
        }
    }

    private static void saveRentalToFile(String customerId, String carId, String rentalDate, String returnDate) {
        String filename = "rental_transactions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(customerId + "," + carId + "," + rentalDate + "," + returnDate);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewRentals(List<Customer> customers) {
        String filename = "rental_transactions.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String customerId = parts[0];
                    String carId = parts[1];
                    String rentalDate = parts[2];
                    String returnDate = parts[3];

                    String customerName = customers.stream()
                            .filter(c -> c.getIdNo().equals(customerId))
                            .map(Customer::getName)
                            .findFirst()
                            .orElse("Unknown Customer");

                    System.out.println("Customer Name: " + customerName);
                    System.out.println("Customer ID: " + customerId);
                    System.out.println("Car ID: " + carId);
                    System.out.println("Rental Date: " + rentalDate);
                    System.out.println("Return Date: " + returnDate);
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void returnCar(List<Car> cars, List<Customer> customers) {
        System.out.println("Enter customer ID: ");
        String customerId = scanner.nextLine();

        System.out.println("Enter car ID to return: ");
        String carId = scanner.nextLine();

        Car car = cars.stream().filter(c -> c.getCarId().equals(carId) && c.isRented()).findFirst().orElse(null);
        if (car != null) {
            car.setRented(false);
            Car.removeRentedCarId(carId);  
            car.saveToFile();
            System.out.println("Car returned successfully.");
        } else {
            System.out.println("Car not found or not rented.");
        }
    }

    private static double calculateRentalCost(Car car, String rentalDate, String returnDate) {
        try {
            Date rentDate = dateFormat.parse(rentalDate);
            Date retDate = dateFormat.parse(returnDate);
            long diffInMillies = Math.abs(retDate.getTime() - rentDate.getTime());
            long diffInHours = diffInMillies / (1000 * 60 * 60);
            return diffInHours * car.getHourlyRate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}


public class CarRentalSystem {
    public static void main(String[ ] args) {
        Scanner scanner = new Scanner(System.in);
        List<Car> cars = Car.readFromFile();
        List<Customer> customers = Customer.readFromFile();

        while (true) {
            System.out.println("KELVIN CAR RENTAL SYSTEM");
            System.out.println("1. Add car");
            System.out.println("2. View all cars");
            System.out.println("3. Add customer");
            System.out.println("4. View all customers");
            System.out.println("5. Rent car");
            System.out.println("6. View rentals");
            System.out.println("7. Return car");
            System.out.println("8. Exit");
            System.out.print("Enter your choice : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:

                    Car car = new Car();
                    car.inputCarDetails();
                    car.saveToFile();
                    cars.add(car);


                    break;

                case 2:

                    Car.viewAllCars(cars);

                    break;


                case 3:

                    Customer customer = new Customer();
                    customer.inputCustomerDetails();
                    customer.saveToFile();
                    customers.add(customer);

                    break;


                case 4:

                    Customer.viewAllCustomers(customers);


                    break;

                case 5:
                    Rental.rentCar(cars);
                    break;
                    
                case 6:
                    Rental.viewRentals(customers);
                    break;
                case 7:
                    Rental.returnCar(cars, customers);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
