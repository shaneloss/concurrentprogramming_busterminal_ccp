package CCP_BusTerminal;

import java.util.concurrent.Semaphore;

public class ticketScanner 
{
    Semaphore semScanner=new Semaphore(1,true); //only 1 person can use a ticket scanner at a time
    
    public ticket scanningTicket(customer c)
    {
        if(semScanner.availablePermits()==0) //if someone is using the scanner
        {
            System.out.println("Ticket Scanner is in use... Customer "+c.custID+" is waiting for Ticket Scanner...");
        }
        try
        {
            semScanner.acquire();
            System.out.println("Customer "+c.custID+" is using the Ticket Scanner");
            Thread.sleep(1000);
        }
        catch(Exception e){}
        c.t.scanTicket();
        System.out.println("Customer "+c.custID+"'s Ticket ("+c.t.ticketNo+") has been scanned");
        semScanner.release();
        return c.t;
    }
}
