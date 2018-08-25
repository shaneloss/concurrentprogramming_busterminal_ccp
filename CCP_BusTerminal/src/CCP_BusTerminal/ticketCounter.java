package CCP_BusTerminal;

import java.util.concurrent.Semaphore;

public class ticketCounter 
{
    Semaphore tcSem=new Semaphore(1,true);
    ticket t;
    String ticketCounterName; //the ticketcountername is ticketCounter A or B
    boolean available; //this is to know if the staff is in toilet break
    boolean servingCustomer; //this is to know if the counter is currently in use or not, so that the staff can go toilet
    ticketNoGenerator tng;
    
    public ticketCounter(ticketNoGenerator tng, String ticketCounterName)
    {
        this.tng=tng;
        this.ticketCounterName=ticketCounterName;
        available=true;
    }
    
    public void goToilet()
    {
        available=false;
        System.out.println("\t\tThe staff from Ticket Counter "+ticketCounterName+" is going to toilet");
        System.out.println("\t\tTicket Counter "+ticketCounterName+" is closed");
    }
    
    public void returnFromToilet()
    {
        System.out.println("\t\tThe staff from Ticket Counter "+ticketCounterName+" has return");
        available=true;
        System.out.println("\t\tTicket Counter "+ticketCounterName+" is open");
    }
    
    public ticket getTicket(customer c, String destination)
    {
        if(tcSem.availablePermits()==0)
        {
            System.out.println("Ticket Counter "+ticketCounterName+" is occupied... Customer "+c.custID+" is queuing");
        }
        try
        {
            tcSem.acquire();
        }
        catch(Exception e){}
        if(available==false)
        {
            System.out.println("Customer "+c.custID+" exit the queue at Ticket Counter "+ticketCounterName);
            tcSem.release();
            return null;
        }
        else
        {
            servingCustomer=true;
            System.out.println("Customer "+c.custID+" is at Ticket Counter "+ticketCounterName);
            try
            {
                Thread.sleep(3000);
            }
            catch(Exception e){}
            int ticketNo=tng.getTicketNumber();
            t=new ticket(ticketNo,destination);
            System.out.println("Customer "+c.custID+" has receive ticket: "+t.ticketNo+" ("+t.destination+")");
            System.out.println("Customer "+c.custID+" has left Ticket Counter "+ticketCounterName);
            servingCustomer=false;
            tcSem.release();
            return t;
        }
        
    }
}
