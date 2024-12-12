package src.main;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    int maxTicketCapacity;  // max capacity of the ticket pool
    int currentTicketCapacity;
    private Lock tryLock = new ReentrantLock();  //manage concurrent access to the pool

    //Constructor
    public TicketPool(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.currentTicketCapacity = 0;
        this.tryLock = new ReentrantLock();
    }

    // tickets added to the pool
    public void addTickets(int ticketsPerRelease) {
        tryLock.lock();  // thread-safety
        try {
            boolean isFullMessagePrinted;  // ticket pool is full or not message
            if (currentTicketCapacity + ticketsPerRelease <= maxTicketCapacity) {
                currentTicketCapacity += ticketsPerRelease;
                isFullMessagePrinted = false;  // the message is not printed yet
            } else {
                System.out.println("Ticket pool is full.");
                isFullMessagePrinted = true; // ticket pool is full message printed
            }
        } finally {
            tryLock.unlock();  //unlock the pool
        }
    }

    //removing tickets from the pool
    public boolean removeTicket(int customerId) {
        tryLock.lock();
        try {
            if (currentTicketCapacity > 0) {
                currentTicketCapacity--;  // remove one ticket
                log("Customer "+ customerId+" retrieved a ticket. Remaining tickets - "+currentTicketCapacity);
                return true;
            } else {
                log("No tickets are currently available in the pool.");
                return false;
            }
        } finally {
            tryLock.unlock();
        }
    }

    public int getCurrentTicketCapacity() {
        return currentTicketCapacity;
    }

    // log messages with a timestamp
    private void log(String message){ // the format year-month-date hour:minute:second
        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        System.out.println("["+timeStamp + "] " + message);
    }
}
