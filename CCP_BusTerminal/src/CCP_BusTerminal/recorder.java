package CCP_BusTerminal;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class recorder implements Runnable
{
    //this class was created just to show how Future works
    LinkedList<Future<bus>> buses;
    bus b=null;
    Future<bus> f;
    File fil;
    FileWriter fw;

    public recorder(LinkedList<Future<bus>> buses, String filename) 
    {
        this.buses = buses;
        fil=new File(filename);
    }
    
    @Override
    public void run()
    {
        try
        {
            //this part is made to erase the text file
            fw=new FileWriter(fil,false);
            fw.write("");
            fw.close();
        }
        catch(Exception e){}
        
        while(true)
        {
            try
            {
                //f is a Future<bus> and retrieve the first element from the buses which is a LinkedList<Future<bus>>
                f=buses.getFirst();
                //b is a bus, and try to retrieve the bus inside the Future. If the Future result hasn't appear yet, the thread will wait until the result appear
                b=f.get();
                for(customer c: b.customers)
                {
                    try
                    {
                        fw=new FileWriter(fil,true);
                        fw.write("BUS("+b.busID+") CUSTOMER("+c.custID+") TICKET("+c.t.ticketNo+")\n");
                        fw.close();
                    }
                    catch(Exception e){System.out.println("");}
                        
                }
                buses.removeFirst();
                b=null;
            }
            catch(Exception e){}
        }    
    }        
}            

