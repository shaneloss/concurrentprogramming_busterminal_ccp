package CCP_BusTerminal;

public class guard implements Runnable
{
    busTerminal bt;
    
    public guard(busTerminal bt)
    {
        this.bt=bt;
    }
    //the guard only check to see if the terminal is full
    @Override
    public void run()
    {
        while(true)
        {   
            //if there is no more available permit, the guard will block the gate
            if(bt.btSem.availablePermits()==0)
            {
                bt.block();
            }
        }
    }
}
