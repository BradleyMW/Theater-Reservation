//Bradley Wersterfer
//bmw170030
//Submitted on 12/4/2018

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * The basic seats that are linked together by the main auditorium class. Each contains a node   * 
 * of basic seat data (defined in the Node class) and pointers to each adjacent seat, whether it *
 * is vertically or horizontally linked. There is also a basic constructor to facilitate the     *
 * initial creation of the linked grid, determining the current row and setting the previous     *
 * element as each one is added.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package Tickets;

public class TheaterSeat extends Node
{
    //Pointers to the surrounding seats.
    private TheaterSeat up,
                        down,
                        left,
                        right;
    
    //Default constructor.
    TheaterSeat()
    {
        //Calls the main constructor with all null values.
        this(0, ' ', ' ', null, null);
    }
    //Main constructor.
    TheaterSeat(int row, char seatLetter, char seatType, TheaterSeat up, TheaterSeat last)
    {
        //Calls the base class constructor for setting seat attributes.
        super(row, seatLetter, seatType, false);
        setReserved(seatType);
        this.up = up;
        
        //Checks to see which direction the row is being built in.
        if(row % 2 == 0)
            this.left = last;
        else
            this.right = last;
    }
    
    public void setUp(TheaterSeat up)
    {
        //Sets the seat directly above this one.
        this.up = up;
    }
    public TheaterSeat getUp()
    {
        //Returns the seat directly above this one.
        return up;
    }
    public void setDown(TheaterSeat down)
    {
        //Sets the seat directly below this one.
        this.down = down;
    }
    public TheaterSeat getDown()
    {
        //Returns the seat directly below this one.
        return down;
    }
    public void setLeft(TheaterSeat left)
    {
        //Sets the seat to the left of this one.
        this.left = left;
    }
    public TheaterSeat getLeft()
    {
        //Returns the seat to the left of this one.
        return left;
    }
    public void setRight(TheaterSeat right)
    {
        //Sets the seat to the right of this one.
        this.right = right;
    }
    public TheaterSeat getRight()
    {
        //Returns the seat to the right of this one.
        return right;
    }
}