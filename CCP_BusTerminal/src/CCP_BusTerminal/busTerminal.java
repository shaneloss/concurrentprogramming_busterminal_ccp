package CCP_BusTerminal;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class busTerminal extends Thread
{
    Semaphore btSem=new Semaphore(100,true); //100 customers can be inside the terminal at a time
    Semaphore gateSem=new Semaphore(1,true); //customer enter waiting area 1 by 1
    waitingArea waWest;
    waitingArea waSouth;
    waitingArea waEast;
    ticketCounter tcA;
    ticketCounter tcB;
    ticketMachine tm;
    ticketScanner tsW;
    ticketScanner tsS;
    ticketScanner tsE;
    inspector ti;
    busNoGenerator bng;
    ticketNoGenerator tng;
    LinkedList<Future<bus>> busesW;
    LinkedList<Future<bus>> busesS;
    LinkedList<Future<bus>> busesE;
    boolean open;
    int custID=1;
    
    public static void main(String[] args) 
    {
        busTerminal bt=new busTerminal();
        bt.start();
    }
    
    public busTerminal()
    {
        open=true;
        bng=new busNoGenerator();
        tng=new ticketNoGenerator();
        tsW=new ticketScanner();
        tsS=new ticketScanner();
        tsE=new ticketScanner();
        waWest=new waitingArea("West", tsW);
        waSouth=new waitingArea("South", tsS);
        waEast=new waitingArea("East",tsE);
        tcA=new ticketCounter(tng, "A");
        tcB=new ticketCounter(tng, "B");
        tm=new ticketMachine(tng);
        ti=new inspector();
        busesW=new LinkedList<>();
        busesS=new LinkedList<>();
        busesE=new LinkedList<>();
    }
    
    @Override
    public void run()
    {
        //create the thread pool
        ExecutorService terminal=Executors.newCachedThreadPool();
        //submit all the thread inside th thread pool
        terminal.submit(new guard(this));
        terminal.submit(new repairman(tm));
        terminal.submit(new staff(tcA));
        terminal.submit(new staff(tcB));
        terminal.submit(new busManager(bng, "West", waWest, terminal, busesW)); 
        terminal.submit(new busManager(bng, "South", waSouth, terminal, busesS));
        terminal.submit(new busManager(bng, "East", waEast, terminal, busesE));
        terminal.submit(new recorder(busesW,"West.txt"));
        terminal.submit(new recorder(busesS,"South.txt"));
        terminal.submit(new recorder(busesE,"East.txt"));
        
        while(true)
        {
            terminal.submit(new customer(custID, this, tcA, tcB, tm, waWest, waSouth, waEast, ti)); 
            custID++;
            try
            {
                //thread.sleep(200) is just to test if the terminal close and open 
                //Thread.sleep(200);
                Thread.sleep(1000*(1+new Random().nextInt(3))); //every 1-4 seconds a new customer arrive
            }
            catch(Exception e){}
        }
    }
    
    public void block()
    {
        open=false;
        System.out.println("Bus Terminal entrance is block");
        try 
        {
            btSem.acquire(20); //acquire(20) is used to block the entrance of 
                                //the terminal until at least 20 customer leave the terminal
            System.out.println("Bus Terminal entrance is open");
            btSem.release(20); //release directly after so that customer can enter
        }
        catch(Exception e){}

        open=true;
    }
    
    public void enterTerminal(customer c)
    {
        if(open==false)
        {
            System.out.println("Guard is blocking the entrance... Customer "+c.custID+" is waiting... (terminal population: "+(100-btSem.availablePermits())+")");
        }
        try
        {
            gateSem.acquire(); //only one customer go throught the gate at a time
            btSem.acquire(); //this is the Semaphore for the terminal sem(100)
            Thread.sleep(10);
            System.out.println("Customer "+c.custID+" has entered the Bus Terminal (terminal population: "+(100-btSem.availablePermits())+")");
            Thread.sleep(100); //customer take 0.1 second to enter the Bus Terminal
            gateSem.release();
        }
        catch(Exception e){}  
    }
    
    public void leaveTerminal(customer c)
    {
        System.out.println("Customer "+c.custID+" is leaving the Bus Terminal");
        btSem.release();
    }
}
