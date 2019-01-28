//Bradley Wersterfer
//bmw170030
//Submitted on 12/4/2018

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Defines an order object for storage in the various user accounts. Each order contains a list  *
 * of theater seats that are tied to it by the main function, which can be added to or otherwise *
 * updated with member functions. The price attached to the current order can also be calculated.*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package Tickets;

import java.util.ArrayList;

public class Order 
{
    //Keeps track of each individual piece of the order.
    private int adults,
                children,
                seniors;
    private ArrayList<TheaterSeat> seats = new ArrayList<>();
    private Auditorium auditorium;
    
    //Default Constructor
    Order()
    {
        //Calls the main constructor with empty values.
        this(null, 0, 0, 0);
    }
    //Main Constructor
    Order(Auditorium auditorium, int row, int seat, int totalSeats)
    {
        //Sets the auditorium and then adds the given selection of seats.
        this.auditorium = auditorium;
        AddSeats(row, seat, totalSeats);
    }
    
    //Takes a starting location and number of tickets to add a selection of seats to this order.
    public void AddSeats(int row, int startingSeat, int totalSeats)
    {
        //Finds the first seat in the selection.
        TheaterSeat curSeat = auditorium.findSeat(row, startingSeat);
        
        //Loops through every seat selected.
        for(int i = 0; i < totalSeats; i++)
        {
            //Determines the ticket type of the current seat.
            switch(curSeat.getTicketType())
            {
                //Adds this seat to the proper ticket counter.
                case 'A':
                    adults++;
                    break;
                case 'C':
                    children++;
                    break;
                case 'S':
                    seniors++;
                    break;
            }
            
            //Adds the seat to this order and then moves on to the next seat.
            seats.add(curSeat);
            curSeat = curSeat.getRight();
        }
    }
    
    //Deletes an individual seat from this order with a given spot in the list of seats.
    public void deleteSeat(int index)
    {
        //Determines what type of seat is being deleted.
        switch(seats.get(index).getTicketType())
        {
            //Decrements the proper counter of ticket types.
            case 'A':
                adults--;
                break;
            case 'C':
                children--;
                break;
            case 'S':
                seniors--;
                break;
        }
        
        //Unreserves the specified seat and removes it from the array list.
        seats.get(index).setTicketType('.');
        seats.get(index).setReserved('.');
        seats.remove(index);
    }
    
    //Determines whether a given seat exists within this order or not.
    public int findSeat(int row, char seat)
    {
        //Loops through all seats associated with this order.
        int index = -1;
        for(int i = 0; i < seats.size(); i++)
        {
            //If the location of the current seat matches the target, then it was found.
            if(row == seats.get(i).getRow() && seat == seats.get(i).getSeat())
            {
                //Notes that the desired seat exists in this order.
                index = i;
            }
        }
        
        //Returns either the index or -1 to signify that the seat was not found.
        return index;
    }
    
    //Returns a formatted string to display the entire current order.
    public String getOrder()
    {
        //Adds all current info to the string on its own line.
        String order = "Auditorium " + auditorium.getAuditoriumNumber() + "\nSeats: ";

        //Loops through every seat attached to this order in order to display it.
        for(int i = 0; i < seats.size(); i++)
        {
            //Displays the current seat in a number-letter format.
            order += (seats.get(i).getRow() + 1);
            order += (char)(seats.get(i).getSeat());
            
            //If there is another seat to be displayed, it is separated by a comma.
            if(i < seats.size() - 1)
                order += ", ";
        }
        
        //Displays the number of each type of ticket sold.
        order += String.format("\nAdult Tickets: %9d \n", adults);
        order += String.format("Child Tickets: %9d \n", children);
        order += String.format("Senior Tickets: %8d ", seniors);

        //Returns the formatted information.
        return order;
    }
    
    //Creates a formatted receipt string for display.
    public String getReceipt()
    {
        //Retrieves the current order information.
        String receipt = getOrder();

        //Tacks on the price and returns the result.
        String price = String.format("%.2f", calculatePrice());
        receipt += String.format("\nPrice: %17s \n", "$" + price);
        return receipt;
    }
    
    //Calculates how much this order will cost with the current seats.
    public float calculatePrice()
    {
        return (float)((adults * 10) + (children * 5) + (seniors * 7.5));
    }
    //Accessors for the current auditorium and seats.
    public Auditorium getAuditorium()
    {
        //Gets this order's auditorium.
        return auditorium;
    }
    public ArrayList<TheaterSeat> getSeats()
    {
        //Gets this order's list of seats.
        return seats;
    }
    
    //Mutators for each of the pieces of the order.
    public void setAuditorium(Auditorium auditorium)
    {
        //Sets this order's auditorium.
        this.auditorium = auditorium;
    }
    public void setAdults(int adults)
    {
        //Sets the number of adult tickets.
        this.adults = adults;
    }
    public void setChildren(int children)
    {
        //Sets the number of child tickets.
        this.children = children;
    }
    public void setSeniors(int seniors)
    {
        //Sets the number of senior tickets.
        this.seniors = seniors;
    }
}