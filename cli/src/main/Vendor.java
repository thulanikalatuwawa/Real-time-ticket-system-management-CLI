package src.main;

public class Vendor implements Runnable {
    private int vendorID;
    private int ticketsPerRelease;  // the number of tickets that will be added to the pool at once
    private int releaseInterval;  // milliseconds - set to 60 seconds
    private TicketPool ticketPool;

    private boolean running = true;  // state of the thread

    // Constructor
    public Vendor(int vendorID, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool) {
        this.vendorID = vendorID;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;
    }

    //run method
    @Override
    public void run() {
        while (running) {
            try {
                if (ticketPool.getCurrentTicketCapacity() < ticketPool.maxTicketCapacity) {
                    ticketPool.addTickets(ticketsPerRelease);
                    System.out.println("Vendor " + vendorID + " released " + ticketsPerRelease + " tickets. Total tickets available: "+ticketPool.getCurrentTicketCapacity());
                } else {
                    System.out.println("Vendor " + vendorID + " can't release more tickets.");
                }
                Thread.sleep(releaseInterval); //waiting time before releasing more tickets
            } catch (InterruptedException e) {  //thread was interrupted
                System.out.println("Vendor " + vendorID + " interrupted.");
                Thread.currentThread().interrupt();
            }
        }
    }
}

