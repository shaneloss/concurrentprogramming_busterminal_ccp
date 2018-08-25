package CCP_BusTerminal;

import java.util.Random;

public class staff implements Runnable
{
    ticketCounter tc;
    
    public staff(ticketCounter tc)
    {
        this.tc=tc;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                //the staff want to go toilet every 15 to 30 seconds
                Thread.sleep(15000+new Random().nextInt(30000));
            }
            catch(Exception e){}
            while(true)
            {
                //here we use tryAcquire. if the staff thread can acquire then the staff can go to toilet
                if(tc.tcSem.tryAcquire()==true)
                {
                    break;
                }
                
            }
            tc.goToilet();
            //the staff release the permit for tcSem (the semaphore for the counter) because the customer need to acquire the permit to then exit the queue
            tc.tcSem.release();
            try
            {
                //the staffs toilet break is of 10 sec
                Thread.sleep(10000);
            }
            catch(Exception e){}
            tc.returnFromToilet();
        }
    }
}
