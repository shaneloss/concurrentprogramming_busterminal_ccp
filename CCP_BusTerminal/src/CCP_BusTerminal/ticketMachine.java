package CCP_BusTerminal;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class ticketMachine 
{
    Semaphore tmSem=new Semaphore(1,true);
    ticket t;
    boolean available;
    ticketNoGenerator tng;
    
    public ticketMachine(ticketNoGenerator tng)
    {
        this.tng=tng;
        available=true;
    }
    
    public ticket getTicket(customer c, String destination)
    {
        if(tmSem.availablePermits()==0)
        {
            System.out.println("Ticket Machine is in-use... Customer "+c.custID+" is queuing for Ticket Machine...");
        }
        try
        {
            tmSem.acquire();
        }
        catch(Exception e){}
        if(available==false)
        {
            System.out.println("Customer "+c.custID+" exit the queue at Ticket Machine");
            tmSem.release();
            return null;
        }
        else
        {
            System.out.println("Customer "+c.custID+" is using the Ticket Machine");
            try
            {
                Thread.sleep(500);
            }
            catch(Exception e){}
            //the ticket machine can only be broken while a customer is using it
            int rand=new Random().nextInt(10); //probability of ticket machine breaking is 10%
            if(rand==0)
            {
                breakDown();
                System.out.println("Customer "+c.custID+" haven't receive any Ticket");
                System.out.println("Customer "+c.custID+" is leaving the Ticket Machine");
                tmSem.release();
                return null;
            }
            try
            {
                Thread.sleep(500);
            }
            catch(Exception e){}
            int ticketNo=tng.getTicketNumber();
            t=new ticket(ticketNo, destination);
            System.out.println("Ticket Machine has print ticket: "+t.ticketNo+" (Destination: "+t.destination+")");
            System.out.println("Customer "+c.custID+" has left the Ticket Machine");
            tmSem.release();
            return t;
        }
    }
    
    public void breakDown()
    {
        available=false;
        System.out.println("\t\tTicket Machine has breakdown... Ticket Machine cannot be use...");
    }
    
    public void repair()
    {
        System.out.println("\t\tTicket Machine is repaired... Ticket Machine can be use...");
        available=true;
    }
}
