package CCP_BusTerminal;

import java.util.concurrent.Semaphore;

public class inspector 
{
    Semaphore semInspector=new Semaphore(1);
    String location="South"; //when the bus terminal open the inspector is in waiting area South
    
    public ticket inspectingTicket(customer c)
    {
        if(!location.equals(c.t.destination)) //if the inspector is not in the same waiting area as the customer
        {
            System.out.println("Customer "+c.custID+" is waiting for the Inspector");
        }
        else //if the inspector is in the same waiting area as the customer
        {
            if(semInspector.availablePermits()==0) //if someone is already accessing the ticket inspector
            {
                System.out.println("Inspector is serving another customer... Customer "+c.custID+" is waiting");
            }
        }
        try
        {
            semInspector.acquire();
        }
        catch(Exception e){}
        if(!location.equals(c.t.destination)) //if the inspector is in a different waiting area, the inspector will need to switch waiting area before inspecting the customer ticket
        {
            System.out.println("Inspector is moving to Waiting Area: "+c.t.destination);
            location=c.t.destination;
            try
            {
                Thread.sleep(1000); //the inspector take 1 second to switch waiting area
            }
            catch(Exception e){}
            System.out.println("Inspector is in Waiting Area: "+c.t.destination);
        }
        System.out.println("Inspector is inspecting Customer "+c.custID+"'s Ticket");
        try
        {
            Thread.sleep(500);
        }
        catch(Exception e){}
        c.t.inspectTicket();
        System.out.println("Customer "+c.custID+"'s Ticket ("+c.t.ticketNo+") has been inspected");
        semInspector.release();
        return c.t;
    }
}
