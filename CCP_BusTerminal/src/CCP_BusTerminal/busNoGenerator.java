package CCP_BusTerminal;

public class busNoGenerator //this is to create unique busID
{
    private static int busOrder=0;
    
    public synchronized int getBusNumber()
    {
        busOrder++;
        return busOrder;
    }
}
