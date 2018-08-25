package CCP_BusTerminal;

import java.util.Random;
import java.util.concurrent.Callable;

public class repairman implements Runnable
{
    ticketMachine tm;
    
    public repairman(ticketMachine tm)
    {
        this.tm=tm;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            //if the machine is working (available=true) then the repairman doesn't need to do anything
            if(tm.available==true)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch(Exception e){}
            }
            else //the machine is not working anymore
            {
                try
                {
                    System.out.println("\t\tRepairman is coming to fix the Ticket Machine");
                    Thread.sleep(2000+(new Random().nextInt(3))*1000);
                    System.out.println("\t\tRepairman is fixing the Ticket Machine...");
                    Thread.sleep(5000+(new Random().nextInt(5))*1000);
                }
                catch(Exception e){}
                tm.repair();
            }
        }
    }
}