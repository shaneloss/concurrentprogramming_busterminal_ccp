package CCP_BusTerminal;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class busManager implements Runnable
{
    String destination;
    waitingArea wa;
    ExecutorService terminal;
    busNoGenerator bng;
    boolean waitFull;
    LinkedList<Future<bus>> buses;
    
   public busManager(busNoGenerator bng, String destination, waitingArea wa, ExecutorService terminal, LinkedList<Future<bus>> buses)
   {
       this.bng=bng;
       this.wa=wa;
       this.destination=destination;
       this.terminal=terminal;
       this.buses=buses;
   }
   
   @Override
   public void run()
   {
       while(true)
       {
           CountDownLatch latch=new CountDownLatch(12);
           int busID=bng.getBusNumber(); //use the busNoGenerator to get a unique busID
           int rand=new Random().nextInt(2);
           if(rand==0)
           {
               waitFull=true; //the bus only go when full
           }
           else
           {
               waitFull=false; //the bus go if full or time limit
           }
           while(true)
           {
               if(wa.busDock.hasQueuedThreads()==true) //if there is already another bus waiting a new bus will not come
               {
                   try
                   {
                        Thread.sleep(5000);
                   }
                   catch(Exception e){}
               }
               else
               {
                   break;
               }
           }
           Future<bus> f=terminal.submit(new bus(busID, destination, wa, latch, waitFull));
           buses.addLast(f); //the future is added to a linked list that will be used on the recorder class
           try
           {
               Thread.sleep(30000+(new Random().nextInt(30000))); //30-60 seconds interval
           }
           catch(Exception e){}
       }
   }
}
