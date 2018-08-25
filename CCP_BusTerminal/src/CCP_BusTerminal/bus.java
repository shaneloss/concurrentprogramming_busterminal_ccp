package CCP_BusTerminal;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class bus implements Callable<bus> //no need to add try and catch inside the call method
{
    int busID;
    CountDownLatch latch;
    waitingArea wa;
    LinkedList<customer> customers=new LinkedList<>();
    String destination;
    boolean waitFull; //use a boolean to know which type of bus (true. wait until full// false. go when full or time limit)
    
    public bus(int busID, String destination, waitingArea wa, CountDownLatch latch, boolean waitFull)
    {
        this.busID=busID;
        this.destination=destination;
        this.wa=wa;
        this.latch=latch;
        this.waitFull=waitFull;
    }
    
    public void enterBus(customer c)
    {
        wa.leaveWA(c); //customer need to exit the waiting area to enter the bus
        customers.addFirst(c); //the customer is added to a LinkedList, when the bus depart all the customer will be printed
        latch.countDown(); //this is to reduce the latch. when the latch counter reach 0 the bus will depart
        System.out.println("Customer "+c.custID+" has entered Bus: "+busID+" (bus "+busID+" population: "+(12-latch.getCount())+")");
        try
        {
            Thread.sleep(100);
        }
        catch(Exception e){}
    }
    
    @Override
    public bus call() throws Exception
    {
        System.out.println("\tBus: "+busID+" has arrived at the Bus Terminal");
        wa.busArrive(this);
        if(waitFull==true)
        {
            System.out.println("\tBus: "+busID+" is waiting to be full before departing");
            //try
            //{
                latch.await();
            //}
            //catch(Exception e){}
            System.out.println("\tBus: "+busID+" is full");
            System.out.println("\tAll customers in Bus: "+busID+" :");
            for(customer c: customers) //this display the customers inside the bus
            {
                System.out.println("\tCustomer  "+c.custID+" is in Bus: "+busID);
                c.bt.btSem.release();
            }
            wa.busDepart();
        }
        else //waitFull is false
        {
            System.out.println("\tBus: "+busID+" will depart in 45 seconds (or full)");
            //try
            //{
                latch.await(45, TimeUnit.SECONDS);
            //}
            //catch(Exception e){}
            if(customers.size()==12)
            {
                System.out.println("\tBus: "+busID+" is full");
            }
            else
            {
                System.out.println("\tBus: "+busID+" has waited 45 seconds");
            }
            System.out.println("\tAll ("+(12-latch.getCount())+") customers in Bus: "+busID+" :");
            for(customer c: customers)
            {
                System.out.println("\tCustomer "+c.custID+" is in Bus: "+busID);
                c.bt.btSem.release();
            }
            wa.busDepart();
        }
        return this;
    }
}
