package src.main;

public class Customer implements Runnable {
    private int customerId;
    private int retrievalInterval;
    private TicketPool ticketPool;

    // Constructor
    public Customer(int customerId, int retrievalInterval, TicketPool ticketPool){
        this.customerId = customerId;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;
    }
    // run method
    @Override
    public void run(){
        try{
            while (!Thread.currentThread().isInterrupted()){  //retrieve tickets while the thread is not interrupted
                if (ticketPool.removeTicket(customerId)){  // a ticket was removed from the pool and customer id passed to ticket pool class
                }else {
                    System.out.println("Customer "+ customerId+ " found no tickets.");
                }
                Thread.sleep(retrievalInterval);  //retrievalInterval
            }
        }catch (InterruptedException e){  // the thread was interrupted
            System.out.println("Customer "+ customerId +" interrupted.");
            Thread.currentThread().interrupt();
        }
    }

}
