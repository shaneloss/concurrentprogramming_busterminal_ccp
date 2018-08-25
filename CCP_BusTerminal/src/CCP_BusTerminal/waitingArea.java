package CCP_BusTerminal;

import java.util.concurrent.Semaphore;

public class waitingArea 
{
    Semaphore busDock=new Semaphore(1,true); //only 1 bus can wait at a waiting area
    Semaphore enterBus=new Semaphore(1,true); //1 customer a a time enter the bus
    Semaphore seatingWA=new Semaphore(10,true); //10 customer can be in the waiting area at a time
    String waName; //waitingarea (West, South, East)
    ticketScanner ts; //each waitingarea has a ticketScanner
    bus b=null;
    
    public waitingArea(String waName, ticketScanner ts)
    {
        this.waName=waName;
        this.ts=ts;
    }
    
    public void busArrive(bus b)
    {
        if(busDock.availablePermits()==0) //if there is already a bus
        {
            System.out.println("\tThere is already a bus docked in front of Waiting Area: "+waName);
            System.out.println("\tBus "+b.busID+" is waiting to be docked");
        }
        try
        {
            busDock.acquire(); 
            System.out.println("\tBus "+b.busID+" is docked at Waiting Area: "+waName);
        }
        catch(Exception e){}
        this.b=b;        
    }
    
    public void busDepart()
    {
        System.out.println("\tBus "+b.busID+" is leaving Waiting Area: "+waName);
        b=null;
        busDock.release();
    }
    
    public void enterWA(customer c)
    {
        if(seatingWA.availablePermits()==0)
        {
            System.out.println("Waiting Area: "+waName+" is full... Customer "+c.custID+" is waiting to enter the Waiting Area... (waiting area "+waName+" population: "+(10-seatingWA.availablePermits())+")");   
        }
        try
        {
            seatingWA.acquire();
            System.out.println("Customer "+c.custID+" has entered Waiting Area: "+waName+" (waiting area "+waName+" population: "+(10-seatingWA.availablePermits())+")");
        }
        catch(Exception e){}
    }
    
    public void leaveWA(customer c)
    {
        System.out.println("Customer "+c.custID+" is leaving the Waiting Area: "+waName);
        seatingWA.release();
    }
    
    public void enterBus(customer c)
    {          
        try
        {
            enterBus.acquire();
        }
        catch(Exception e){}
        while(true)
        {
            if(b!=null)
            {
                b.enterBus(c);
                enterBus.release();
                break;
            }
            else
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch(Exception e){}
            }
        }
    }
}
