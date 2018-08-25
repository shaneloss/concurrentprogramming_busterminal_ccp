/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CCP_BusTerminal;

/**
 *
 * @author GP62 6QF
 */
public class ticket 
{
    int ticketNo;
    String destination; //(West, South, East)
    boolean scanned;
    boolean inspected;
    
    public ticket(int ticketNo, String destination)
    {
        this.ticketNo=ticketNo;
        this.destination=destination;
    }
    
    public void scanTicket()
    {
        scanned=true;
    }
    
    public void inspectTicket()
    {
        inspected=true;
    }
}
