package CCP_BusTerminal;

import java.util.Random;

public class customer implements Runnable
{
    int custID;
    ticket t=null;
    String destination;
    
    busTerminal bt;
    ticketCounter tcA;
    ticketCounter tcB;
    ticketMachine tm;
    waitingArea waWest;
    waitingArea waSouth;
    waitingArea waEast;
    inspector ti;
    waitingArea waCurrent; //the waiting area where the customer is/need to go
    
    public customer(int custID, busTerminal bt, ticketCounter tcA, ticketCounter tcB, ticketMachine tm, waitingArea waWest, waitingArea waSouth, waitingArea waEast, inspector ti)
    {
        this.custID=custID;
        this.bt=bt;
        this.tcA=tcA;
        this.tcB=tcB;
        this.tm=tm;
        this.waWest=waWest;
        this.waSouth=waSouth;
        this.waEast=waEast;
        this.ti=ti;
    }
    
    @Override
    public void run()
    {
        System.out.println("\t\t\tNEW Customer "+custID+" is comming to the Bus Terminal");
        
        //enter terminal
        bt.enterTerminal(this);
        
        //choose destination
        int rand=new Random().nextInt(3); //this is for the customer to choose the destination
        if(rand==0)
        {
            destination="West";
            waCurrent=waWest;
        }
        else if(rand==1)
        {
            destination="South";
            waCurrent=waSouth;
        }
        else
        {
            destination="East";
            waCurrent=waEast;
        }
        
        try
        {
            Thread.sleep(1000);
        }
        catch(Exception e){}
        
        //get ticket
        while(t==null) //while customer doesn't receive any ticket, they will loop this
        {
            rand=new Random().nextInt(3); //this to choose between the counters and the machine
            if(rand==0) //go to ticketMachine
            {
                if(tm.available==true)
                {
                    t=tm.getTicket(this, destination);
                }
            }
            else if(rand==1) //go to ticketCounter A
            {
                if(tcA.available==true)
                {
                    t=tcA.getTicket(this, destination);
                }
            }
            else if(rand==2)
            {
                if(tcB.available==true)
                {
                    t=tcB.getTicket(this, destination);
                }
            }
            else
            {
                try
                {
                    Thread.sleep(10);
                }
                catch(Exception e){}
            }
        }
        
        try
        {
            Thread.sleep(2500);
        }
        catch(Exception e){}
        
        //enter waiting area
        System.out.println("Customer "+custID+" is going to Waiting Area "+t.destination);
        waCurrent.enterWA(this);
        
        //scan and inspect ticket
        rand=new Random().nextInt(2);
        if(rand==0) //scan then inspect
        {
            waCurrent.ts.scanningTicket(this);
            ti.inspectingTicket(this);
        }
        else //inspect then scan
        {
            ti.inspectingTicket(this);
            waCurrent.ts.scanningTicket(this);
        }
        
        //enter bus
        waCurrent.enterBus(this);
    }
}
