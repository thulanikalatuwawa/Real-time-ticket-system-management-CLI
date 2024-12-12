package src.main;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Configuration {
    private int totalTickets;  // total number of tickets that are available in the pool
    private int ticketReleaseRate; // number of tickets the vendor will add to the pool
    private int customerRetrievalRate; // how frequently a customer attempts to retrieve a ticket (in milliseconds)
    private int maxTicketCapacity; // the maximum number of tickets for the event

    //Constructor
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Checking input validation
    public boolean validationInput(){
        return totalTickets>0 && totalTickets<=500 &&
                ticketReleaseRate>0 && ticketReleaseRate<=500 &&
                customerRetrievalRate>0 && customerRetrievalRate<=2000 &&  // one ticket every 2 seconds
                maxTicketCapacity>0 && maxTicketCapacity<=500 &&
                maxTicketCapacity>=totalTickets;      //total number of tickets can't exceed maximum number of tickets
    }

    // getters and setters
    public int getTotalTickets() {
        return totalTickets;
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

// ---------------------------------------------- JSON FILE ------------------------------------------------------------
    // save to a json file
    public static void jsonFileWriter(Configuration config, String fileName) {
        JSONObject jsonObject = new JSONObject();  // create a new json object and store details in it
        jsonObject.put("totalTickets", config.getTotalTickets());
        jsonObject.put("ticketReleaseRate", config.getTicketReleaseRate());
        jsonObject.put("customerRetrievalRate", config.getCustomerRetrievalRate());
        jsonObject.put("maxTicketCapacity", config.getMaxTicketCapacity());

        // generate a file name using the time stamp
        fileName = "config_" +System.currentTimeMillis()+".json";

        // write the data to a json file
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonObject.toJSONString());
            System.out.println("Configuration saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();   // in case of an error
        }
    }
    // loading from json files
    public static Configuration loadJsonFile(String fileName) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(fileName));   // parse the file
            JSONObject jsonObject = (JSONObject) obj;

            int totalTickets = ((Long) jsonObject.get("totalTickets")).intValue();
            int ticketReleaseRate = ((Long) jsonObject.get("ticketReleaseRate")).intValue();
            int customerRetrievalRate = ((Long) jsonObject.get("customerRetrievalRate")).intValue();
            int maxTicketCapacity = ((Long) jsonObject.get("maxTicketCapacity")).intValue();

            return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;  // in case of an error while loading the file
        }
    }

    // list of all the files saved
    public static List<String> listSavedFiles() {
        File folder = new File(".");
        File[] files = folder.listFiles(((dir, name) -> name.endsWith(".json"))); // only json files
        List<String> filenames = new ArrayList<>();  //storing the files
        if (files != null) {
            for (File file : files) {
                filenames.add(file.getName());
            }
        }
        return filenames;
    }
}
