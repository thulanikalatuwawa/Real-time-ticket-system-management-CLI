// K.D THULANI PIYATHMA KALATUWAWA - 20232217 (IIT) - W2053489 (UOW)
// I declare that my work contains no examples of misconduct, such as plagiarism, or collusion.
// Any code taken from other sources is referenced within my code solution.

package src.main;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Configuration config = null;

        System.out.println("----------- Welcome To The Ticket Management System -------------");
        System.out.print(""" 
                Would you like to -
                1. Create a new configuration
                2. Load a saved file:  """);
        String choice = input.nextLine().trim();  // converting the int input to a string

        if (choice.equals("1")) {  // create a new configuration
            int totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity;
            // Inputs for configuration and checking the input range
            do {
                System.out.print("Enter the maximum ticket capacity (1-500): ");
                maxTicketCapacity = input.nextInt();
                if (maxTicketCapacity < 1 || maxTicketCapacity > 500) {
                    System.out.println("The max ticket capacity must be between 1 and 500.");
                }
            } while (maxTicketCapacity < 1 || maxTicketCapacity > 500);

            do {
                System.out.print("Enter the total number of tickets (1-500): ");
                totalTickets = input.nextInt();
                if (totalTickets < 1 || totalTickets > 500) {
                    System.out.println("Total tickets must be between 1 and 500.");
                } else if (totalTickets > maxTicketCapacity) {
                    System.out.println("Total tickets cannot exceed the maximum ticket capacity.");
                }
            } while (totalTickets < 1 || totalTickets > 500 || totalTickets > maxTicketCapacity);

            do {
                System.out.print("Enter the ticket release rate (1-500) per minute: ");
                ticketReleaseRate = input.nextInt();
                if (ticketReleaseRate < 1 || ticketReleaseRate > 500) {
                    System.out.println("The ticket release rate must be between 1 and 500.");
                } else if (ticketReleaseRate > maxTicketCapacity) {
                    System.out.println("Ticket release rate cannot exceed the maximum ticket capacity.");
                } else if (ticketReleaseRate > totalTickets) {
                    System.out.println("Ticket release rate cannot exceed the total number of tickets.");
                }
            } while (ticketReleaseRate < 1 || ticketReleaseRate > 500 || ticketReleaseRate > maxTicketCapacity || ticketReleaseRate > totalTickets);

            do {
                System.out.print("Enter the customer retrieval rate(1-2000 milliseconds): ");
                customerRetrievalRate = input.nextInt();
                if (customerRetrievalRate < 1 || customerRetrievalRate > 2000) {
                    System.out.println("The customer retrieval rate must be between 1 and 2000");
                }
            } while (customerRetrievalRate < 1 || customerRetrievalRate > 2000);


            config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
            System.out.println("New configuration created.");

            String answer;
            do {
                System.out.print("Would you like to save this configuration? (y/n): ");
                answer = input.next();

                if (answer.equalsIgnoreCase("y")) { // Save the configuration
                    Configuration.jsonFileWriter(config, "config.json");
                    System.out.println("File saved as config.json");
                } else if (answer.equalsIgnoreCase("n")) {
                    System.out.println("The file will not be saved.");
                } else {
                    System.out.println("Wrong input. Try again...");
                }
            } while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n"));


            // loading previously saved configurations
        } else if (choice.equals("2")) {
            List<String> savedFiles = Configuration.listSavedFiles(); // get the list of saved files
            if (savedFiles.isEmpty()) {
                System.out.println("No saved configuration files found.");
                return;
            } else {
                System.out.println("Saved configuration files: ");
                for (int i = 0; i < savedFiles.size(); i++) {
                    System.out.println((i + 1) + ". " + savedFiles.get(i));  // display the saved files
                }
                System.out.println("Enter the number of the file you want to load: ");
                int fileChoice = input.nextInt();

                if (fileChoice >= 1 && fileChoice <= savedFiles.size()) {
                    String fileName = savedFiles.get(fileChoice - 1);
                    Configuration loadJsonFile = Configuration.loadJsonFile(fileName);

                    if (loadJsonFile != null) {  // If no error in loading Json file
                        System.out.println("Loaded Configuration: ");
                        System.out.println("Total tickets: " + loadJsonFile.getTotalTickets());
                        System.out.println("Tickets release rate: " + loadJsonFile.getTicketReleaseRate());
                        System.out.println("Customer retrieval rate: " + loadJsonFile.getCustomerRetrievalRate());
                        System.out.println("Max ticket capacity: " + loadJsonFile.getMaxTicketCapacity());
                    } else {
                        System.out.println("No configuration file found or error in loading");
                    }
                } else {
                    System.out.println("Invalid file selection.");
                    return;
                }
            }
        } else System.out.println("Invalid choice. Run the system again...");

        // if configuration created
        if (config != null) {
            // create ticket pool
            TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity());

            // Create and start threads for vendors and customers
            int vendorCount = 3; // Number of vendors
            Thread[] vendorThreads = new Thread[vendorCount];
            for (int i = 0; i < vendorCount; i++) {
                Vendor vendor = new Vendor(i + 1, config.getTicketReleaseRate(), 60000, ticketPool);  //set to 60seconds
                vendorThreads[i] = new Thread(vendor);
            }
            // Create and start customer threads
            int customerCount = 5; // Number of customers
            Thread[] customerThreads = new Thread[customerCount];
            for (int i = 0; i < customerCount; i++) {
                Customer customer = new Customer(i + 1, config.getCustomerRetrievalRate(),  ticketPool);
                customerThreads[i] = new Thread(customer);
            }
// start or exit the system
            while (true) {
                System.out.print("Enter 's' to run the system, 'x' to exit the system: ");
                String command = input.next().toLowerCase();

                if (command.equals("s")) {
                    boolean isRunning = true;
                    System.out.println("Ticket operations have started.");

                    // start threads
                    for (Thread vendorThread : vendorThreads) {
                        if (!vendorThread.isAlive()) {
                            vendorThread.start();
                        }
                    }
                    for (Thread customerThread : customerThreads) {
                        if (!customerThread.isAlive()) {
                            customerThread.start();
                        }
                    }
                    while (true) {
                        System.out.println("System is running. Enter 'e' to stop.");
                        String stopCommand = input.next().toLowerCase();

                        if (stopCommand.equals("e")) {
                            System.out.println("Stopping the system...");
                            System.out.println("Ticket operations have stopped.");
                            System.exit(0);
                        }else System.out.println("Invalid input. The system is still running.");
                    }
                } else if (command.equals("x")) {
                    System.out.println("Exiting from system");
                    System.exit(0);
                }else System.out.println("Invalid command.");

                input.close();
            }
        }
    }
}

// REFERENCES
// 1 - READ & WRITE JSON: https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
//                      - https://stackoverflow.com/questions/10926353/how-to-read-json-file-into-java-with-simple-json-library
//                      -  https://www.tutorialspoint.com/how-to-write-create-a-json-file-using-java
// 4 - LOCK IN JAVA: https://www.javatpoint.com/lock-in-java
// 5 - STRING TO FILE: https://stackoverflow.com/questions/1053467/how-do-i-save-a-string-to-a-text-file-using-java




