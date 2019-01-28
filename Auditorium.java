//Bradley Wersterfer
//bmw170030
//Submitted on 12/4/2018

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Creates a doubly linked list grid of theaterseat objects for use by the main function.        *
 * This supports the storage of seat arrangements in text files, and will construct the          *
 * auditorium again when the program is reopened with any changes that were made.                *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package Tickets;

import java.util.Scanner;

public class Auditorium 
{
    private TheaterSeat first;
    private int numSeats,
                numRows;
    private int auditoriumNumber;
    
    //Default Constructor
    Auditorium()
    {
        //Sets the entire auditorium to be empty.
        first = null;
        numSeats = 0;
        numRows = 0;
    }
    //Main Constructor
    Auditorium(Scanner file, int auditoriumNumber)
    {
        String temp;
        int row = 0;
        TheaterSeat cur = null;
        TheaterSeat topRow = null;
        
        //Sets the current auditorium number.
        this.auditoriumNumber = auditoriumNumber;
        
        //Loops through every line of data in the file.
        while(file.hasNextLine())
        {
            //Stores the next line of seats from the file for processing.
            temp = file.nextLine();
            numSeats = temp.length();
            
            if(first == null)
            {
                //Creates the first seat if necessary.
                first = new TheaterSeat(0, 'A', temp.charAt(0), null, null);
                cur = first;
            }
            else
                //If the first seat already exists, sets the row above the current one.
                topRow = cur;
            
            //The row was odd (index is row - 1).
            if(row % 2 == 0)
            {
                //Checks to see if this row had another one above it.
                if(row > 0)
                {
                    //Moves the current pointer down to the next row.
                    cur.setDown(new TheaterSeat(row, 'A', temp.charAt(0), topRow, null));
                    cur = cur.getDown();
                }
                
                //Runs through each seat in the current row.
                for(int i = 1; i < numSeats; i++)
                {
                    if(topRow != null && topRow.getRight() != null)
                        topRow = topRow.getRight();
                    
                    //Creates the next seat to the right.
                    cur.setRight(new TheaterSeat(row, (char)(i + 'A'), temp.charAt(i), topRow,  cur));

                    //Moves the cursors to the next seat.
                    if(cur.getRight() != null)
                        cur = cur.getRight();
                    
                }
            }
            else //Row was even.
            {
                //Creates the first seat in the new row.
                cur.setDown(new TheaterSeat(row, (char)(numSeats - 1 + 'A'), temp.charAt(numSeats - 1), topRow, null));
                cur = cur.getDown();
                
                //Goes backwards through the string to make the next row, skipping the first seat.
                for(int i = numSeats - 2; i >= 0; i--)
                {
                    if(topRow.getLeft() != null)
                        topRow = topRow.getLeft();
                    
                    //Creates the next seat to the left.
                    cur.setLeft(new TheaterSeat(row, (char)(i + 'A'), temp.charAt(i), topRow, cur));
                    
                    //Moves the pointers for this row and the one above it to the left.
                    if(cur.getLeft() != null)
                        cur = cur.getLeft();
                    
                    
                    //Sets the seat above this one to point to it.
                    topRow.setDown(cur);
                }
            }
            
            //Keeps track of the total number of rows in the auditorium.
            row++;
        }
        
        //Stores the number of rows in the auditorium.
        numRows = row;
    }
    
    //Finds an individual seat in the auditorium.
    public TheaterSeat findSeat(int row, int seat)
    {
        //Loops through until the proper row is found.
        TheaterSeat cur = first;
        for(int i = 0; i < row; i++)
        {
            //Moves to the next row.
            if(cur.getDown() != null)
                cur = cur.getDown();
        }
        
        //Loops through until the proper column is found.
        for(int j = 0; j < seat; j++)
        {
            //Moves once to the right.
            if(cur.getRight() != null)
                cur = cur.getRight();
        }
        
        //Returns the final pointer location.
        return cur;
    }
    
    //Finds the number of open seats in this auditorium.
    public int getSeatsOfType(char letter)
    {
        //Moves through each row of the auditorium.
        TheaterSeat cur = first;
        TheaterSeat firstInRow;
        int seatType = 0;
        while(cur != null)
        {
            //Runs through each seat of the current row.
            firstInRow = cur.getDown();
            while(cur != null)
            {
                //Reads in the current seat.
                if(cur.getTicketType() == letter)
                    seatType++;
                
                //Moves the current pointer to the right.
                cur = cur.getRight();
            }
            cur = firstInRow;
        }
        
        //Returns the final count of seats of the given type.
        return seatType;
    }
    
    //Counts the total number of seats that are reserved.
    public int getTotalReservedSeats()
    {
        //Moves through each row of the auditorium.
        TheaterSeat cur = first;
        TheaterSeat firstInRow;
        int seats = 0;
        while(cur != null)
        {
            //Runs through each seat of the current row.
            firstInRow = cur.getDown();
            while(cur != null)
            {
                //Reads in the current seat.
                if(cur.getTicketType() != '.')
                    seats++;
                
                //Moves the current pointer to the right.
                cur = cur.getRight();
            }
            cur = firstInRow;
        }
        
        //Returns the final count of reserved seats.
        return seats;
    }
    
    public double getSales()
    {
        //Temporary holder for the seat number.
        char letter;
        int numAdults = 0,
            numKids = 0,
            numSeniors = 0;
        
        //Moves through each row of the auditorium.
        TheaterSeat cur = first;
        TheaterSeat firstInRow;
        while(cur != null)
        {
            //Runs through each seat of the current row.
            firstInRow = cur.getDown();
            while(cur != null)
            {
                //Reads in the current seat.
                letter = cur.getTicketType();
                
                //Counts the number of each type of ticket sold.
                switch(letter)
                {
                    //Chooses the correct type based on the letter used.
                    case 'A':
                        numAdults++;
                        break;
                    case 'C':
                        numKids++;
                        break;
                    case 'S':
                        numSeniors++;
                        break;
                }
                
                //Moves the current pointer to the right.
                cur = cur.getRight();
            }
            cur = firstInRow;
        }
        
        //Calculates the final sales.
        return (numAdults * 10) + (numKids * 5) + (numSeniors * 7.5);
    }
    
    //Finds the total amount of seats in the auditorium.
    public int getTotalSeats()
    {
        //Because the auditoriums are rectangles, the total seats are found through multiplication.
        return numRows * numSeats;
    }
    //Accessors and mutators for the member variables.
    public void setFirst(TheaterSeat first)
    {
        //Sets the head pointer.
        this.first = first;
    }
    public TheaterSeat getFirst()
    {
        //Returns the head pointer.
        return first;
    }
    public int getNumSeats()
    {
        //Returns the number of seats in a row.
        return numSeats;
    }
    public int getNumRows()
    {
        //Returns the number of rows in the auditorium.
        return numRows;
    }
    public int getAuditoriumNumber()
    {
        //Returns which auditorium this one is.
        return auditoriumNumber;
    }
}