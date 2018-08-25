package CCP_BusTerminal;

public class ticketNoGenerator 
{
    private static int ticketOrder=0;
    
    public synchronized int getTicketNumber()
    {
        //the first return ticket number is 1 (0++=1)
        ticketOrder++;
        return ticketOrder;
    }
}
