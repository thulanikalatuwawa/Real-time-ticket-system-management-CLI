# Real-Time Ticket Management System (Java CLI)

## Overview
This is a Java-based Command Line Interface (CLI) ticket management system where multiple vendors and customers interact with a shared ticket pool. 
Vendors release tickets into the pool, while customers attempt to retrieve tickets from the pool. 
The system allows users to create configurations, load saved configurations, and manage tickets in a multi threaded environment. 
Ticket data is stored and loaded from JSON files.

## Features
- **Ticket Pool**: Manages a pool of tickets with a specified maximum capacity.
- **Vendors**: Multiple vendors can release tickets into the pool at a specified release rate(every 60 seconds).
- **Customers**: Multiple customers can attempt to retrieve tickets from the pool at a specified retrieval rate.
- **Configuration**: Users can create new configurations, load existing ones, and save configurations to JSON files.
- **Thread Safety**: Uses `ReentrantLock` to manage concurrent access to the ticket pool.
- **Logging**: Logs ticket operations with timestamps.

## Requirements
- JDK 11 or higher
- A command-line terminal

## Setup and Installation

1. **Clone the Repository**:  
   Clone this repository to your local machine.
   ```bash
   git clone <repository-url>
2. **Compile the code**
   Navigate to the project folder and compile the Java files using the javac command:
   ```bash
   java src/main/*.java
3. **Run the program**
   After compiling the code, run the Main class:
   ```bash 
   java src.main.Main

## How to Use

### Main Menu
Upon launching, the system will prompt you with two options:
            1. Create a new Configuration
            2. Load a saved Configuration

#### Configuration Creation
When creating a new configuration, you'll be prompted to enter the following details:
- **Maximum Ticket Capacity (1–500)** - The maximum number of tickets the event can hold
- **Total Number of Tickets (1–500, cannot exceed max capacity)** - The total number of tickets that are available to sell
- **Ticket Release Rate (1–200 tickets per minute)** - The number of tickets the vendor will add to the pool every minute
- **Customer Retrieval Rate (1–2000 milliseconds)** - How frequently a customer will attempt to retrieve a ticket.

Once the configuration is entered, you will be asked whether to save it to a file. 
If you choose "yes", the configuration will be saved with a timestamp-based filename (e.g., config_1616161616161.json).

#### Loading Saved Configurations
If you choose to load a saved configuration, the system will list all saved .json configuration files. 
Select the file you want to load, and the system will display the configuration details.

##### Running the System
After creating or loading a configuration, you can run the system by entering s to start the ticket operations. Vendors will begin releasing tickets, and customers will attempt to retrieve them based on the rates defined in the configuration. The system will continue running until you enter e to stop it.

##### Exiting the System
To exit the system at any time, type x when prompted.




   

