//Bradley Wersterfer
//bmw170030
//Submitted on 12/4/2018

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * This is a basic data type that can be used in the TheaterSeat class to store identifying      *
 * information about each seat and act as a payload. It contains the placement (both row number  *
 * and seat letter), along with what type of ticket has been reserved for the seat and the       *
 * current availability. Accessor and mutator functions are also included.                       *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package Tickets;

public abstract class Node
{  
    //Member variables to store the attributes of each seat.
    private int row;
    private char seat,
                 ticketType;
    private boolean reserved;
    
    Node()
    {
        //Calls the main constructor with default arguments.
        this(0, ' ', ' ', true);
    }
    Node(int row, char seat, char ticketType, boolean reserved)
    {
        //Sets each of the individual seat variables.
        this.row = row;
        this.seat = seat;
        this.ticketType = ticketType;
        this.reserved = reserved;
    }
    
    public void setRow(int row)
    {
        //Modifies the row number.
        this.row = row;
    }
    public int getRow()
    {
        //Gets the row number.
        return row;
    }
    public void setSeat(char seat)
    {
        //Modifies the seat letter.
        this.seat = seat;
    }
    public char getSeat()
    {
        //Gets the seat letter.
        return seat;
    }
    public void setTicketType(char ticketType)
    {
        //Modifies the ticket type.
        this.ticketType = ticketType;
    }
    public char getTicketType()
    {
        //Gets the ticket type.
        return ticketType;
    }
    public void setReserved(char seat)
    {
        //Modifies the reserved status based on the seat.
        if(seat == '.')
            reserved = false;
        else
            reserved = true;
    }
    public boolean getReserved()
    {
        //Gets the reserved status.
        return reserved;
    }
}